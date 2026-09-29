package com.example.smartpantrymanager;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//this handles the bottom navigation to swap to the different screens
//each activity will call this class
final class NavHelper {
    private NavHelper() {
    }

    // currentItemID is the id of the activity that is currently open
    static void select(AppCompatActivity activity, int currentItemId) {
        BottomNavigationView navigationView = activity.findViewById(R.id.bottomNavigation);
        if (navigationView != null) {
            navigationView.setSelectedItemId(currentItemId);
        }
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

        //sets the tapped tabs id to the activity that will be opened
        Class<?> destination = null;
        if (id == R.id.nav_pantry) {
            destination = MainActivity.class;
        } else if (id == R.id.nav_recipes) {
            destination = SuggestedRecipesActivity.class;
        } else if (id == R.id.nav_settings) {
            destination = SettingsActivity.class;
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
