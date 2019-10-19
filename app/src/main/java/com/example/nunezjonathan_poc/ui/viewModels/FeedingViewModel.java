package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.FeedingRepository;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class FeedingViewModel extends AndroidViewModel {

    private final Application mApplication;
    private final FeedingRepository mRepository;
    private MutableLiveData<List<Event>> feedingList;
    private LiveData<List<Event>> feedingRoom;

    public FeedingViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        mRepository = new FeedingRepository(application);
        feedingList = mRepository.getFeedingList();
        feedingRoom = mRepository.getFeedingRoom();
    }

    public LiveData<List<Event>> getFeedingList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            return feedingList;
        } else {
            return feedingRoom;
        }
    }

    public void insertFeedingEvent(Event event) {
        mRepository.insertFeedingEvent(event);
    }

    public void deleteFeeding(Event event) {
        mRepository.deleteFeedingEvent(event);
    }
}
