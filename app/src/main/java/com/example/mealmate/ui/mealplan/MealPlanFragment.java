package com.example.mealmate.ui.mealplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealmate.databinding.FragmentMealplanBinding;

public class MealPlanFragment extends Fragment {

    private FragmentMealplanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MealPlanViewModel mealPlanViewModel =
                new ViewModelProvider(this).get(MealPlanViewModel.class);

        binding = FragmentMealplanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMealplan;
        mealPlanViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}