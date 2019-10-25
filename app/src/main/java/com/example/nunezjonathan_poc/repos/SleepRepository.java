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
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.tasks.DatabaseDeleteTask;
import com.example.nunezjonathan_poc.tasks.DatabaseInsertTask;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.nunezjonathan_poc.databases.FirestoreDatabase.getFamilyId;

public class SleepRepository {

    private static final int SLEEP = Event.EventType.SLEEP;

    private final Application mApplication;
    private EventDao mDao;
    private MutableLiveData<List<Event>> mSleepList = new MutableLiveData<>();
    private LiveData<List<Event>> mSleepRoom;
    private SharedPreferences sharedPrefs;
    private long childId = -1;
    private String childDocumentId = null;

    public SleepRepository(Application application) {
        mApplication = application;
        mDao = AppDatabase.getDatabase(mApplication).eventDao();
        sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            String familyId = getFamilyId(application);
            if (familyId != null) {
            childDocumentId = sharedPrefs.getString("childDocumentId", null);
            if (childDocumentId != null) {
                FirebaseFirestore.getInstance()
                        .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                        .document(childDocumentId)
                        .collection(FirestoreDatabase.COLLECTION_EVENT)
                        .whereEqualTo("eventType", SLEEP)
                        .limit(25)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    List<Event> sleepEvents = new ArrayList<>();
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Event event = document.toObject(Event.class);
                                        if (event != null) {
                                            sleepEvents.add(event);
                                        }
                                    }

                                    mSleepList.postValue(sleepEvents);
                                }
                            }
                        });
            }
            }
        } else {
            childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                mSleepRoom = mDao.queryAllByChildIdAndType(childId, SLEEP);
            }
        }
    }

    public MutableLiveData<List<Event>> getSleepList() {
        return mSleepList;
    }

    public LiveData<List<Event>> getSleepRoom() {
        return mSleepRoom;
    }

    public void insertSleepEvent(EventActivityListener listener, Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.addEventToDB(listener, mApplication, event);
        } else {
            new DatabaseInsertTask(listener, mDao, mApplication).execute(event);
        }
    }

    public void deleteSleepEvent(Event event) {
        if (OptionalServices.cloudSyncEnabled(mApplication)) {
            FirestoreDatabase.deleteEventInDB(mApplication, event);
        } else {
            new DatabaseDeleteTask(mDao, mApplication).execute(event);
        }
    }

}
