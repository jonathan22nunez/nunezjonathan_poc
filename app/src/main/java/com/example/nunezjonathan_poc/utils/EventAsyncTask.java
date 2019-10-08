package com.example.nunezjonathan_poc.utils;

import android.app.Application;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.Toast;

import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;

public class EventAsyncTask extends AsyncTask<Event, Void, LongSparseArray<Event>> {

    private final EventDao mDao;
    private final Application mApplication;

    public EventAsyncTask(EventDao mDao, Application mApplication) {
        this.mDao = mDao;
        this.mApplication = mApplication;
    }

    @Override
    protected LongSparseArray<Event> doInBackground(Event... events) {
        long rowId = mDao.insertEvent(events[0]);
        LongSparseArray<Event> lsa = new LongSparseArray<>(1);
        lsa.put(rowId, events[0]);

        return lsa;
    }

    @Override
    protected void onPostExecute(LongSparseArray<Event> lsa) {
        super.onPostExecute(lsa);

        long rowId = lsa.keyAt(0);
        if (rowId != -1) {
            Toast.makeText(mApplication, "Successfully saved Event", Toast.LENGTH_SHORT).show();
        }
    }
}
