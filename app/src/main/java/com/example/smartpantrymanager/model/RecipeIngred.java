package com.example.smartpantrymanager.model;

public class RecipeIngred {

    private String name;
    private double quantity;
    private String unit;
    public RecipeIngred(long id, String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String toDisplayString() {
        String qtyStr = quantity == Math.floor(quantity) ? String.valueOf((int) quantity) : String.valueOf(quantity);
        String unitPart = (unit == null || unit.trim().isEmpty()) ? "" : unit + " ";
        return qtyStr + " " + unitPart + name;
    }
}
