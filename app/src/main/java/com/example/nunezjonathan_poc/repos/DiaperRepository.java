package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.tasks.DatabaseDeleteTask;
import com.example.nunezjonathan_poc.tasks.DatabaseInsertTask;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiaperRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private MutableLiveData<List<Event>> mDiaperList = new MutableLiveData<>();
    private LiveData<List<Event>> mDiaperRoom;

    public DiaperRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).eventDao();
        SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            String familyId = FirestoreDatabase.getFamilyId(application);
            if (familyId != null) {
                String childDocumentId = sharedPrefs.getString("childDocumentId", null);
                if (childDocumentId != null) {
                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

                    Query wetQuery = rootRef
                            .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                            .document(familyId)
                            .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                            .document(String.valueOf(childDocumentId))
                            .collection(FirestoreDatabase.COLLECTION_EVENT)
                            .whereEqualTo("eventType", Event.EventType.WET);
                    Query poopyQuery = rootRef
                            .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                            .document(familyId)
                            .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                            .document(String.valueOf(childDocumentId))
                            .collection(FirestoreDatabase.COLLECTION_EVENT)
                            .whereEqualTo("eventType", Event.EventType.POOPY);
                    Query mixedQuery = rootRef
                            .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                            .document(familyId)
                            .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                            .document(String.valueOf(childDocumentId))
                            .collection(FirestoreDatabase.COLLECTION_EVENT)
                            .whereEqualTo("eventType", Event.EventType.MIXED);

                    Task wetQueryTask = wetQuery.get();
                    Task poopyQueryTask = poopyQuery.get();
                    Task mixedQueryTask = mixedQuery.get();

                    Tasks.whenAllSuccess(wetQueryTask, poopyQueryTask, mixedQueryTask).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> objects) {
                            List<Event> events = new ArrayList<>();
                            for (DocumentSnapshot document :
                                    (QuerySnapshot) objects.get(0)) {
                                Event event = document.toObject(Event.class);
                                if (event != null) {
                                    events.add(event);
                                }
                            }

                            for (DocumentSnapshot document :
                                    (QuerySnapshot) objects.get(1)) {
                                Event event = document.toObject(Event.class);
                                if (event != null) {
                                    events.add(event);
                                }
                            }

                            for (DocumentSnapshot document :
                                    (QuerySnapshot) objects.get(2)) {
                                Event event = document.toObject(Event.class);
                                if (event != null) {
                                    events.add(event);
                                }
                            }

                            mDiaperList.postValue(events);
                        }
                    });
                }
            }
        } else {
            long childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                mDiaperRoom = mDao.queryAllByChildIdAndType(childId, Event.EventType.WET, Event.EventType.POOPY, Event.EventType.MIXED);
            }
        }
    }

    public MutableLiveData<List<Event>> getDiaperList() {
        return mDiaperList;
    }

    public LiveData<List<Event>> getDiaperRoom() {
        return mDiaperRoom;
    }


    public void insertDiaperEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addEventToDB(mApplication, event);
        } else {
            new DatabaseInsertTask(mDao, mApplication).execute(event);
        }
    }

    public void deleteDiaperEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.deleteEventInDB(mApplication, event);
        } else {
            new DatabaseDeleteTask(mDao, mApplication).execute(event);
        }
    }
}
