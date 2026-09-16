package com.example.smartpantrymanager.util;

import java.util.HashMap;
import java.util.Map;
public final class IngredNormalization {
    // used to normalize units (example grams to g)
    // and changing from plural to singular (example tomatoes to tomato)

    private IngredNormalization() {

    }

    // this will normalize the units and the different ways people may type units
    private static final Map<String, String> Unit_Synonyms = new HashMap<>();
    static {
        Unit_Synonyms.put("gram", "g");
        Unit_Synonyms.put("grams", "g");
        Unit_Synonyms.put("g", "g");
        Unit_Synonyms.put("kg", "kg");
        Unit_Synonyms.put("kilogram", "kg");
        Unit_Synonyms.put("kilograms", "kg");
        Unit_Synonyms.put("kilos", "kg");
        Unit_Synonyms.put("kilo", "kg");
        Unit_Synonyms.put("ml", "ml");
        Unit_Synonyms.put("milliliter", "ml");
        Unit_Synonyms.put("milliliters", "ml");
        Unit_Synonyms.put("millilitre", "ml");
        Unit_Synonyms.put("millilitres", "ml");
        Unit_Synonyms.put("l", "l");
        Unit_Synonyms.put("liter", "l");
        Unit_Synonyms.put("liters", "l");
        Unit_Synonyms.put("litre", "l");
        Unit_Synonyms.put("tsp", "tsp");
        Unit_Synonyms.put("teaspoon", "tsp");
        Unit_Synonyms.put("teaspoons", "tsp");
        Unit_Synonyms.put("tbs", "tbs");
        Unit_Synonyms.put("tablespoon", "tbs");
        Unit_Synonyms.put("tablespoons", "tbs");
        Unit_Synonyms.put("tbsp", "tbs");
        Unit_Synonyms.put("cup", "cup");
        Unit_Synonyms.put("cups", "cup");
        Unit_Synonyms.put("slice", "slice");
        Unit_Synonyms.put("slices", "slice");
        Unit_Synonyms.put("piece", "pcs");
        Unit_Synonyms.put("pieces", "pcs");
        Unit_Synonyms.put("pcs", "pcs");
        Unit_Synonyms.put("pc", "pcs");
        Unit_Synonyms.put("", "pcs");
    }


    //will normalize plural words and make them singular
    //e.g. will turn tomatoes to tomato
    public static String normalizeWords(String normWords) {
        if (normWords == null) return "";
        String word = normWords.trim().toLowerCase().replaceAll("\\s+", " ");
        if (word.endsWith("oes") || word.endsWith("shes") || word.endsWith("ches") || word.endsWith("xes") || word.endsWith("sses")) {

            word = word.substring(0, word.length() - 2);

        } else if (word.endsWith("ies") && word.length() > 4) {
            word = word.substring(0, word.length() - 3) + "y";

        } else if (word.endsWith("s") && !word.endsWith("ss") && word.length() > 3) {
            word = word.substring(0, word.length() -1);
        }
        return word;
    }

    public static String normalizeUnit(String normUnit) {
        if (normUnit == null) return "pcs";
        String unit = normUnit.trim().toLowerCase();
        String norm = Unit_Synonyms.get(unit);
        return norm != null ? norm : unit;
    }

    public static String unitCategory(String normUnit) {
        switch (normUnit) {
            case "g":
            case "kg":
                return "mass";
            case "ml":
            case "l":
                return "volume";
            default:
                return "count:" + normUnit;
        }
    }

    public static double toBaseQuantity(double quantity, String normUnit) {
        switch (normUnit) {
            case "kg":
            case "l":
                return quantity * 1000;
            default:
                return quantity;
        }
    }
}
