package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.Ingred;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textEmpty = findViewById(R.id.textEmptyPantry);

        FloatingActionButton fab = findViewById(R.id.fabAddIngredient);
        fab.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, AddEditIngredActivity.class)));

        NavHelper.setup(this, R.id.nav_pantry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<Ingred> ingred = dbHelper.getAllPantryIngreds();

        if (ingred.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        PantryAdapter adapter = new PantryAdapter(ingred, new PantryAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(Ingred ingred) {
                Intent intent = new Intent(MainActivity.this, AddEditIngredActivity.class);
                intent.putExtra(AddEditIngredActivity.EXTRA_INGREDIENT_ID, ingred.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Ingred item) {
                confirmDelete(item);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void confirmDelete(Ingred item) {
        new AlertDialog.Builder(this)
                .setTitle(item.getName())
                .setMessage("Remove this ingredient from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deletePantryIngred(item.getId());
                    loadPantryItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}