package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SleepViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private long childId;

    public SleepViewModelFactory(Application mApplication, long childId) {
        this.mApplication = mApplication;
        this.childId = childId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SleepViewModel(mApplication, childId);
    }
}
