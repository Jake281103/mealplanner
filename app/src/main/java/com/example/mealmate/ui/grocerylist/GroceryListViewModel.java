package com.example.mealmate.ui.grocerylist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroceryListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GroceryListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Grocery List fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}