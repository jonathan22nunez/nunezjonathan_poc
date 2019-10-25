package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.DiaperRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class DiaperViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final DiaperRepository mRepository;
    private MutableLiveData<List<Event>> diaperList;
    private LiveData<List<Event>> diaperRoom;

    public DiaperViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new DiaperRepository(mApplication);
        diaperList = mRepository.getDiaperList();
        diaperRoom = mRepository.getDiaperRoom();
    }

    public LiveData<List<Event>> getDiaperList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            return diaperList;
        } else {
            return diaperRoom;
        }
    }

    public void insertDiaperEvent(EventActivityListener listener, Event event) {
        mRepository.insertDiaperEvent(listener, event);
    }

    public void deleteDiaper(Event event) {
        mRepository.deleteDiaperEvent(event);
    }
}
