package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.repos.HealthRepository;

import java.util.List;

public class HealthViewModel extends AndroidViewModel {

    private final HealthRepository mRepository;
    private final LiveData<List<Health>> healthList;

    public HealthViewModel(@NonNull Application application) {
        super(application);
        mRepository = new HealthRepository(application);
        healthList = mRepository.getHealthList();
    }

    public LiveData<List<Health>> getHealthList() {
        return healthList;
    }

    public void insertHealth(Health health) {
        mRepository.insertHealthData(health);
    }
}
