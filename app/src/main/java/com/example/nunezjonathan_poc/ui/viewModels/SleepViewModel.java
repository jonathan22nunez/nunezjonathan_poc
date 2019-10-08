package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.SleepRepository;

import java.util.List;

public class SleepViewModel extends AndroidViewModel {

    private final SleepRepository mRepository;
    private final LiveData<List<Event>> sleepList;

    public SleepViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SleepRepository(application);
        sleepList = mRepository.getSleepList();
    }

    public LiveData<List<Event>> getSleepList() {
        return sleepList;
    }

    public void insertSleep(Event event) {
        mRepository.insertSleepEvent(event);
    }
}
