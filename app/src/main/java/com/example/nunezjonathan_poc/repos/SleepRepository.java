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

public class SleepRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private LiveData<List<Event>> mSleepList;

    public SleepRepository(Application application) {
        mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).eventDao();
        long childId = mApplication.getSharedPreferences("currentChild",
                Context.MODE_PRIVATE).getLong("childId", -1);
        if (childId != -1) {
            mSleepList = mDao.queryAllByChildIdAndType(childId, Event.EventType.SLEEP);
        }
    }

    public LiveData<List<Event>> getSleepList() {
        return mSleepList;
    }

    public void insertSleepEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addEventToDB(event);
        } else {
            new EventAsyncTask(mDao, mApplication).execute(event);
        }
    }

}
