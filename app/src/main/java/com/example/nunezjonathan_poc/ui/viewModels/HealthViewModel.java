package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.repos.HealthRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class HealthViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final HealthRepository mRepository;
    private LiveData<List<Health>> healthList;
//    private LiveData<List<Health>> healthRoom;

    public HealthViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new HealthRepository(mApplication);
        healthList = mRepository.getHealthList();
    }

    public LiveData<List<Health>> getHealthList() {
        return healthList;
    }

    public void insertHealth(Health health) {
        mRepository.insertHealthData(health);
    }

    public void deleteHealth(Health health) {
        mRepository.deleteHealthData(health);
    }
}
