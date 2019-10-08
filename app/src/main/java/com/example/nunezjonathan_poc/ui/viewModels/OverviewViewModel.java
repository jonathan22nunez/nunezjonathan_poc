package com.example.nunezjonathan_poc.ui.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.repos.OverviewRepository;
import java.util.List;

public class OverviewViewModel extends AndroidViewModel {

    private OverviewRepository mRepository;
    private LiveData<List<Event>> overviewList;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OverviewRepository(application);
        overviewList = mRepository.getOverviewList();
    }

    public LiveData<List<Event>> getOverviewList() {
        return overviewList;
    }
}
