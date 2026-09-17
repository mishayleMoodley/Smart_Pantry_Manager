package com.example.smartpantrymanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantrymanager.model.Ingred;

import java.util.ArrayList;
import java.util.List;

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

    private static final String CREATE_TABLE_PANTRY =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_P_NAME + " TEXT NOT NULL, " +
                COL_P_QTY + " REAL NOT NULL, " +
                COL_P_UNIT + " TEXT NOT NULL, " +
                COL_P_EXPIRY + " TEXT)";

    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COL_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  "+
                    COL_R_NAME + " TEXT NOT NULL,  "+
                    COL_R_STEPS + " TEXT NOT NULL);";

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
}
