package com.example.mealmate.ui.fav;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FavViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Favourites fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}