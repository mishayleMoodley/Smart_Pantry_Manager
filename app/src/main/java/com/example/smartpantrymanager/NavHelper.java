package com.example.smartpantrymanager;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
final class NavHelper {
    private NavHelper() {
    }
    static void setup(AppCompatActivity activity, int currentItemId) {
        BottomNavigationView navigationView = activity.findViewById(R.id.bottomNavigation);
        if (navigationView == null){
            return;
        }

    navigationView.setSelectedItemId(currentItemId);

    navigationView.setOnItemSelectedListener(menuItem -> {
        int id = menuItem.getItemId();
        if (id == currentItemId) {
            return true;
        }

        Class<?> destination = null;
        if (id == R.id.nav_pantry) {
            destination = MainActivity.class;
        } else if (id == R.id.nav_recipes) {
            destination = SuggestedRecipesActivity.class;
        }

        if (destination != null) {
            Intent intent = new Intent(activity, destination);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);

        }
        return true;
    });
}
}
