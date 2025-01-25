package com.example.mealmate.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.RecipeAdapter;
import com.example.mealmate.ValidationActivity;
import com.example.mealmate.databinding.FragmentHomeBinding;
import com.example.mealmate.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private RecipeAdapter recommendedAdapter;
    private RecipeAdapter userRecipesAdapter;
    private RecipeAdapter mealPlanRecipesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        // Set up RecyclerViews
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

        return root;
    }

    private void setupRecommendedRecyclerView() {
        // Get RecyclerView from binding
        RecyclerView recommendedRecyclerView = binding.recommendedRecyclerView;

        // Set layout manager to make it horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recommendedRecyclerView.setLayoutManager(layoutManager);

        // Dummy data for testing
        List<Recipe> recommendedRecipes = new ArrayList<>();
        recommendedRecipes.add(new Recipe("Ragi Roti", "Delicious breakfast", new ArrayList<>(), "Cook instructions", "30", "10", "2", "Breakfast", "https://cdn.loveandlemons.com/wp-content/uploads/2024/07/ratatouille.jpg"));
        recommendedRecipes.add(new Recipe("Lemon Rice", "Tasty lunch", new ArrayList<>(), "Cook instructions", "10", "5", "3", "Lunch", "https://www.simplyrecipes.com/thmb/KRw_r32s4gQeOX-d07NWY1OlOFk=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Homemade-Pizza-Dough-Lead-Shot-1c-c2b1885d27d4481c9cfe6f6286a64342.jpg"));
        recommendedRecipes.add(new Recipe("Paneer Curry", "Main course dinner", new ArrayList<>(), "Cook instructions", "20", "15", "4", "Dinner", "https://www.eatingwell.com/thmb/iCdLRBC1BMcDYKRYMTyyToQ8mRs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/8401873-ad2429ae1858464a92229875c91c093d.jpg"));

        // Set the adapter for the RecyclerView
        recommendedAdapter = new RecipeAdapter(getContext(), recommendedRecipes);
        recommendedRecyclerView.setAdapter(recommendedAdapter);
    }

    private void setupUserRecipesRecyclerView() {
        // Get RecyclerView from binding
        RecyclerView userRecipesRecyclerView = binding.userRecipesRecyclerView;

        // Set layout manager to make it horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        userRecipesRecyclerView.setLayoutManager(layoutManager);

        // Dummy data for user-created recipes (replace this with real data from Firestore)
        List<Recipe> userRecipes = new ArrayList<>();
        userRecipes.add(new Recipe("Homemade Pizza", "Cheesy and delicious", new ArrayList<>(), "Bake for 20 mins", "35", "20", "3", "Dinner", "https://img.freepik.com/free-photo/fried-chicken-with-mushrooms-rice_140725-9648.jpg?ga=GA1.1.1770578812.1736612313&semt=ais_hybrid"));
        userRecipes.add(new Recipe("Chocolate Cake", "Perfect dessert", new ArrayList<>(), "Bake for 25 mins", "20", "25", "8", "Dessert", "https://img.freepik.com/free-photo/papaya-salad-served-with-rice-noodles-vegetable-salad-decorated-with-thai-food-ingredients_1150-26500.jpg?ga=GA1.1.1770578812.1736612313&semt=ais_hybrid"));
        userRecipes.add(new Recipe("Pasta Salad", "Healthy and quick", new ArrayList<>(), "Mix and serve", "25", "30", "2", "Lunch", "https://img.freepik.com/free-photo/pad-thai-white-plate-with-lemon-wooden-table_1150-21185.jpg?ga=GA1.1.1770578812.1736612313&semt=ais_hybrid"));
        userRecipes.add(new Recipe("PhaThai", "Healthy and Spicy", new ArrayList<>(), "Mix and serve", "22", "40", "3", "Dinner", "https://img.freepik.com/free-photo/stir-fried-fresh-rice-flour-noodles-with-mixed-meat-egg_1339-5383.jpg?t=st=1737787569~exp=1737791169~hmac=11862f2bf483d57fcfe0a90182f4871d33d0764d3aaa33a3450d119a6550342e&w=1380"));
        userRecipes.add(new Recipe("Vegetable BBQ", "Protein and quick", new ArrayList<>(), "Mix and serve", "18", "23", "4", "Breakfast", "https://img.freepik.com/free-photo/bbq-with-variety-meats-complete-with-tomatoes-bell-peppers-white-plate_1150-22588.jpg?ga=GA1.1.1770578812.1736612313&semt=ais_hybrid"));

        // Set the adapter for the RecyclerView
        userRecipesAdapter = new RecipeAdapter(getContext(), userRecipes);
        userRecipesRecyclerView.setAdapter(userRecipesAdapter);
    }

    private void setupMealPlanRecipesRecyclerView() {
        RecyclerView mealPlanRecipesRecyclerView = binding.mealPlanRecipesRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mealPlanRecipesRecyclerView.setLayoutManager(layoutManager);

        List<Recipe> mealPlanRecipes = new ArrayList<>();
        mealPlanRecipes.add(new Recipe("Vegan Salad", "Fresh and healthy", new ArrayList<>(), "Mix and serve", "10", "0", "2", "Lunch", "https://img.freepik.com/free-photo/spring-rolls-with-cheese-dark-background_1150-45277.jpg?t=st=1737789798~exp=1737793398~hmac=cfe249e3fc02fd6f233e6ae79d4ec03c4032102f481ca3f8afcce76a51295e52&w=1380"));
        mealPlanRecipes.add(new Recipe("Grilled Chicken", "Juicy and delicious", new ArrayList<>(), "Grill for 25 mins", "20", "25", "4", "Dinner", "https://img.freepik.com/free-photo/toasted-meatballs-topped-with-spicy-sauce_1150-27282.jpg?ga=GA1.1.1770578812.1736612313&semt=ais_hybrid"));
        mealPlanRecipes.add(new Recipe("Oatmeal Bowl", "Perfect breakfast", new ArrayList<>(), "Add toppings", "5", "0", "1", "Breakfast", "https://img.freepik.com/free-photo/stir-fry-spaghetti-put-pork-bowl_1150-24192.jpg?ga=GA1.1.1770578812.1736612313&semt=ais_hybrid"));

        mealPlanRecipesAdapter = new RecipeAdapter(getContext(), mealPlanRecipes);
        mealPlanRecipesRecyclerView.setAdapter(mealPlanRecipesAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
