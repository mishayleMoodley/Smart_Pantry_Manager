package com.example.smartpantrymanager.model;

import java.util.ArrayList;
import java.util.List;
public class Recipe {

    private long id;
    private String name;
    private String steps;
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

    public String getIngredCount() {
        int count = ingreds.size();
        return count + (count == 1 ? " ingredient" : " ingredients");
    }
}
