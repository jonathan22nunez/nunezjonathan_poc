package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.OverviewRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final OverviewRepository mRepository;
    private final LiveData<List<Event>> eventList;
    //private final LiveData<List<Event>> sleepList;
    //private final LiveData<List<Event>> feedingList;
    //private final LiveData<List<Event>> diaperList;

    public EventViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OverviewRepository(application);
        eventList = mRepository.getOverviewList();
        //sleepList = mRepository.getSleepList();
        //feedingList = mRepository.getFeedingList();
        //diaperList = mRepository.getDiaperList();
    }

    public LiveData<List<Event>> getEventList() {
        return eventList;
    }

//    public LiveData<List<Event>> getSleepList() {
//        return sleepList;
//    }

//    public LiveData<List<Event>> getFeedingList() {
//        return feedingList;
//    }

//    public LiveData<List<Event>> getDiaperList() {
//        return diaperList;
//    }

    public void insertEvent(Event event) {
        mRepository.insertEventData(event);
    }
}
