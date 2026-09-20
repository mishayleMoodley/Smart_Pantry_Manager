package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.Ingred;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredActivity extends AppCompatActivity {

    public static final String EXTRA_INGREDIENT_ID = "extra_ingredient_id";

    private static final String[] UNITS = {
            "g", "kg", "ml", "l", "pcs", "slice", "cup", "tsp", "tbsp"
    };

    private DatabaseHelper dbHelper;
    private Ingred currentIngredient;

    private TextInputLayout layoutName;
    private TextInputLayout layoutQuantity;
    private TextInputEditText editName;
    private TextInputEditText editQuantity;
    private TextInputEditText editExpiry;
    private Spinner spinnerUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingred);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        layoutName = findViewById(R.id.layoutName);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, UNITS);
        spinnerUnit.setAdapter(unitAdapter);

        editExpiry.setOnClickListener(v -> showDatePicker());

        long ingredientId = getIntent().getLongExtra(EXTRA_INGREDIENT_ID, -1);
        if (ingredientId != -1) {
            currentIngredient = dbHelper.getPantryIngred(ingredientId);
            if (currentIngredient != null) {
                populateForEdit();
            }
        } else {
            currentIngredient = new Ingred();
        }

        assert currentIngredient != null;
        toolbar.setTitle(currentIngredient.isNew()
                ? R.string.title_add_ingredient : R.string.title_edit_ingredient);

        findViewById(R.id.buttonSaveIngredient).setOnClickListener(v -> saveIngredient());
        findViewById(R.id.buttonDeleteIngredient).setOnClickListener(v -> deleteIngredient());

        if (!currentIngredient.isNew()) {
            findViewById(R.id.buttonDeleteIngredient).setVisibility(android.view.View.VISIBLE);
        }
    }

    private void populateForEdit() {
        editName.setText(currentIngredient.getName());
        editQuantity.setText(formatQuantity(currentIngredient.getQuantity()));
        editExpiry.setText(currentIngredient.getExpiryDate());

        int unitIndex = indexOf(currentIngredient.getUnit());
        if (unitIndex >= 0) {
            spinnerUnit.setSelection(unitIndex);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                    year, month + 1, dayOfMonth);
            editExpiry.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean isFormValid(String name, String quantityText) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            layoutName.setError(getString(R.string.error_name_required));
            valid = false;
        } else {
            layoutName.setError(null);
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            quantity = -1;
        }

        if (quantityText == null || quantityText.trim().isEmpty() || quantity <= 0) {
            layoutQuantity.setError(getString(R.string.error_quantity_invalid));
            valid = false;
        } else {
            layoutQuantity.setError(null);
        }

        return valid;
    }

    private void saveIngredient() {
        String name = editName.getText() == null ? "" : editName.getText().toString().trim();
        String quantityText = editQuantity.getText() == null ? "" : editQuantity.getText().toString().trim();

        if (!isFormValid(name, quantityText)) {
            return;
        }

        currentIngredient.setName(name);
        currentIngredient.setQuantity(Double.parseDouble(quantityText));
        currentIngredient.setUnit((String) spinnerUnit.getSelectedItem());

        String expiry = editExpiry.getText() == null ? "" : editExpiry.getText().toString().trim();
        currentIngredient.setExpiryDate(expiry.isEmpty() ? null : expiry);

        if (currentIngredient.isNew()) {
            dbHelper.insertPantryIngreds(currentIngredient);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updatePantryIngred(currentIngredient);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void deleteIngredient() {
        if (!currentIngredient.isNew()) {
            dbHelper.deletePantryIngred(currentIngredient.getId());
            Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private String formatQuantity(double quantity) {
        return quantity == Math.floor(quantity)
                ? String.valueOf((int) quantity)
                : String.valueOf(quantity);
    }

    private int indexOf(String value) {
        if (value == null) return -1;
        for (int i = 0; i < AddEditIngredActivity.UNITS.length; i++) {
            if (AddEditIngredActivity.UNITS[i].equalsIgnoreCase(value)) return i;
        }
        return -1;
    }
}