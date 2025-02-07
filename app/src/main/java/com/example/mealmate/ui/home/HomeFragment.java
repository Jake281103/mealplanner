package com.example.mealmate.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.RecipeAdapter;
import com.example.mealmate.RecipeDatabaseHandler;
import com.example.mealmate.ShowAllRecipe;
import com.example.mealmate.ShowAllUserRecipe;
import com.example.mealmate.ValidationActivity;
import com.example.mealmate.databinding.FragmentHomeBinding;
import com.example.mealmate.model.Recipe;
import com.example.mealmate.ui.mealplan.MealPlanFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private RecipeAdapter recommendedAdapter;
    private RecipeAdapter userRecipesAdapter;
    private RecipeAdapter mealPlanRecipesAdapter;
    private FirebaseFirestore db;
    private RecipeDatabaseHandler recipeDatabaseHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseApp.initializeApp(this.getContext());
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        recipeDatabaseHandler = new RecipeDatabaseHandler(this.getContext());

        setupRecommendedRecyclerView();
        setupUserRecipesRecyclerView();
        setupMealPlanRecipesRecyclerView();

        // Logout button functionality
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sign out the user from Firebase
                mAuth.signOut();

                // Redirect to ValidationActivity
                Intent intent = new Intent(requireActivity(), ValidationActivity.class);
                startActivity(intent);

                // Close the current activity and show a logout message
                requireActivity().finishAffinity();
                Toast.makeText(requireContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });

        binding.viewAllRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Redirect to ValidationActivity
                Intent intent = new Intent(requireActivity(), ShowAllRecipe.class);
                startActivity(intent);

            }
        });

        binding.viewAddedRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to ValidationActivity
                Intent intent = new Intent(requireActivity(), ShowAllUserRecipe.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void setupRecommendedRecyclerView() {
        // Get RecyclerView from binding
        RecyclerView recommendedRecyclerView = binding.recommendedRecyclerView;

        // Set layout manager to make it horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recommendedRecyclerView.setLayoutManager(layoutManager);

        List<Recipe> userRecipes = new ArrayList<>();
        recommendedAdapter = new RecipeAdapter(getContext(), userRecipes);
        recommendedRecyclerView.setAdapter(recommendedAdapter);

        recipeDatabaseHandler.getAllRecipes(new RecipeDatabaseHandler.RecipeUpdateListener() {
            @Override
            public void onRecipesUpdated(ArrayList<Recipe> recipes) {
                recommendedAdapter.updateData(getRandomRecipes(recipes,5)); // Update adapter with real data
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace(); // Handle error
            }
        });

    }

    // Helper method to get random
    private ArrayList<Recipe> getRandomRecipes(List<Recipe> allRecipes, int count) {
        ArrayList<Recipe> randomRecipes = new ArrayList<>();
        Collections.shuffle(allRecipes); // Shuffle the list of recipes
        for (int i = 0; i < count && i < allRecipes.size(); i++) {
            randomRecipes.add(allRecipes.get(i)); // Get the first 'count' recipes
        }
        return randomRecipes;
    }

    private void setupUserRecipesRecyclerView() {
        // Get RecyclerView from binding
        RecyclerView userRecipesRecyclerView = binding.userRecipesRecyclerView;

        // Set layout manager to make it horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        userRecipesRecyclerView.setLayoutManager(layoutManager);

        List<Recipe> userRecipes = new ArrayList<>();
        userRecipesAdapter = new RecipeAdapter(getContext(), userRecipes);
        userRecipesRecyclerView.setAdapter(userRecipesAdapter);

        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("UserRecipesActivity", "User is not logged in.");
            return;
        }

        recipeDatabaseHandler.getAllUserRecipes(userId, new RecipeDatabaseHandler.UserRecipeUpdateListener() {
            @Override
            public void onUserRecipesUpdated(ArrayList<Recipe> recipes) {
                // Update RecyclerView with the fetched recipes
                userRecipesAdapter.updateData(getRandomRecipes(recipes,5));
            }

            @Override
            public void onError(FirebaseFirestoreException error) {
                Log.e("UserRecipesActivity", "Error fetching user recipes", error);
            }
        });
    }

    private void setupMealPlanRecipesRecyclerView() {
        RecyclerView mealPlanRecipesRecyclerView = binding.mealPlanRecipesRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mealPlanRecipesRecyclerView.setLayoutManager(layoutManager);

        ArrayList<Recipe> mealPlanRecipes = new ArrayList<>();
        mealPlanRecipesAdapter = new RecipeAdapter(getContext(), mealPlanRecipes);
        mealPlanRecipesRecyclerView.setAdapter(mealPlanRecipesAdapter);

        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("UserRecipesActivity", "User is not logged in.");
            return;
        }

        recipeDatabaseHandler.getAllMealPlanRecipes(userId, new RecipeDatabaseHandler.MealPlanRecipeUpdateListener() {
            @Override
            public void onMealPlanRecipesUpdated(ArrayList<Recipe> recipes) {
                // Update RecyclerView with the fetched recipes
                mealPlanRecipesAdapter.updateData(getRandomRecipes(recipes,5));
            }

            @Override
            public void onError(FirebaseFirestoreException error) {
                Log.e("UserRecipesActivity", "Error fetching user recipes", error);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
