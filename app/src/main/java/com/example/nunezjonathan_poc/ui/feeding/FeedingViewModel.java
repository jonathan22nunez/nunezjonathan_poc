package com.example.nunezjonathan_poc.ui.feeding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FeedingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("My Feeding Screen");
    }

    public LiveData<String> getText() { return mText; }
}
