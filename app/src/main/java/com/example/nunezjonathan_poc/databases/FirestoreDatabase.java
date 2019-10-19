package com.example.nunezjonathan_poc.databases;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public abstract class FirestoreDatabase {

    public static final String COLLECTION_FAMILIES = "families";
    public static final String COLLECTION_CHILDREN = "children";
    public static final String COLLECTION_EVENT = "event";
    public static final String COLLECTION_HEALTH = "health";

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getFamilyId(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("familyData", Context.MODE_PRIVATE);
        return sharedPrefs.getString("family_id", null);
    }

    public static void addChildToDB(final Application application, final Child child, final boolean setToCurrent) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_FAMILIES)
                    .document(familyId)
                    .collection(COLLECTION_CHILDREN)
                    .document();

            child.documentId = docRef.getId();

            docRef.set(child)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (setToCurrent && application != null) {
                                SharedPreferences sharedPrefs = application.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                                sharedPrefs.edit().putString("childDocumentId", child.documentId).apply();
                                sharedPrefs.edit().putString("childName", child.name).apply();
                                sharedPrefs.edit().putString("childImageUriString", child.imageStringUri).apply();

                                Toast.makeText(application, "Successfully created Child Profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public static void addEventToDB(final Application application, Event event) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            SharedPreferences sharedPrefs = application.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            String childDocumentId = sharedPrefs.getString("childDocumentId", null);
            if (childDocumentId != null) {
                event.childDocumentId = childDocumentId;
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection(COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(COLLECTION_CHILDREN)
                        .document(event.childDocumentId)
                        .collection(COLLECTION_EVENT)
                        .document();
                event.documentId = docRef.getId();

                docRef.set(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(application, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public static void deleteChildInDB(final Application application, String childDocumentId) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            FirebaseFirestore.getInstance()
                    .collection(COLLECTION_FAMILIES)
                    .document(familyId)
                    .collection(COLLECTION_CHILDREN)
                    .document(childDocumentId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(application, "Successfully created Child Profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static void deleteEventInDB(final Application application, Event event) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            FirebaseFirestore.getInstance()
                    .collection(COLLECTION_FAMILIES)
                    .document(familyId)
                    .collection(COLLECTION_CHILDREN)
                    .document(event.childDocumentId)
                    .collection(COLLECTION_EVENT)
                    .document(event.documentId)
                    .delete();
        }
    }

    public static void addHealthToDB(final Application application, Health health) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            SharedPreferences sharedPrefs = application.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            String childDocumentId = sharedPrefs.getString("childDocumentId", null);
            if (childDocumentId != null) {
                health.childDocumentId = childDocumentId;
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection(COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(COLLECTION_CHILDREN)
                        .document(health.childDocumentId)
                        .collection(COLLECTION_HEALTH)
                        .document();

                health.documentId = docRef.getId();

                docRef.set(health)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(application, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public static void deleteHealthInDB(Application application, Health health) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            FirebaseFirestore.getInstance()
                    .collection(COLLECTION_FAMILIES)
                    .document(familyId)
                    .collection(COLLECTION_CHILDREN)
                    .document(health.childDocumentId)
                    .collection(COLLECTION_HEALTH)
                    .document(health.documentId)
                    .delete();
        }
    }

    public static void deleteChildren(Application application) {
        String familyId = getFamilyId(application);
        if (familyId != null) {
            FirebaseFirestore.getInstance().collection(COLLECTION_FAMILIES)
                    .document(familyId)
                    .collection(COLLECTION_CHILDREN)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot document :
                                        task.getResult()) {
                                    Child child = document.toObject(Child.class);
                                    //FirestoreDatabase.deleteChildInDB(String.valueOf(child._id));
                                }
                            }

                        }
                    });
        }
    }
}
