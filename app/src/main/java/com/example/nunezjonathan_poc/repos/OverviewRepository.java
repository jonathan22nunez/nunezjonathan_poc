package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.utils.EventAsyncTask;

import java.util.List;

public class OverviewRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private LiveData<List<Event>> mOverviewList;

    public OverviewRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(application).eventDao();
        long childId = application.getSharedPreferences("currentChild",
                Context.MODE_PRIVATE).getLong("childId", -1);
        if (childId != -1) {
            mOverviewList = mDao.queryAllByChildId(childId);
        }
    }

    public LiveData<List<Event>> getOverviewList() {
        return mOverviewList;
    }

    public void insertEventData(Event event) {
        new EventAsyncTask(mDao, mApplication).execute(event);
    }
}
