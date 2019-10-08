package com.example.nunezjonathan_poc.databases;

import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirestoreDatabase {

    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CHILDREN = "children";
    public static final String COLLECTION_EVENT = "event";
    private static final String COLLECTION_HEALTH = "health";

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void addChildToDB(Child child) {
        FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(getCurrentUser().getUid())
                .collection(COLLECTION_CHILDREN)
                .document(String.valueOf(child._id))
                .set(child);
    }

    public static void addEventToDB(Event event) {
        FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(getCurrentUser().getUid())
                .collection(COLLECTION_CHILDREN)
                .document(String.valueOf(event.childId))
                .collection(COLLECTION_EVENT)
                .add(event);
    }

    public static void addHealthToDB(Health health) {
        FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(getCurrentUser().getUid())
                .collection(COLLECTION_CHILDREN)
                .document(String.valueOf(health.childId))
                .collection(COLLECTION_HEALTH)
                .add(health);
    }
}
