package com.example.mealmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private Context context;
    private ArrayList<String> groceryList;
    private Set<String> selectedItems;

    public GroceryListAdapter(Context context, ArrayList<String> groceryList) {
        this.context = context;
        this.groceryList = groceryList != null ? groceryList : new ArrayList<>(); // Prevent null list
        this.selectedItems = new HashSet<>(); // Prevent NullPointerException
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grocery_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        String item = groceryList.get(position);
        holder.groceryItemText.setText(item);

        // FIX: Ensure selectedItems is not null
        if (selectedItems == null) {
            selectedItems = new HashSet<>();
        }

        // Set the checkbox state
        holder.groceryItemCheckbox.setOnCheckedChangeListener(null); // Avoid unwanted callbacks
        holder.groceryItemCheckbox.setChecked(selectedItems.contains(item));

        // Handle checkbox toggle
        holder.groceryItemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        });
    }

    public void updateData(ArrayList<String> newGroceryList) {
        this.groceryList = newGroceryList != null ? newGroceryList : new ArrayList<>(); // Prevent null list
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public Set<String> getSelectedItems() {
        return selectedItems;
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView groceryItemText;
        CheckBox groceryItemCheckbox;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            groceryItemText = itemView.findViewById(R.id.grocery_item_text);
            groceryItemCheckbox = itemView.findViewById(R.id.grocery_item_checkbox);
        }
    }
}

