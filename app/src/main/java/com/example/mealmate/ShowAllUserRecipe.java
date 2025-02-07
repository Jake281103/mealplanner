package com.example.mealmate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.databinding.ActivityShowAllUserRecipeBinding;
import com.example.mealmate.model.MealPlan;
import com.example.mealmate.model.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowAllUserRecipe extends AppCompatActivity {

    private ActivityShowAllUserRecipeBinding binding;
    private RecipeDatabaseHandler databaseHandler;
    private UserRecipeAdapter adapter;
    private Recipe recentlyDeletedRecipe;
    private int recentlyDeletedPosition;

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

        databaseHandler = new RecipeDatabaseHandler(this);

        // Example: Use binding to access UI elements
        binding.backToHome.setOnClickListener(v -> finish());

        // Initialize RecyclerView or other components here using binding
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserRecipeAdapter(this, new ArrayList<>()); // Pass an empty list initially
        binding.recipeRecyclerView.setAdapter(adapter);

        // Load data and update the adapter
        loadAllRecipes();

        // Attach swipe-to-delete functionality with undo
        attachSwipeToDelete();
    }

    private void loadAllRecipes() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("UserRecipesActivity", "User is not logged in.");
            return;
        }

        databaseHandler.getAllUserRecipes(userId, new RecipeDatabaseHandler.UserRecipeUpdateListener() {
            @Override
            public void onUserRecipesUpdated(ArrayList<Recipe> recipes) {
                // Update RecyclerView with the fetched recipes
                adapter.updateData(recipes);
            }

            @Override
            public void onError(FirebaseFirestoreException error) {
                Log.e("UserRecipesActivity", "Error fetching user recipes", error);
            }
        });

    }

    // Method to show the recipe details in a dialog
    protected void showRecipeDetailsDialog(Recipe recipe) {
        // Create the dialog
        Dialog recipeDialog = new Dialog(this);
        recipeDialog.setContentView(R.layout.dialog_recipe_details);

        // Set custom width for the dialog
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(recipeDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;  // Full screen width or set custom width (e.g., 800px)
        recipeDialog.getWindow().setAttributes(params);

        // Initialize views inside the dialog
        ImageView recipeImage = recipeDialog.findViewById(R.id.recipeImage);
        TextView recipeName = recipeDialog.findViewById(R.id.recipeName);
        TextView recipeDescription = recipeDialog.findViewById(R.id.recipeDescription);
        TextView recipeIngredients = recipeDialog.findViewById(R.id.recipeIngredients);
        TextView recipeInstructions = recipeDialog.findViewById(R.id.recipeInstructions);
        TextView recipePrepTime = recipeDialog.findViewById(R.id.recipePrepTime);
        TextView recipeCookingTime = recipeDialog.findViewById(R.id.recipeCookingTime);
        TextView recipeServings = recipeDialog.findViewById(R.id.recipeServings);
        TextView recipeCategory = recipeDialog.findViewById(R.id.recipeCategory);
        ImageView closeButton = recipeDialog.findViewById(R.id.closeButton);

        // Set data into the views
        Picasso.get().load(recipe.getImageUrl()).into(recipeImage);
        recipeName.setText(recipe.getName());
        recipeCategory.setText(recipe.getCategory());
        recipeDescription.setText(recipe.getDescription());
        recipeIngredients.setText(String.join(", ", recipe.getIngredients()));
        recipeInstructions.setText(recipe.getInstructions());
        recipePrepTime.setText(String.format("Prep : %s mins", recipe.getPrepTime()));
        recipeCookingTime.setText(String.format("Cook : %s mins", recipe.getCookTime()));
        recipeServings.setText(String.format("Servings: %s", recipe.getServings()));

        // Close the dialog when the close button is clicked
        closeButton.setOnClickListener(v -> recipeDialog.dismiss());

        // Add to MealPlan button in dialog
        Button addtoMealPlanButton = recipeDialog.findViewById(R.id.addMealPlan);
        addtoMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToMealPlan(recipe);
            }
        });

        // Show the dialog
        recipeDialog.show();
    }

    private void addToMealPlan(Recipe recipe){
        // Get the current date and next day date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Set the min and max date for the date picker
        calendar.add(Calendar.DAY_OF_MONTH, 1);  // Move to next day
        int nextDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nextMonth = calendar.get(Calendar.MONTH);
        int nextYear = calendar.get(Calendar.YEAR);

        // Create DatePickerDialog and set min and max date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // Create a formatted date string
                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                MealPlan mealPlan = new MealPlan(recipe, selectedDate);

                // Store the data in Firestore
                databaseHandler.addMealPlan(mealPlan);
            }
        }, year, month, day);

        // Set minimum date to today's date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        // Set maximum date to the next day
        calendar.add(Calendar.DAY_OF_MONTH, 5); // Add one more day for max date
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private void attachSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Drag-and-drop is not supported
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe-to-delete with undo
                int position = viewHolder.getAdapterPosition();
                recentlyDeletedRecipe = adapter.getRecipeAtPosition(position);
                recentlyDeletedPosition = position;

                // Temporarily remove the recipe from the adapter
                adapter.removeRecipeAtPosition(position);

                // Show Snackbar for Undo
                showUndoSnackbar();
            }
        });

        itemTouchHelper.attachToRecyclerView(binding.recipeRecyclerView);
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "Recipe deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> undoDelete());
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    // Snackbar was dismissed without clicking Undo - proceed with deletion
                    deleteRecipeFromFirestore(recentlyDeletedRecipe);
                }
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        // Restore the recipe to the adapter
        adapter.restoreRecipe(recentlyDeletedRecipe, recentlyDeletedPosition);
        recentlyDeletedRecipe = null; // Clear the recently deleted recipe
    }

    private void deleteRecipeFromFirestore(Recipe recipe) {
        if (recipe.getRecipeId() != null) {
            databaseHandler.deleteUserRecipe(recipe.getRecipeId());
        } else {
            Log.e("ShowAllUserRecipe", "Recipe ID is null. Cannot delete.");
        }
    }
}