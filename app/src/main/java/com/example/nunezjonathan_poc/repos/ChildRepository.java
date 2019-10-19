package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChildRepository {

    private final Application mApplication;
    private ChildDao mChildDao;
    private MutableLiveData<List<Child>> mChildren = new MutableLiveData<>();
    private LiveData<List<Child>> mChildrenRoom;

    public ChildRepository(Application application) {
        mApplication = application;
        mChildDao = AppDatabase.getDatabase(mApplication).childDao();
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            String familyId = FirestoreDatabase.getFamilyId(application);
            if (familyId != null) {
                FirebaseFirestore.getInstance()
                        .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e == null && queryDocumentSnapshots != null) {
                                    List<Child> children = new ArrayList<>();
                                    for (QueryDocumentSnapshot document :
                                            queryDocumentSnapshots) {
                                        Child child = document.toObject(Child.class);
                                        children.add(child);
                                    }

                                    mChildren.postValue(children);
                                }
                            }
                        });
            }
        } else {
            mChildrenRoom = mChildDao.queryAll();
        }
    }

    public LiveData<List<Child>> getChildren() {
        if (OptionalServices.cloudSyncEnabled(mApplication)) return mChildren;
        else return mChildrenRoom;
    }

    public void insertChildData(Child child) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addChildToDB(mApplication, child, true);
        } else {
            new insertAsyncTask(mApplication, mChildDao).execute(child);
        }
    }

    public void deleteChildData(Child child) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.deleteChildInDB(mApplication, child.documentId);
        } else {
            new deleteAsyncTask(mApplication, mChildDao).execute(child);
        }

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
                sharedPrefs.edit().putString("childName", lsa.get(rowId).name).apply();
                sharedPrefs.edit().putString("childImageUriString", lsa.get(rowId).imageStringUri).apply();

                Toast.makeText(mApplication, "Successfully created Child Profile", Toast.LENGTH_SHORT).show();
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
                SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                sharedPrefs.edit().putLong("childId", -1).apply();
                sharedPrefs.edit().putString("childName", "Child").apply();

                Toast.makeText(mApplication, "Successfully deleted Child Profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
