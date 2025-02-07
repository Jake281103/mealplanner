package com.example.mealmate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;

    // Constructor
    public UserRecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    // Inflates the item layout for each row in the RecyclerView
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    // Binds data to the views in each row
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // Set recipe details
        holder.recipeName.setText(recipe.getName());
        holder.recipeDescription.setText(recipe.getDescription());
        holder.recipePrepTime.setText(recipe.getPrepTime() + " mins");
        holder.recipeServings.setText("Servings: " + recipe.getServings());
        holder.recipeCategory.setText(recipe.getCategory());

        // Load the recipe image using Picasso
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            Picasso.get().load(recipe.getImageUrl()).into(holder.recipeImage);
        } else {
            holder.recipeImage.setImageResource(R.drawable.placeholder_image); // Fallback image
        }

        // Handle Edit Button Click
        holder.editRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getRecipeId());
            context.startActivity(intent);
        });

        // Add click listener to show recipe details
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof ShowAllUserRecipe) {
                ((ShowAllUserRecipe) context).showRecipeDetailsDialog(recipe);
            }
        });
    }

    // Returns the total count of items in the RecyclerView
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // Updates the list of recipes dynamically
    public void updateData(List<Recipe> newRecipes) {
        this.recipeList = newRecipes;
        notifyDataSetChanged();
    }

    // Retrieve recipe at a given position
    public Recipe getRecipeAtPosition(int position) {
        return recipeList.get(position);
    }

    // Remove recipe from the list
    public void removeRecipeAtPosition(int position) {
        recipeList.remove(position);
        notifyItemRemoved(position);
    }

    // Restore recipe to the list
    public void restoreRecipe(Recipe recipe, int position) {
        recipeList.add(position, recipe);
        notifyItemInserted(position);
    }

    // ViewHolder class to hold individual item views
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeDescription, recipePrepTime, recipeServings, recipeCategory;
        ImageView recipeImage;
        ImageView editRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeDescription = itemView.findViewById(R.id.recipeDescription);
            recipePrepTime = itemView.findViewById(R.id.recipePrepTime);
            recipeServings = itemView.findViewById(R.id.recipeServings);
            recipeCategory = itemView.findViewById(R.id.recipeCategory);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            editRecipe = itemView.findViewById(R.id.updateButton);
        }
    }
}

