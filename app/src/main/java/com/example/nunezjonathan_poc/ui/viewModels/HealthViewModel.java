package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.repos.HealthRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class HealthViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final HealthRepository mRepository;

    public HealthViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new HealthRepository(mApplication);
    }

    public LiveData<List<Health>> getHealthList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) return mRepository.getHealthList();
        else return mRepository.getHealthRoom();
    }

    public void insertHealth(EventActivityListener listener, Health health) {
        mRepository.insertHealthData(listener, health);
    }

    public void deleteHealth(Health health) {
        mRepository.deleteHealthData(health);
    }
}
