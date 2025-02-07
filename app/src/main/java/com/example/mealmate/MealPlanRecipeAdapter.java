package com.example.mealmate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.model.MealPlan;
import com.example.mealmate.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class MealPlanRecipeAdapter extends RecyclerView.Adapter<MealPlanRecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<MealPlan> mealPlanList;

    public MealPlanRecipeAdapter(Context context, List<MealPlan> mealPlanList) {
        this.context = context;
        this.mealPlanList = mealPlanList;
    }

    @NonNull
    @Override
    public MealPlanRecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mealplan_recipe_card, parent, false);
        return new MealPlanRecipeAdapter.RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanRecipeAdapter.RecipeViewHolder holder, int position) {
        MealPlan mealPlan = mealPlanList.get(position);

        holder.recipeCategory.setText(mealPlan.getRecipe().getCategory());
        holder.recipeName.setText(mealPlan.getRecipe().getName());
        holder.recipePrepTime.setText(mealPlan.getRecipe().getPrepTime() + " mins");
        holder.mealPlanDate.setText(mealPlan.getDate());

        // Use Picasso or Glide to load the image
        Picasso.get().load(mealPlan.getRecipe().getImageUrl()).into(holder.recipeImage);

        // Handle recipe click event
        holder.itemView.setOnClickListener(v -> {
            showRecipeDetailsDialog(mealPlan.getRecipe()); // Call the method to show the popup modal
        });
    }

    public void showRecipeDetailsDialog(Recipe recipe) {
        // Create and configure the dialog
        Dialog recipeDialog = new Dialog(context);
        recipeDialog.setContentView(R.layout.dialog_recipe_details);

        // Set custom width for the dialog
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(recipeDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;  // Full screen width or set custom width (e.g., 800px)
        recipeDialog.getWindow().setAttributes(params);

        // Initialize views in the dialog
        ImageView recipeImage = recipeDialog.findViewById(R.id.recipeImage);
        TextView recipeName = recipeDialog.findViewById(R.id.recipeName);
        TextView recipeDescription = recipeDialog.findViewById(R.id.recipeDescription);
        TextView recipeIngredients = recipeDialog.findViewById(R.id.recipeIngredients);
        TextView recipeInstructions = recipeDialog.findViewById(R.id.recipeInstructions);
        TextView recipePrepTime = recipeDialog.findViewById(R.id.recipePrepTime);
        TextView recipeCookingTime = recipeDialog.findViewById(R.id.recipeCookingTime);
        TextView recipeServings = recipeDialog.findViewById(R.id.recipeServings);


        // Populate the dialog with recipe details
        Picasso.get().load(recipe.getImageUrl()).into(recipeImage); // Load image
        recipeName.setText(recipe.getName());
        recipeDescription.setText(recipe.getDescription());
        recipeIngredients.setText(String.join(", ", recipe.getIngredients()));
        recipeInstructions.setText(recipe.getInstructions());
        recipePrepTime.setText(String.format("Prep : %s", recipe.getPrepTime()));
        recipeCookingTime.setText(String.format("Cook : %s", recipe.getCookTime()));
        recipeServings.setText(String.format("Servings: %s", recipe.getServings()));

        // Close button in dialog
        ImageView closeButton = recipeDialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> recipeDialog.dismiss()); // Close the dialog

        // Add to MealPlan button in dialog
        Button addtoMealPlanButton = recipeDialog.findViewById(R.id.addMealPlan);
        addtoMealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Create a formatted date string
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                        MealPlan mealPlan = new MealPlan(recipe, selectedDate);

                        // Store the data in Firestore
                        RecipeDatabaseHandler databaseHandler = new RecipeDatabaseHandler(context);
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
        });

        // Show the dialog
        recipeDialog.show();
    }

    public void updateData(List<MealPlan> newMealPlans) {
        this.mealPlanList = newMealPlans;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mealPlanList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeCategory, recipeName, recipePrepTime, mealPlanDate;
        ImageView recipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeCategory = itemView.findViewById(R.id.recipeCategory);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipePrepTime = itemView.findViewById(R.id.recipePrepTime);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            mealPlanDate = itemView.findViewById(R.id.mealPlanDate);
        }
    }
}
