package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class ChildRepository {

    private final Application mApplication;
    private final ChildDao mChildDao;
    private final LiveData<List<Child>> mChildren;

    public ChildRepository(Application application) {
        mApplication = application;
        mChildDao = AppDatabase.getDatabase(mApplication).childDao();
        mChildren = mChildDao.queryAll();
    }

    public LiveData<List<Child>> getChildren() {
        return mChildren;
    }

    public void insertChildData(Child child) {
        new insertAsyncTask(mApplication, mChildDao).execute(child);
    }

    public void deleteChildData(Child child) {
        new deleteAsyncTask(mApplication, mChildDao).execute(child);
    }

    private static class insertAsyncTask extends AsyncTask<Child, Void, LongSparseArray<Child>> {

        private final ChildDao mAsyncTaskDao;
        private final Application mApplication;

        insertAsyncTask(Application application, ChildDao dao) {
            mApplication = application;
            mAsyncTaskDao = dao;
        }

        @Override
        protected LongSparseArray<Child> doInBackground(Child... children) {
            long rowId = mAsyncTaskDao.insertChild(children[0]);
            LongSparseArray<Child> lsa = new LongSparseArray<>(1);
            lsa.put(rowId, children[0]);

            return lsa;
        }

        @Override
        protected void onPostExecute(LongSparseArray<Child> lsa) {
            super.onPostExecute(lsa);

            long rowId = lsa.keyAt(0);
            if (rowId != -1) {
                SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                sharedPrefs.edit().putLong("childId", rowId).apply();

                Toast.makeText(mApplication, "Successfully created Child Profile", Toast.LENGTH_SHORT).show();

                if (OptionalServices.cloudSyncEnabled(mApplication)) {
                    Child child = lsa.get(rowId);
                    child._id = (int) rowId;
                    FirestoreDatabase.addChildToDB(child);

                }
            }
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Child, Void, Integer> {

        private final ChildDao mAsyncTaskDao;
        private final Application mApplication;

        deleteAsyncTask(Application application, ChildDao dao) {
            mApplication = application;
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(Child... children) {
            return mAsyncTaskDao.delete(children);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer > 0) {
                Toast.makeText(mApplication, "Successfully deleted Child Profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
