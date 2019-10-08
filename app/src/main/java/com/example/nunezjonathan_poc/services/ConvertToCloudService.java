package com.example.nunezjonathan_poc.services;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Event;
import java.util.List;

public class ConvertToCloudService extends JobIntentService {

    private static final int JOB_ID = 100;

    Handler mHandler = new Handler();

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, ConvertToCloudService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (FirestoreDatabase.getCurrentUser() != null) {
            AppDatabase db = AppDatabase.getDatabase(this);

            ChildDao childDao = db.childDao();
            List<Child> childList = childDao.queryAllForConvert();
            for (Child child :
                    childList) {
                FirestoreDatabase.addChildToDB(child);
            }

            EventDao eventDao = db.eventDao();
            List<Event> eventList = eventDao.queryAll();
            for (Event event :
                    eventList) {
                FirestoreDatabase.addEventToDB(event);
                eventDao.deleteEvent(event);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showToast("Finished syncing to the Cloud!");
    }

    private void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ConvertToCloudService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
