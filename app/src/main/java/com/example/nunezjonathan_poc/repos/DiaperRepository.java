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

public class DiaperRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private LiveData<List<Event>> mDiaperList;

    public DiaperRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).eventDao();
        long childId = mApplication.getSharedPreferences("currentChild",
                Context.MODE_PRIVATE).getLong("childId", -1);
        if (childId != -1) {
            mDiaperList = mDao.queryAllByChildIdAndType(childId, Event.EventType.WET, Event.EventType.POOPY, Event.EventType.MIXED);
        }
    }

    public LiveData<List<Event>> getDiaperList() {
        return mDiaperList;
    }

    public void insertDiaperEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addEventToDB(event);
        } else {
            new EventAsyncTask(mDao, mApplication).execute(event);
        }
    }
}
