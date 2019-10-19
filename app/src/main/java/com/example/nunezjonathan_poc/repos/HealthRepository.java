package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.daos.HealthDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HealthRepository {

    private final Application mApplication;
    private final HealthDao mDao;
    private MutableLiveData<List<Health>> mHealthList = new MutableLiveData<>();
    private LiveData<List<Health>> mHealthRoom;


    public HealthRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).healthDao();
        SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            String familyId = FirestoreDatabase.getFamilyId(application);
            if (familyId != null) {
            String childDocumentId = sharedPrefs.getString("childDocumentId", null);
            if (childDocumentId != null) {
                FirebaseFirestore.getInstance()
                        .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                        .document(childDocumentId)
                        .collection(FirestoreDatabase.COLLECTION_HEALTH)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e == null && queryDocumentSnapshots != null) {
                                    List<Health> healthList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document :
                                            queryDocumentSnapshots) {
                                        Health health = document.toObject(Health.class);
                                        healthList.add(health);
                                    }

                                    mHealthList.setValue(healthList);
                                }
                            }
                        });
            }
            }
        } else {
            long childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                mHealthRoom = mDao.queryAllByChildId(childId);
            }
        }
    }

    public LiveData<List<Health>> getHealthList() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            return mHealthList;
        } else {
            return mHealthRoom;
        }
    }

//    public LiveData<List<Health>> getHealthRoom() {
//        return mHealthRoom;
//    }

    public void insertHealthData(Health health) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addHealthToDB(mApplication, health);
        } else {
            new insertHealthAsyncTask(mDao, mApplication).execute(health);
        }
    }

    public void deleteHealthData(Health health) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.deleteHealthInDB(mApplication, health);
        } else {
            new deleteAsyncTask(mDao, mApplication).execute(health);
        }
    }

    private static class insertHealthAsyncTask extends AsyncTask<Health, Void, LongSparseArray<Health>> {

        private HealthDao mAsyncTaskDao;
        private Application mApplication;

        public insertHealthAsyncTask(HealthDao mAsyncTaskDao, Application mApplication) {
            this.mAsyncTaskDao = mAsyncTaskDao;
            this.mApplication = mApplication;
        }

        @Override
        protected LongSparseArray<Health> doInBackground(Health... healths) {
            SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            long childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                Health health = healths[0];
                health.childId = childId;
                long rowId = mAsyncTaskDao.insertHealth(health);
                LongSparseArray<Health> lsa = new LongSparseArray<>(1);
                lsa.put(rowId, health);

                return lsa;
            }

            return null;
        }

        @Override
        protected void onPostExecute(LongSparseArray<Health> lsa) {
            super.onPostExecute(lsa);

            long rowId = lsa.keyAt(0);
            if (rowId != -1) {
                Toast.makeText(mApplication, "Successfully saved Health Event", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Health, Void, Integer> {

        private final HealthDao mAsyncTaskDao;
        private final Application mApplication;

        deleteAsyncTask(HealthDao mAsyncTaskDao, Application mApplication) {
            this.mAsyncTaskDao = mAsyncTaskDao;
            this.mApplication = mApplication;
        }

        @Override
        protected Integer doInBackground(Health... health) {
            return mAsyncTaskDao.deleteHealth(health[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer > 0) {
                Toast.makeText(mApplication, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
