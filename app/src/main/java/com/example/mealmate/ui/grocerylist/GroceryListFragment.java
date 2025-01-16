package com.example.mealmate.ui.grocerylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealmate.databinding.FragmentGrocerylistBinding;

public class GroceryListFragment extends Fragment {

    private FragmentGrocerylistBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroceryListViewModel groceryListViewModel =
                new ViewModelProvider(this).get(GroceryListViewModel.class);

        binding = FragmentGrocerylistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGrocery;
        groceryListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}