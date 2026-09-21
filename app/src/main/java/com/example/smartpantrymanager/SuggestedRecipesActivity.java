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
        loadSuggestedRecipes();
        loadCloseRecipes();
    }

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

    // this is the close recipes that are one ingredient away
    private void loadCloseRecipes() {
        List<Recipe> closeRecipes = dbHelper.getCloseRecipes();

        boolean hasAny = !closeRecipes.isEmpty();
        textCloseRecipeHeader.setVisibility(hasAny ? View.VISIBLE : View.GONE);
        recyclerCloseRecipe.setVisibility(hasAny ? View.VISIBLE : View.GONE);

        RecipeAdapter adapter = new RecipeAdapter(closeRecipes, this::openRecipeDetail);
        recyclerCloseRecipe.setAdapter(adapter);
    }

    private void openRecipeDetail(Recipe recipe) {
        Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}