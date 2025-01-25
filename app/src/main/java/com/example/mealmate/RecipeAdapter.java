package com.example.mealmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.recipeCategory.setText(recipe.getCategory());
        holder.recipeName.setText(recipe.getName());
        holder.recipePrepTime.setText(recipe.getPrepTime() + " mins");

        // Use Picasso or Glide to load the image
        Picasso.get().load(recipe.getImageUrl()).into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeCategory, recipeName, recipePrepTime;
        ImageView recipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeCategory = itemView.findViewById(R.id.recipeCategory);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipePrepTime = itemView.findViewById(R.id.recipePrepTime);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}