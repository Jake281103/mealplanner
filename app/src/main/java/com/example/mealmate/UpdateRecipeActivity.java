package com.example.mealmate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealmate.model.Recipe;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UpdateRecipeActivity extends AppCompatActivity {
    private EditText nameInput, descInput, ingredientsInput, instructionsInput, prepInput, cookInput, servingsInput, imageUrlInput;
    private RadioGroup categoryGroup;
    private ArrayList<String> ingredients;
    private Button saveChangesButton, backButton;
    private FirebaseFirestore db;
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        // Initialize views
        nameInput = findViewById(R.id.updateRecipeName);
        descInput = findViewById(R.id.updateRecipeDesc);
        ingredientsInput = findViewById(R.id.updateRecipeIngredient);
        instructionsInput = findViewById(R.id.updateRecipeInstruction);
        prepInput = findViewById(R.id.updatePrepTime);
        cookInput = findViewById(R.id.updateCookingTime);
        servingsInput = findViewById(R.id.updateServing);
        imageUrlInput = findViewById(R.id.updateImage);
        saveChangesButton = findViewById(R.id.saveUpdateButton);
        categoryGroup = findViewById(R.id.updateCategory);
        backButton = findViewById(R.id.backButton);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get recipeId from intent
        recipeId = getIntent().getStringExtra("recipeId");

        Toast.makeText(this, recipeId, Toast.LENGTH_SHORT).show();

        if (recipeId == null) {
            Log.e("UpdateRecipeActivity", "Error: recipeId is null");
            Toast.makeText(this, "Error: Unable to load recipe details", Toast.LENGTH_SHORT).show();
            finish();  // Finish the activity if recipeId is null
        }


        // Load existing recipe details
        loadRecipeDetails();

        // Save changes button
        saveChangesButton.setOnClickListener(v -> saveRecipeChanges());

        backButton.setOnClickListener(v -> finish());

    }

    private void loadRecipeDetails() {
        db.collection("userRecipes").document(recipeId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch the 'recipe' field from the document
                        Recipe recipe = documentSnapshot.get("recipe", Recipe.class);

                        if (recipe != null) {
                            // Populate fields with existing recipe data
                            nameInput.setText(recipe.getName());
                            descInput.setText(recipe.getDescription());
                            ingredientsInput.setText(String.join(", ", recipe.getIngredients()));
                            instructionsInput.setText(recipe.getInstructions());
                            prepInput.setText(recipe.getPrepTime());
                            cookInput.setText(recipe.getCookTime());
                            servingsInput.setText(recipe.getServings());
                            imageUrlInput.setText(recipe.getImageUrl());

                            // Set the correct category based on recipe data
                            setCategorySelection(recipe.getCategory());
                        }
                    } else {
                        Log.e("UpdateRecipeActivity", "Document not found!");
                        Toast.makeText(this, "Recipe not found in Firestore", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("UpdateRecipeActivity", "Error fetching document", e);
                    Toast.makeText(this, "Failed to load recipe", Toast.LENGTH_SHORT).show();
                });
    }


    private void setCategorySelection(String category) {
        switch (category.toLowerCase()) {
            case "breakfast":
                categoryGroup.check(R.id.breakfast);
                break;
            case "lunch":
                categoryGroup.check(R.id.lunch);
                break;
            case "dinner":
                categoryGroup.check(R.id.dinner);
                break;
            default:
                categoryGroup.clearCheck(); // Clear any selected category if not found
        }
    }

    private void saveRecipeChanges() {
        // Get updated recipe details
        String name = nameInput.getText().toString().trim();
        String description = descInput.getText().toString().trim();
        String ingredientsText = ingredientsInput.getText().toString().trim();
        String instructions = instructionsInput.getText().toString().trim();
        String prepTime = prepInput.getText().toString().trim();
        String image = imageUrlInput.getText().toString().trim();
        String cookTime = cookInput.getText().toString().trim();
        String servings = servingsInput.getText().toString().trim();
        ingredients = new ArrayList<>();
        for (String ingredient : ingredientsText.split(",")) {
            ingredients.add(ingredient.trim());
        }

        // Get the selected category
        int selectedCategoryId = categoryGroup.getCheckedRadioButtonId();
        RadioButton selectedCategoryButton = findViewById(selectedCategoryId);
        String category = selectedCategoryButton != null ? selectedCategoryButton.getText().toString() : "Other";

        // Validate inputs
        if (name.isEmpty() || description.isEmpty() || image.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()
                || prepTime.isEmpty() || cookTime.isEmpty() || servings.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create updated recipe object
        Recipe updatedRecipe = new Recipe(name, description, ingredients, instructions, prepTime, cookTime, servings, category, image);
        updatedRecipe.setRecipeId(recipeId);

        // Update Firestore document
        db.collection("userRecipes").document(recipeId).update("recipe", updatedRecipe)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
                });
    }
}