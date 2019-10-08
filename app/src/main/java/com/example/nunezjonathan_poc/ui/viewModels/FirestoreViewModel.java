package com.example.nunezjonathan_poc.ui.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreViewModel extends ViewModel {

    private MutableLiveData<List<Event>> eventsList = new MutableLiveData<>();

    public LiveData<List<Event>> getAllEventsList(String uid, long childId) {
        FirebaseFirestore.getInstance()
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
                .collection(FirestoreDatabase.COLLECTION_EVENT)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    eventsList.setValue(events);
                }
            }
        });

        return eventsList;
    }

    public LiveData<List<Event>> getSleepList(String uid, long childId) {
        FirebaseFirestore.getInstance()
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
                .collection(FirestoreDatabase.COLLECTION_EVENT)
                .whereEqualTo("eventType", Event.EventType.SLEEP)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    List<Event> events = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Event event = document.toObject(Event.class);
                        if (event != null) {
                            events.add(event);
                        }
                    }

                    eventsList.setValue(events);
                }
            }
        });

        return eventsList;
    }

    public LiveData<List<Event>> getFeedingList(String uid, long childId) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Query nurseQuery = rootRef
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
                .collection(FirestoreDatabase.COLLECTION_EVENT)
                .whereEqualTo("eventType", Event.EventType.NURSE);
        Query bottleQuery = rootRef
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
                .collection(FirestoreDatabase.COLLECTION_EVENT)
                .whereEqualTo("eventType", Event.EventType.BOTTLE);

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

                eventsList.setValue(events);
            }
        });

        return eventsList;
    }

    public LiveData<List<Event>> getDiaperList(String uid, long childId) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Query wetQuery = rootRef
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
                .collection(FirestoreDatabase.COLLECTION_EVENT)
                .whereEqualTo("eventType", Event.EventType.WET);
        Query poopyQuery = rootRef
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
                .collection(FirestoreDatabase.COLLECTION_EVENT)
                .whereEqualTo("eventType", Event.EventType.POOPY);
        Query mixedQuery = rootRef
                .collection(FirestoreDatabase.COLLECTION_USERS)
                .document(uid)
                .collection(FirestoreDatabase.COLLECTION_CHILDREN)
                .document(String.valueOf(childId))
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

                eventsList.setValue(events);
            }
        });

        return eventsList;
    }
}
