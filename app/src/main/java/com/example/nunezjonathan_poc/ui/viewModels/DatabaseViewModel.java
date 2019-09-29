package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Sleep;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    public MutableLiveData<Long> childId = new MutableLiveData<>();
    private LiveData<List<Child>> childrenListLiveData;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        childrenListLiveData = appDatabase.childDao().queryAll();
    }

    public LiveData<List<Child>> getChildrenListLiveData() {
        return childrenListLiveData;
    }
}
