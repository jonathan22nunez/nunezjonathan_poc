package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.FeedingRepository;

import java.util.List;

public class FeedingViewModel extends AndroidViewModel {

    private final FeedingRepository mRepository;
    private final LiveData<List<Event>> feedingList;

    public FeedingViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FeedingRepository(application);
        feedingList = mRepository.getFeedingList();
    }

    public LiveData<List<Event>> getFeedingList() {
        return feedingList;
    }

    public void insertFeedingEvent(Event event) {
        mRepository.insertFeedingEvent(event);
    }
}
