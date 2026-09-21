package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngred;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Recipe recipe = dbHelper.getRecipe(recipeId);

        if (recipe == null) {
            finish();
            return;
        }

        toolbar.setTitle(recipe.getName());

        TextView textName = findViewById(R.id.textDetailRecipeName);
        TextView textIngredients = findViewById(R.id.textDetailIngredients);
        TextView textSteps = findViewById(R.id.textDetailSteps);

        textName.setText(recipe.getName());

        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngred ingredient : recipe.getIngreds()) {
            ingredientsText.append("• ").append(ingredient.toDisplayString()).append("\n");
        }
        textIngredients.setText(ingredientsText.toString().trim());

        textSteps.setText(recipe.getSteps());
    }
}