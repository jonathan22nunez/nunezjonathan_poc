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
    private MutableLiveData<List<Event>> overviewList;
    private LiveData<List<Event>> overviewRoom;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        OverviewRepository mRepository = new OverviewRepository(mApplication);
        overviewList = mRepository.getOverviewList();
        overviewRoom = mRepository.getOverviewRoom();
    }

    public LiveData<List<Event>> getOverviewList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) return overviewList;
        else return overviewRoom;
    }
}
