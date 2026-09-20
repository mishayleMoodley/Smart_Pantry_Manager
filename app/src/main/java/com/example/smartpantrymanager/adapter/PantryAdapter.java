package com.example.smartpantrymanager.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.Ingred;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder>{

    public interface OnItemActionListener {
        void onItemClick(Ingred ingred);
        void onDeleteClick(Ingred ingred);
    }
    
    private final List<Ingred> ingreds;
    private final OnItemActionListener listener;
    public PantryAdapter(List<Ingred> ingreds, OnItemActionListener listener) {
        this.ingreds = ingreds;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingred_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        Ingred ingred = ingreds.get(position);

        String qtyText = ingred.getQuantity() == Math.floor(ingred.getQuantity())
                ? String.valueOf((int) ingred.getQuantity()) : String.valueOf(ingred.getQuantity());

        holder.textName.setText(ingred.getName());
        holder.textQty.setText(qtyText + " " + ingred.getUnit());

        if (ingred.getExpiryDate() != null && !ingred.getExpiryDate().isEmpty()) {
            holder.textExpiry.setText(ingred.getExpiryDate());
            holder.textExpiry.setVisibility(View.VISIBLE);
        } else {
            holder.textExpiry.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(ingred));
        holder.buttonDelete.setOnClickListener(v -> listener.onDeleteClick(ingred));
    }

    @Override
    public int getItemCount() {
        return ingreds.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final TextView textQty;
        private final TextView textExpiry;
        private final ImageButton buttonDelete;

        PantryViewHolder (@NotNull View ingredView) {
            super(ingredView);
            textName = ingredView.findViewById(R.id.textIngredName);
            textQty = ingredView.findViewById(R.id.textIngredQty);
            textExpiry = ingredView.findViewById(R.id.textIngredExpiry);
            buttonDelete = ingredView.findViewById(R.id.buttonDeleteIngred);

        }
    }


}
