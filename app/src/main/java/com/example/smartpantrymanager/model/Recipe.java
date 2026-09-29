package com.example.smartpantrymanager.model;

import java.util.ArrayList;
import java.util.List;
//this stores information about a recipe and its ingredients
public class Recipe {

    private final long id;
    private final String name;

    //this will return the steps of the recipe
    private final String steps;

    //empty at first but populates with databaseHelper
    private List<RecipeIngred> ingreds = new ArrayList<>();

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getSteps() {
        return steps;
    }

    public List<RecipeIngred> getIngreds() {
        return ingreds;
    }

    public void setIngreds(List<RecipeIngred> ingreds) {
        this.ingreds = ingreds;
    }


    //helper class for the recyclerView (1 ingredient vs 2 ingredients [plural])
    public String getIngredCount() {
        int count = ingreds.size();
        return count + (count == 1 ? " ingredient" : " ingredients");
    }
}
