package com.example.nunezjonathan_poc.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.models.Child;

import java.util.List;

public class DeleteAllData extends JobIntentService {

    private static final int JOB_ID = 200;

    Handler mHandler = new Handler();

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, DeleteAllData.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppDatabase db = AppDatabase.getDatabase(this);

        ChildDao childDao = db.childDao();
        List<Child> childList = childDao.queryAllForConvert();
        for (Child child :
                childList) {
            childDao.delete(child);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showToast("Everything has been deleted.");
    }

    private void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeleteAllData.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
