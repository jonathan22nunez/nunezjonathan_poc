package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
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

public class FeedingRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private MutableLiveData<List<Event>> mFeedingList = new MutableLiveData<>();
    private LiveData<List<Event>> mFeedingRoom;

    public FeedingRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).eventDao();
        SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            String familyId = FirestoreDatabase.getFamilyId(application);
            if (familyId != null) {
                String childDocumentId = sharedPrefs.getString("childDocumentId", null);
                if (childDocumentId != null) {
                    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

                    Query nurseQuery = rootRef
                            .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                            .document(familyId)
                            .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                            .document(childDocumentId)
                            .collection(FirestoreDatabase.COLLECTION_EVENT)
                            .whereEqualTo("eventType", Event.EventType.NURSE)
                            .limit(25);
                    Query bottleQuery = rootRef
                            .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                            .document(familyId)
                            .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                            .document(childDocumentId)
                            .collection(FirestoreDatabase.COLLECTION_EVENT)
                            .whereEqualTo("eventType", Event.EventType.BOTTLE)
                            .limit(25);

                    Task nurseQueryTask = nurseQuery.get();
                    Task bottleQueryTask = bottleQuery.get();

                    Tasks.whenAllSuccess(nurseQueryTask, bottleQueryTask).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
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

                            mFeedingList.postValue(events);
                        }
                    });
                }
            }
        } else {
            long childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                mFeedingRoom = mDao.queryAllByChildIdAndType(childId, Event.EventType.NURSE, Event.EventType.BOTTLE);
            }
        }
    }

    public MutableLiveData<List<Event>> getFeedingList() {
        return mFeedingList;
    }

    public LiveData<List<Event>> getFeedingRoom() {
        return mFeedingRoom;
    }

    public void insertFeedingEvent(EventActivityListener listener, Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addEventToDB(listener, mApplication, event);
        } else {
            new DatabaseInsertTask(listener, mDao, mApplication).execute(event);
        }
    }

    public void deleteFeedingEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.deleteEventInDB(mApplication, event);
        } else {
            new DatabaseDeleteTask(mDao, mApplication).execute(event);
        }
    }
}
