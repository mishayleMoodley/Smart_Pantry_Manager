package com.example.smartpantrymanager.util;

import java.util.Locale;
import java.util.Map;

public final class IngredNormalization {
    //used to normalize units (example grams to g)
    //and changing from plural to singular (example tomatoes to tomato)
    private IngredNormalization() {

    }

    //this will normalize the units and the different ways people may type units
    private static final Map<String, String> UNIT_SYNONYMS;
    static {
        UNIT_SYNONYMS = Map.ofEntries(
                Map.entry("gram", "g"),
                Map.entry("grams", "g"),
                Map.entry("g", "g"),

                Map.entry("kg", "kg"),
                Map.entry("kilogram", "kg"),
                Map.entry("kilograms", "kg"),
                Map.entry("kilos", "kg"),
                Map.entry("kilo", "kg"),

                Map.entry("ml", "ml"),
                Map.entry("milliliter", "ml"),
                Map.entry("milliliters", "ml"),
                Map.entry("millilitre", "ml"),
                Map.entry("millilitres", "ml"),

                Map.entry("l", "l"),
                Map.entry("liter", "l"),
                Map.entry("liters", "l"),
                Map.entry("litre", "l"),

                Map.entry("tsp", "tsp"),
                Map.entry("teaspoon", "tsp"),
                Map.entry("teaspoons", "tsp"),

                Map.entry("tbs", "tbs"),
                Map.entry("tablespoon", "tbs"),
                Map.entry("tablespoons", "tbs"),
                Map.entry("tbsp", "tbs"),

                Map.entry("cup", "cup"),
                Map.entry("cups", "cup"),

                Map.entry("slice", "slice"),
                Map.entry("slices", "slice"),

                Map.entry("piece", "pcs"),
                Map.entry("pieces", "pcs"),
                Map.entry("pcs", "pcs"),
                Map.entry("pc", "pcs"),
                Map.entry("", "pcs")
        );
    }

    //will normalize plural words and make them singular
    //e.g. will turn tomatoes to tomato
    public static String normalizeWords(String normWords) {
        if (normWords == null) return "";
        String word = normWords.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (word.endsWith("oes") || word.endsWith("shes") || word.endsWith("ches") || word.endsWith("xes") || word.endsWith("sses")) {
            //tomatoes to tomato, dishes to dish for example
            word = word.substring(0, word.length() - 2);
        } else if (word.endsWith("ies") && word.length() > 4) {
            //berries to berry
            word = word.substring(0, word.length() - 3) + "y";
        } else if (word.endsWith("s") && !word.endsWith("ss") && word.length() > 3) {
            //eggs to egg
            word = word.substring(0, word.length() - 1);
        }
        return word;
    }

    //this will map all the different spelling of units and make it equal to the same shortest form
    //of the unit, for example, kilogram/kilos/kilo will all mean kg
    public static String normalizeUnit(String normUnit) {
        if (normUnit == null || normUnit.trim().isEmpty()) return "pcs";
        String unit = normUnit.trim().toLowerCase(Locale.ROOT);
        String norm = UNIT_SYNONYMS.get(unit);
        return norm != null ? norm : unit;
    }

    //this will group units into the same type of category
    //example kg and g will be grouped together
    public static String unitCategory(String normUnit) {
        String unit = normalizeUnit(normUnit);
        switch (unit) {
            case "g":
            case "kg":
            case "ml":
            case "l":
            case "tsp":
            case "tbs":
            case "cup":
                return "measure";
            case "pcs":
            case "pc":
            case "piece":
            case "pieces":
            case "slice":
            case "slices":
            default:
                return "count";
        }
    }

    //will convert a quantity to its smallest base unit in its category
    public static double toBaseQuantity(double quantity, String normUnit) {
        if (normUnit == null) return quantity;

        switch (normalizeUnit(normUnit)) {
            case "kg":
                return quantity * 1000.0; //kg to g base
            case "l":
                return quantity * 1000.0; //l to ml base
            case "cup":
                return quantity * 240.0;  //cup to ml base
            case "tbs":
                return quantity * 15.0;   //tbsp to ml base
            case "tsp":
                return quantity * 5.0;    //tsp to ml base
            default:
                return quantity;
        }
    }
}
