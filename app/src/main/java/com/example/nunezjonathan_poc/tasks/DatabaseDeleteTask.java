package com.example.nunezjonathan_poc.tasks;

import android.app.Application;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.Toast;

import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.models.Event;

public class DatabaseDeleteTask extends AsyncTask<Event, Void, Integer> {

    private final EventDao mDao;
    private final Application mApplication;

    public DatabaseDeleteTask(EventDao mDao, Application mApplication) {
        this.mDao = mDao;
        this.mApplication = mApplication;
    }

    @Override
    protected Integer doInBackground(Event... events) {
        return mDao.deleteEvent(events[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if (integer != -1) {
            Toast.makeText(mApplication, "Deleted Event", Toast.LENGTH_SHORT).show();
        }
    }
}
