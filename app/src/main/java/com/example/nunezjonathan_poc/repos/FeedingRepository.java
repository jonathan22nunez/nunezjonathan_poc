package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.utils.EventAsyncTask;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class FeedingRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private LiveData<List<Event>> mFeedingList;

    public FeedingRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).eventDao();
        long childId = mApplication.getSharedPreferences("currentChild",
                Context.MODE_PRIVATE).getLong("childId", -1);
        if (childId != -1) {
            mFeedingList = mDao.queryAllByChildIdAndType(childId, Event.EventType.NURSE, Event.EventType.BOTTLE);
        }
    }

    public LiveData<List<Event>> getFeedingList() {
        return mFeedingList;
    }

    public void insertFeedingEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addEventToDB(event);
        } else {
            new EventAsyncTask(mDao, mApplication).execute(event);
        }    }
}
