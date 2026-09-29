package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;

import java.util.List;

//this will show both the suggested recipes and the close recipes which require a missing ingredient
//both of these lists will get reloaded using onResume so its always up to date

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private RecyclerView recyclerCloseRecipe;
    private TextView textEmpty;
    private TextView textCloseRecipeHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerCloseRecipe = findViewById(R.id.recyclerCloseRecipes);
        recyclerCloseRecipe.setLayoutManager(new LinearLayoutManager(this));

        textEmpty = findViewById(R.id.textEmptySuggestions);
        textCloseRecipeHeader = findViewById(R.id.textCloseRecipesHeader);

        NavHelper.setup(this, R.id.nav_recipes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.select(this, R.id.nav_recipes);
        loadSuggestedRecipes();
        loadCloseRecipes();
    }

    //shows only the recipes the user can make from their pantry
    //otherwise will show a message saying there are no recipes
    private void loadSuggestedRecipes() {
        List<Recipe> suggested = dbHelper.getSuggestedRecipes();

        if (suggested.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        RecipeAdapter adapter = new RecipeAdapter(suggested, this::openRecipeDetail);
        recyclerView.setAdapter(adapter);
    }

    //this is the close recipes that are one ingredient away
    //the header will only show up if there are any recipes that are close
    private void loadCloseRecipes() {
        List<Recipe> closeRecipes = dbHelper.getCloseRecipes();

        boolean hasAny = !closeRecipes.isEmpty();
        textCloseRecipeHeader.setVisibility(hasAny ? View.VISIBLE : View.GONE);
        recyclerCloseRecipe.setVisibility(hasAny ? View.VISIBLE : View.GONE);

        RecipeAdapter adapter = new RecipeAdapter(closeRecipes, this::openRecipeDetail);
        recyclerCloseRecipe.setAdapter(adapter);
    }

    //this will open the recipe details for both lists
    private void openRecipeDetail(Recipe recipe) {
        Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}