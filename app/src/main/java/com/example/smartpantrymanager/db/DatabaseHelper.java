package com.example.smartpantrymanager.db;

import static com.example.smartpantrymanager.util.IngredNormalization.normalizeUnit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantrymanager.model.Ingred;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.util.IngredNormalization;

import java.util.ArrayList;
import java.util.List;

import com.example.smartpantrymanager.model.RecipeIngred;
import java.util.HashMap;
import java.util.Map;

// I am using SQLite as the database
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 1;

    // the pantry table the user owns
    private static final String TABLE_PANTRY = "pantry_items";
    private static final String COL_P_ID = "_id";
    private static final String COL_P_NAME = "name";
    private static final String COL_P_QTY = "quantity";
    private static final String COL_P_UNIT = "unit";
    private static final String COL_P_EXPIRY = "expiry_date";

    //the recipe table
    private static final String TABLE_RECIPES = "recipes";
    private static final String COL_R_ID = "_id";
    private static final String COL_R_NAME = "name";
    private static final String COL_R_STEPS = "steps";

    //recipe ingredients requirements
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String COL_RI_ID = "_id";
    private static final String COL_RI_RECIPE_ID = "recipe_id";
    private static final String COL_RI_NAME = "name";
    private static final String COL_RI_QTY = "quantity";
    private static final String COL_RI_UNIT = "unit";

    //pantry table
    private static final String CREATE_TABLE_PANTRY =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_P_NAME + " TEXT NOT NULL, " +
                COL_P_QTY + " REAL NOT NULL, " +
                COL_P_UNIT + " TEXT NOT NULL, " +
                COL_P_EXPIRY + " TEXT)";

    //recipe table
    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COL_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  "+
                    COL_R_NAME + " TEXT NOT NULL,  "+
                    COL_R_STEPS + " TEXT NOT NULL);";

    //recipe ingredients table
    private static final String CREATE_TABLE_RECIPE_INGREDIENTS =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                    COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                    COL_RI_NAME + " TEXT NOT NULL, " +
                    COL_RI_QTY + " REAL NOT NULL, " +
                    COL_RI_UNIT + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " +
                    TABLE_RECIPES + "(" + COL_R_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_RECIPE_INGREDIENTS);
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }



    // PANTRY CRUD METHODS

    public long insertPantryIngreds(Ingred ingred) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = pantryToValues(ingred);
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }
    public int updatePantryIngred(Ingred ingred) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = pantryToValues(ingred);
        int rows = db.update(TABLE_PANTRY, values, COL_P_ID + " = ?",
                new String[]{String.valueOf(ingred.getId())});
        db.close();
        return rows;
    }

    public void deletePantryIngred(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_P_ID + " =?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Ingred getPantryIngred(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, COL_P_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Ingred ingred = null;
        if(c.moveToFirst()) {
            ingred = cursorToIngred(c);
        }
        c.close();
        db.close();
        return ingred;
    }

    public List<Ingred> getAllPantryIngreds() {
        List<Ingred> ingred = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null,
                null, null, COL_P_NAME + " ASC");
        if (c.moveToFirst()) {
            do {
                ingred.add(cursorToIngred(c));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return ingred;
    }

    private ContentValues pantryToValues(Ingred ingred) {
        ContentValues values = new ContentValues();
        values.put(COL_P_NAME, ingred.getName());
        values.put(COL_P_QTY, ingred.getQuantity());
        values.put(COL_P_UNIT, ingred.getUnit());
        values.put(COL_P_EXPIRY, ingred.getExpiryDate());
        return values;
    }

    private Ingred cursorToIngred(Cursor c) {
        return new Ingred(
                c.getLong(c.getColumnIndexOrThrow(COL_P_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_P_NAME)),
                c.getDouble(c.getColumnIndexOrThrow(COL_P_QTY)),
                c.getString(c.getColumnIndexOrThrow(COL_P_UNIT)),
                c.getString(c.getColumnIndexOrThrow(COL_P_EXPIRY)));
    }


    // these functions will read the recipes from the database
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null, COL_R_NAME + " ASC");
        if (c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndexOrThrow(COL_R_ID));
                String name = c.getString(c.getColumnIndexOrThrow(COL_R_NAME));
                String steps = c.getString(c.getColumnIndexOrThrow(COL_R_STEPS));
                Recipe recipe = new Recipe(id, name, steps);
                recipe.setIngreds(getRecipeIngreds(db, id));
                recipes.add(recipe);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return recipes;
    }

    public Recipe getRecipe(long recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, COL_R_ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        Recipe recipe = null;
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndexOrThrow(COL_R_NAME));
            String steps = c.getString(c.getColumnIndexOrThrow(COL_R_STEPS));
            recipe = new Recipe(recipeId, name, steps);
            recipe.setIngreds(getRecipeIngreds(db, recipeId));
        }
        c.close();
        db.close();
        return recipe;
    }

    private List<RecipeIngred> getRecipeIngreds(SQLiteDatabase db, long recipeId) {
        List<RecipeIngred> list = new ArrayList<>();
        Cursor c = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RI_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        if (c.moveToFirst()) {
            do {
                list.add(new RecipeIngred(
                        c.getString(c.getColumnIndexOrThrow(COL_RI_NAME)),
                        c.getDouble(c.getColumnIndexOrThrow(COL_RI_QTY)),
                        c.getString(c.getColumnIndexOrThrow(COL_RI_UNIT))));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }



    // the recipe matching functions, will only return the recipes that can be made with only
    // the ingredients the user has

    public List<Recipe> getSuggestedRecipes() {
        List<Recipe> allRecipes = getAllRecipes();
        Map<String, Double> pantryTotals = buildPantryTotals(getAllPantryIngreds());

        List<Recipe> suggested = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryTotals)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    private Map<String, Double> buildPantryTotals(List<Ingred> pantryIngreds) {
        Map<String, Double> totals = new HashMap<>();
        for (Ingred ingred : pantryIngreds) {
            String key = pantryKey(ingred.getName(), ingred.getUnit());
            String normalizedUnit = normalizeUnit(ingred.getUnit());
            double base = IngredNormalization.toBaseQuantity(ingred.getQuantity(), normalizedUnit);
            Double existing = totals.get(key);
            totals.put(key, existing == null ? base : existing + base);
        }
        return totals;
    }

    private String pantryKey(String name, String unit) {
        String normalizedName = IngredNormalization.normalizeWords(name);
        String normalizedUnit = normalizeUnit(unit);
        String category = IngredNormalization.unitCategory(normalizedUnit);
        return normalizedName + "::" + category;
    }

    private boolean canMakeRecipe(Recipe recipe, Map<String, Double> pantryTotals) {
        for (RecipeIngred req : recipe.getIngreds()) {
            if (!hasEnough(req, pantryTotals)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasEnough(RecipeIngred req, Map<String, Double> pantryTotals) {
        String key = pantryKey(req.getName(), req.getUnit());
        String normalizedUnit = normalizeUnit(req.getUnit());
        double requiredBase = IngredNormalization.toBaseQuantity(req.getQuantity(), normalizedUnit);
        Double available = pantryTotals.get(key);
        return available != null && available >= requiredBase;
    }


    // adding/seeding recipes

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Omelette",
                "1. Beat the eggs in a bowl nad season well with salt. Heat the oil and butter" +
                        " in a non stick frying pan over medium heat until the butter is melted\n" +
                        "2. Pour the eggs into the pan and spread it evenly over the pan. Let the" +
                        " mixture cook for about 20 seconds then scrape a line through the middle" +
                        " with a spatula.",
                new Object[][]{
                        {"egg", 2, "pcs"},
                        {"salt", 1, "tsp"},
                        {"butter", 1, "tsp"},
                        {"bread", 2, "slices"}
                });
        insertRecipe(db, "Homemade Peanut Butter",
                "1. Heat oven to 200C\n" +
                        "2. Put peanuts into large baking tray, place into oven and roast for 10mins" +
                        " Remove when golden brown and let to cool\n" +
                        "3. Add peanuts to food processor and add salt, blend for 4-5mins" +
                        " add oil to loosen consistency",
                new Object[][]{
                        {"peanuts", 400, "g"},
                        {"salt", 2, "tsp"},
                        {"oil", 15, "ml"}
                });
        insertRecipe(db, "Pancakes",
                "1. Add flour in a large bowl and make a well in the center. Crack the eggs" +
                        " in the center, add half of the milk, and whisk/beat until smooth. " +
                        " add the rest of the milk until the mixture is smooth. \n" +
                        "2. Heat a non stick frying pan over medium heat and add a drop of oil" +
                        " covering the whole pan. Add a thin layer of the batter onto the pan" +
                        " Leave the batter for 30 seconds until it starts to change color around " +
                        " the edges\n" +
                        "3. Gently lift up the edges of the pancake using a flat knife, gently " +
                        " moving to the center of the pancake. In one quick movement flip the " +
                        " pancake over and leave for around 30 seconds and remove once done",
                new Object[][]{
                        {"egg", 2, "pcs"},
                        {"flour", 100, "g"},
                        {"milk", 300, "ml"},
                        {"oil", 1, "tbs"}
                });
        insertRecipe(db, "Hash Browns",
                "1. Cook the potatoes in a pan of boiling water for 10 mins then drain and" +
                        " set aside to cool. \n" +
                        "2. Grate the potatoes  into a bowl and removing the skins. Season well" +
                        " with salt and pour over half the butter. Mix well and divide into 8" +
                        " and shape it into a patty. \n" +
                        "3. Heat the oil and the remaining butter in a frying pan until its sizzling" +
                        " and gently fry the potatoes. Fry for 4-5 mins on each side until its" +
                        " crispy and golden. ",
                new Object[][]{
                        {"potato", 3, "pcs"},
                        {"butter", 50, "g"},
                        {"oil", 4, "tbs"},
                        {"salt", 1, "tsp"}
                });
        insertRecipe(db, "Homemade Ice cream",
                "1. Add condensed milk, cream and vanilla into a large bowl. Beat with an " +
                        " electric whisk until thick and stiff.\n" +
                        "2. Scrape it off the bowl and put in the freezer and cover with cling" +
                        " wrap and freeze until solid. \n",
                new Object[][]{
                        {"sweetened condensed milk", 200, "g"},
                        {"cream", 600, "ml"},
                        {"vanilla extract", 1, "tsp"}
                });
        insertRecipe(db, "Fresh Chips",
                "1. Cut up the potatoes into chunky sized chips.\n" +
                        "2. Put the chips in a saucepan. Pour enough oil to cover them by atleast" +
                        " 2cm. Put the pan on high heart and boil the oil. Stirring the chips" +
                        " occasionally. \n" +
                        "3. After 20 mins, the chips should look like they are frying, keep stirring" +
                        " until its golden brown. Remove the chips from the pan and let them cool",
                new Object[][]{
                        {"potato", 5, "pcs"},
                        {"salt", 5, "tsp"},
                        {"oil", 300, "ml"}
                });
        insertRecipe(db, "Crepes",
                "1. Add the flour to a large bowl, crack the eggs and add half of the milk" +
                        " and whisk until smooth and thick. Add remaining milk and whisk more. set" +
                        " aside for 30 mins. \n" +
                        "2. Heat a non stick frying pan over medium heat and add a drop of oil to " +
                        " the pan. When pan is hot, add batter to cover the surface of pan. \n" +
                        "3. Gently lift up the edges of the crepe using a flat knife, gently " +
                        " moving to the center of the crepe. In one quick movement flip the " +
                        " crepe over and leave for around 30 seconds and remove once done \n" +
                        "4. Whip some cream and add to the crepe. ",
                new Object[][]{
                        {"flour", 175, "g"},
                        {"egg", 3, "pcs"},
                        {"milk", 450, "ml"},
                        {"oil", 1, "tbs"},
                        {"cream", 150, "ml"}
                });
        insertRecipe(db, "Shortbread",
                "1. Heat the oven to 170C, add the flour, butter and egg into a mixing bowl, " +
                        " combine the ingredients until the mixture feels like crumbly, then " +
                        " squeeze until it comes together. " +
                        "2. Add some flour to a counter, and use a rolling pin to roll the dough" +
                        " to about half a cm thick. Cut the dough into thin rectangles and place " +
                        " onto a baking tray. Use a fork to create holes. Sprinkle with sugar. \n" +
                        "3. Chill the dough in the fridge for 20mins, and bake for 15-20mins. " +
                        " Remove the shortbread rectangles from the oven and leave to cool. \n",
                new Object[][]{
                        {"flour", 150, "g"},
                        {"butter", 100, "g"},
                        {"caster sugar", 50, "g"}
                });
        insertRecipe(db, "Peanut Butter Cookies",
                "1. Heat oven to 180C, and line 2 large baking trays with baking paper. " +
                        " Add the peanut butter and sugar into a bowl. Add the salt and mix well" +
                        " with a spoon. Add the egg and mix again until it turns into a dough. \n" +
                        "2. Break off small pieces of dough and place it on the baking tray with" +
                        " a decent spacing between them. Press them down with a fork and spread " +
                        " them. \n" +
                        "3. Bake for 12 mins until golden around the edges. Cool on the trays for 10" +
                        "mins and transfer to a wire rack and cool. ",
                new Object[][]{
                        {"peanut butter", 200, "g"},
                        {"caster sugar", 175, "g"},
                        {"salt", 1, "tsp"},
                        {"egg", 1, "pcs"}
                });
        insertRecipe(db, "Homemade Tortillas",
                "1. Mix together the flour and salt. Mix the oil and 150ml of water " +
                        " into the dry ingredients to form a soft dough. Knead for 1-2 mins\n" +
                        "2. Split the dough into 25g pieces and roll into thin circular shapes. " +
                        " Heat a dry pan until very hot and fry each tortilla for 20 secs on " +
                        " each side. \n" +
                        "3. Remove any excess flour if needed. ",
                new Object[][]{
                        {"self raising flour", 225, "g"},
                        {"oil", 1, "tbsp"}
                });

    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingreds) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_R_NAME, name);
        recipeValues.put(COL_R_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (Object[] ingred : ingreds) {
            ContentValues riValues = new ContentValues();
            riValues.put(COL_RI_RECIPE_ID, recipeId);
            riValues.put(COL_RI_NAME, (String) ingred[0]);
            riValues.put(COL_RI_QTY, ((Number) ingred[1]).doubleValue());
            riValues.put(COL_RI_UNIT, (String) ingred[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, riValues);
        }
    }


}
