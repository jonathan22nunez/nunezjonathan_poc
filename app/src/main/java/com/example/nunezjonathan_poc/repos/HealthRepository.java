package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import com.example.nunezjonathan_poc.daos.HealthDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class HealthRepository {

    private final Application mApplication;
    private final HealthDao mHealthDao;
    private LiveData<List<Health>> mHealthList;


    public HealthRepository(Application mApplication) {
        this.mApplication = mApplication;
        AppDatabase db = AppDatabase.getDatabase(mApplication);
        mHealthDao = db.healthDao();
        long childId = mApplication.getSharedPreferences("currentChild",
                Context.MODE_PRIVATE).getLong("childId", -1);
        if (childId != -1) {
            mHealthList = mHealthDao.queryAllByChildId(childId);
        }
    }

    public LiveData<List<Health>> getHealthList() {
        return mHealthList;
    }

    public void insertHealthData(Health health) {
        new insertHealthAsyncTask(mHealthDao, mApplication).execute(health);
    }

    private static class insertHealthAsyncTask extends AsyncTask<Health, Void, LongSparseArray<Health>> {

        private HealthDao mAsyncTaskDao;
        private Application mApplication;

        public insertHealthAsyncTask(HealthDao mAsyncTaskDao, Application mApplication) {
            this.mAsyncTaskDao = mAsyncTaskDao;
            this.mApplication = mApplication;
        }

        @Override
        protected LongSparseArray<Health> doInBackground(Health... health) {
            long rowId = mAsyncTaskDao.insertHealth(health[0]);
            LongSparseArray<Health> lsa = new LongSparseArray<>(1);
            lsa.put(rowId, health[0]);

            return lsa;
        }

        @Override
        protected void onPostExecute(LongSparseArray<Health> lsa) {
            super.onPostExecute(lsa);

            long rowId = lsa.keyAt(0);
            if (rowId != -1) {
                Toast.makeText(mApplication, "Successfully saved Health Event", Toast.LENGTH_SHORT).show();

                if (OptionalServices.cloudSyncEnabled(mApplication)) {
                    FirestoreDatabase.addHealthToDB(lsa.get(rowId));
                }
            }
        }
    }
}
