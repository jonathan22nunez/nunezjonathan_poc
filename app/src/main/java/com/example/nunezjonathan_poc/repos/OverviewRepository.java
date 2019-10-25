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

public class OverviewRepository {

    private final Application mApplication;
    private final EventDao mDao;
    private MutableLiveData<List<Event>> mOverviewList = new MutableLiveData<>();
    private LiveData<List<Event>> mOverviewRoom;

    public OverviewRepository(Application application) {
        this.mApplication = application;
        mDao = AppDatabase.getDatabase(application).eventDao();
    }

    public MutableLiveData<List<Event>> getOverviewList() {
        SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        Calendar today = Calendar.getInstance();
        String todayString = CalendarUtils.toDateString(today.getTime());
        String familyId = FirestoreDatabase.getFamilyId(mApplication);
        if (familyId != null) {
            String childDocumentId = sharedPrefs.getString("childDocumentId", null);
            if (childDocumentId != null) {
                FirebaseFirestore.getInstance()
                        .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                        .document(childDocumentId)
                        .collection(FirestoreDatabase.COLLECTION_EVENT)
                        .whereGreaterThan("datetime", todayString)
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
            } else {
                mOverviewList = new MutableLiveData<>();
            }
        }

        return mOverviewList;
    }

    public LiveData<List<Event>> getOverviewRoom() {
        SharedPreferences sharedPrefs = mApplication.getSharedPreferences("currentChild", Context.MODE_PRIVATE);

        long childId = sharedPrefs.getLong("childId", -1);
        if (childId != -1) {
            mOverviewRoom = mDao.queryAllByChildId(childId);
        }

        return mOverviewRoom;
    }

    public void insertEventData(Event event) {
        new DatabaseInsertTask(null, mDao, mApplication).execute(event);
    }
}
