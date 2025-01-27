package com.example.mealmate;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mealmate.databinding.ActivityShowAllUserRecipeBinding;
import com.example.mealmate.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class ShowAllUserRecipe extends AppCompatActivity {

    private ActivityShowAllUserRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge display
        binding = ActivityShowAllUserRecipeBinding.inflate(getLayoutInflater());
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
        UserRecipeAdapter adapter = new UserRecipeAdapter(this, new ArrayList<>()); // Pass an empty list initially
        binding.recipeRecyclerView.setAdapter(adapter);

        // Load data (e.g., from Firestore or local database) and update the adapter
        loadAllRecipes(adapter);
    }

    private void loadAllRecipes(UserRecipeAdapter adapter) {
        RecipeDatabaseHandler recipeDatabaseHandler = new RecipeDatabaseHandler(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("UserRecipesActivity", "User is not logged in.");
            return;
        }

        recipeDatabaseHandler.getAllUserRecipes(userId, new RecipeDatabaseHandler.UserRecipeUpdateListener() {
            @Override
            public void onError(FirebaseFirestoreException error) {
                Log.e("UserRecipesActivity", "Error fetching user recipes", error);
            }

            @Override
            public void onUserRecipesUpdated(ArrayList<Recipe> recipes) {
                // Update RecyclerView with the fetched recipes
                adapter.updateData(recipes);
            }
        });

    }
}