package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.tasks.DatabaseInsertTask;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OverviewRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private MutableLiveData<List<Event>> mOverviewList = new MutableLiveData<>();
    private LiveData<List<Event>> mOverviewRoom;

    public OverviewRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(application).eventDao();
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
                        .collection(FirestoreDatabase.COLLECTION_EVENT)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    List<Event> events = new ArrayList<>();
                                    for (DocumentSnapshot document :
                                            task.getResult()) {
                                        Event event = document.toObject(Event.class);
                                        if (event != null) {
                                            events.add(event);
                                        }
                                    }

                                    mOverviewList.postValue(events);
                                }
                            }
                        });
            }
            }
        } else {
            long childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                mOverviewRoom = mDao.queryAllByChildId(childId);
            }
        }
    }

    public MutableLiveData<List<Event>> getOverviewList() {
        return mOverviewList;
    }

    public LiveData<List<Event>> getOverviewRoom() {
        return mOverviewRoom;
    }

    public void insertEventData(Event event) {
        new DatabaseInsertTask(mDao, mApplication).execute(event);
    }
}
