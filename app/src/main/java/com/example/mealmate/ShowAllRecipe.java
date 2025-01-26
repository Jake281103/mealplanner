package com.example.mealmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mealmate.databinding.ActivityShowAllRecipeBinding;
import com.example.mealmate.model.Recipe;

import java.util.ArrayList;

public class ShowAllRecipe extends AppCompatActivity {

    private ActivityShowAllRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge display
        binding = ActivityShowAllRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Apply insets for Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Example: Use binding to access UI elements
        binding.backToHome.setOnClickListener(v -> finish());

        // Initialize RecyclerView or other components here using binding
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AllRecipeAdapter adapter = new AllRecipeAdapter(this, new ArrayList<>()); // Pass an empty list initially
        binding.recipeRecyclerView.setAdapter(adapter);

        // Load data (e.g., from Firestore or local database) and update the adapter
        loadAllRecipes(adapter);
    }

    private void loadAllRecipes(AllRecipeAdapter adapter) {
        RecipeDatabaseHandler recipeDatabaseHandler = new RecipeDatabaseHandler(this);

        recipeDatabaseHandler.getAllRecipes(new RecipeDatabaseHandler.RecipeUpdateListener() {
            @Override
            public void onRecipesUpdated(ArrayList<Recipe> recipes) {
                adapter.updateData(recipes); // Update adapter with real data
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace(); // Handle error
            }
        });
    }
}
