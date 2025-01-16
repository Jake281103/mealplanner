package com.example.mealmate.ui.fav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealmate.databinding.FragmentFavBinding;

public class FavFragment extends Fragment {

    private FragmentFavBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavViewModel favViewModel =
                new ViewModelProvider(this).get(FavViewModel.class);

        binding = FragmentFavBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFav;
        favViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}