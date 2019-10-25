package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.SleepRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class SleepViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final SleepRepository mRepository;
    private MutableLiveData<List<Event>> sleepList;
    private LiveData<List<Event>> sleepRoom;

    public SleepViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new SleepRepository(mApplication);
        sleepList = mRepository.getSleepList();
        sleepRoom = mRepository.getSleepRoom();
    }

    public LiveData<List<Event>> getSleepList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            return sleepList;
        } else {
            return sleepRoom;
        }
    }

    public void insertSleep(EventActivityListener listener, Event event) {
        mRepository.insertSleepEvent(listener, event);
    }

    public void deleteSleep(Event event) {
        mRepository.deleteSleepEvent(event);
    }
}
