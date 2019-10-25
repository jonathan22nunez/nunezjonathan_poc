package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.OverviewRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final OverviewRepository mRepository;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new OverviewRepository(mApplication);
    }

    public LiveData<List<Event>> getOverviewList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) return mRepository.getOverviewList();
        else return mRepository.getOverviewRoom();
    }
}
