package com.example.mealmate.model;

public class MealPlan {

    private String mealPlanId;
    private Recipe recipe;
    private String date;

    public MealPlan(Recipe recipe, String date) {
        this.recipe = recipe;
        this.date = date;
    }

    public MealPlan(){};

    public String getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(String mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
