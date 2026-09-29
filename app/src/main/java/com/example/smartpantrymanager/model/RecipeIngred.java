package com.example.smartpantrymanager.model;

//this handles one ingredient requirement which belongs to a recipe, like 1 egg or 200ml milk
//and will check the users pantry if they have it
public class RecipeIngred {

    private final String name;
    private final double quantity;
    private final String unit;
    public RecipeIngred(String name, double quantity, String unit) {
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
        String qtyStr = quantity == Math.floor(quantity) ? String.valueOf((int) quantity) :
                String.valueOf(quantity);
        String unitPart = (unit == null || unit.trim().isEmpty()) ? "" : unit + " ";
        return qtyStr + " " + unitPart + name;
    }
}
