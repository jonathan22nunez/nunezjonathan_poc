package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.models.Sleep;

import java.util.List;

public class SleepViewModel extends ViewModel {

    private AppDatabase appDatabase;
    private LiveData<List<Sleep>> sleepListLiveData;


    public SleepViewModel(Application application, long childId) {
        appDatabase = AppDatabase.getInstance(application);
        sleepListLiveData = appDatabase.sleepDao().queryAllByChildId(childId);
    }

    public LiveData<List<Sleep>> getSleepListLiveData() {
        return sleepListLiveData;
    }
}
