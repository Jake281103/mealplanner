package com.example.mealmate.model;

import java.util.ArrayList;

public class Recipe {

    private String recipeId;        // Unique identifier for Firestore document
    private String name;      // Recipe name
    private String description; // Recipe description
    private ArrayList<String> ingredients; // Ingredients stored as a list
    private String instructions; // Cooking instructions
    private String prepTime;     // Preparation time in minutes
    private String cookTime;     // Cooking time in minutes
    private String servings;     // Number of servings
    private String category;  // Recipe category (e.g., Dessert, Main Course)
    private String imageUrl;  // URL of the recipe image (if applicable)

    // Default constructor (required for Firestore)
    public Recipe() {}

    // Constructor
    public Recipe(String name, String description, ArrayList<String> ingredients,
                  String instructions, String prepTime, String cookTime, String servings, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public String getServings() {
        return servings;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
