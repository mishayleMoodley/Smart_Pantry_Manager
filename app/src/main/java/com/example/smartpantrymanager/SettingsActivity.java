package com.example.smartpantrymanager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SmartPantryPreferences";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts_enabled";
    public static final String KEY_UNIT_SYSTEM = "unit_system"; // metric or imperial

    private Switch switchExpiryAlerts;
    private RadioGroup radioGroupUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        radioGroupUnits = findViewById(R.id.radioGroupUnits);

        loadPreferences();

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPreferences().edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());

        radioGroupUnits.setOnCheckedChangeListener((group, checkedId) -> {
            String unitSystem = checkedId == R.id.radioImperial ? "imperial" : "metric";
            getPreferences().edit().putString(KEY_UNIT_SYSTEM, unitSystem).apply();
        });

        NavHelper.setup(this, R.id.nav_settings);
    }

    private void loadPreferences() {
        SharedPreferences prefs = getPreferences();
        boolean alertsEnabled = prefs.getBoolean(KEY_EXPIRY_ALERTS, true);
        switchExpiryAlerts.setChecked(alertsEnabled);

        String unitSystem = prefs.getString(KEY_UNIT_SYSTEM, "metric");
        if ("imperial".equals(unitSystem)) {
            radioGroupUnits.check(R.id.radioImperial);
        } else {
            radioGroupUnits.check(R.id.radioMetric);
        }
    }

    private SharedPreferences getPreferences() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }
}