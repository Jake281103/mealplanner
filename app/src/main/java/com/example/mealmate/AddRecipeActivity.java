package com.example.mealmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mealmate.databinding.ActivityAddRecipeBinding;
import com.example.mealmate.model.Recipe;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    private ActivityAddRecipeBinding binding; // Binding class generated for activity_add_recipe.xml
    private RecipeDatabaseHandler databaseHandler;
    private String name, description, ingredientsText,instructions,prep,cook,servings,imageUrl,category;
    private ArrayList<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the binding object
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize database handler
        databaseHandler = new RecipeDatabaseHandler(this);

        // Set up button click listener
        binding.createRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe();
            }
        });
    }

    private void addRecipe() {
        // Collect data from the input fields using binding
        name = binding.recipeName.getText().toString().trim();
        description = binding.recipeDesc.getText().toString().trim();
        ingredientsText = binding.recipeIngredient.getText().toString().trim();
        ingredients = new ArrayList<>();
        for (String ingredient : ingredientsText.split(",")) {
            ingredients.add(ingredient.trim());
        }
        instructions = binding.recipeInstruction.getText().toString().trim();
        prep = binding.prepTime.getText().toString().trim();
        cook = binding.cookingTime.getText().toString().trim();
        servings = binding.serving.getText().toString().trim();
        imageUrl = binding.image.getText().toString().trim();

        // Get selected category
        int selectedCategoryId = binding.category.getCheckedRadioButtonId();
        RadioButton selectedCategoryButton = findViewById(selectedCategoryId);
        category = selectedCategoryButton != null ? selectedCategoryButton.getText().toString() : "Other";

        // Validate inputs
        if (name.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()
                || prep.isEmpty() || cook.isEmpty() || servings.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Recipe object
        Recipe recipe = new Recipe(name, description, ingredients, instructions, prep, cook, servings, category, imageUrl);

        // Add the recipe to Firestore
        databaseHandler.addRecipeWithId(recipe);

        Intent intent = new Intent(AddRecipeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Clean up the binding reference
    }
}