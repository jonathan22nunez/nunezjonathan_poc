package com.example.nunezjonathan_poc.tasks;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.Toast;

import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;

public class DatabaseInsertTask extends AsyncTask<Event, Void, LongSparseArray<Event>> {

    private final EventDao mDao;
    private final Application mApplication;
    private EventActivityListener mListener;

    public DatabaseInsertTask(EventActivityListener listener, EventDao mDao, Application mApplication) {
        this.mDao = mDao;
        this.mApplication = mApplication;
        this.mListener = listener;
    }

    @Override
    protected LongSparseArray<Event> doInBackground(Event... events) {
        SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        long childId = sharedPrefs.getLong("childId", -1);
        if (childId != -1) {
            Event event = events[0];
            event.childId = childId;
            long rowId = mDao.insertEvent(event);
            LongSparseArray<Event> lsa = new LongSparseArray<>(1);
            lsa.put(rowId, event);

            return lsa;
        }

        return null;
    }

    @Override
    protected void onPostExecute(LongSparseArray<Event> lsa) {
        super.onPostExecute(lsa);

        if (lsa != null) {
            long rowId = lsa.keyAt(0);
            if (rowId != -1) {
                if (mListener != null) {
                    mListener.savedSuccessfully();
//                    Toast.makeText(mApplication, "Saved Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
