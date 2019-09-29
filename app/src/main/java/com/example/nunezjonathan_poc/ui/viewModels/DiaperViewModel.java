package com.example.nunezjonathan_poc.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiaperViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiaperViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("My Diaper Screen");
    }

    public LiveData<String> getText() { return mText; }
}
