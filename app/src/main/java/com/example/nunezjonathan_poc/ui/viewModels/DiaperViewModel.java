package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.DiaperRepository;

import java.util.List;

public class DiaperViewModel extends AndroidViewModel {

    private final DiaperRepository mRepository;
    private final LiveData<List<Event>> diaperList;

    public DiaperViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DiaperRepository(application);
        diaperList = mRepository.getDiaperList();
    }

    public LiveData<List<Event>> getDiaperList() {
        return diaperList;
    }

    public void insertDiaperEvent(Event event) {
        mRepository.insertDiaperEvent(event);
    }
}
