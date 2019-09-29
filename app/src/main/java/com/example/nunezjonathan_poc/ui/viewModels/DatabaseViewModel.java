package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.models.Child;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private LiveData<List<Child>> childrenListLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(application);
        childrenListLiveData = appDatabase.childDao().queryAll();
    }

    public LiveData<List<Child>> getChildrenListLiveData() {
        return childrenListLiveData;
    }
}
