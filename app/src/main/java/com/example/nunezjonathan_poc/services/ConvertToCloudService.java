package com.example.nunezjonathan_poc.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.daos.HealthDao;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ConvertToCloudService extends JobIntentService {

    private static final int JOB_ID = 22;

    Handler mHandler = new Handler();

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, ConvertToCloudService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String familyId = intent.getStringExtra("familyId");
        if (FirestoreDatabase.getCurrentUser() != null && familyId != null) {
            final AppDatabase db = AppDatabase.getDatabase(this);

            final ChildDao childDao = db.childDao();

            List<Child> childList = childDao.queryAllForConvert();
            for (final Child child :
                    childList) {
                final CollectionReference rootColRef = FirebaseFirestore.getInstance()
                        .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                        .document(familyId)
                        .collection(FirestoreDatabase.COLLECTION_CHILDREN);

                final DocumentReference childrenRef = rootColRef.document();

                child.documentId = childrenRef.getId();

                childrenRef.set(child);

                convertEvents(db, child, rootColRef);
                convertHealths(db, child, rootColRef);

                childDao.delete(child);
            }
        }

        SharedPreferences sharedPrefs = getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong("childId", -1).apply();
        sharedPrefs.edit().putString("childName", "Child").apply();
        sharedPrefs.edit().putString("childDocumentId", null).apply();

        sharedPrefs = getSharedPreferences("familyData", Context.MODE_PRIVATE);
        sharedPrefs.edit().putString("family_id", familyId).apply();
    }

    private void convertEvents(AppDatabase db, Child child, CollectionReference rootColRef) {
        final EventDao eventDao = db.eventDao();
        List<Event> eventList = eventDao.queryAllByChildIdForConvert(child._id);
        for (final Event event :
                eventList) {
            event.childDocumentId = child.documentId;
            DocumentReference eventRef = rootColRef
                    .document(event.childDocumentId)
                    .collection(FirestoreDatabase.COLLECTION_EVENT)
                    .document();

            event.documentId = eventRef.getId();

            eventRef.set(event);
            eventDao.deleteEvent(event);
        }
    }

    private void convertHealths(AppDatabase db, Child child, CollectionReference rootColRef) {
        final HealthDao healthDao = db.healthDao();
        List<Health> healthList = healthDao.queryAllByChildIdForConvert(child._id);
        for (final Health health :
                healthList) {
            health.childDocumentId = child.documentId;
            DocumentReference healthRef = rootColRef
                    .document(health.childDocumentId)
                    .collection(FirestoreDatabase.COLLECTION_HEALTH)
                    .document();

            health.documentId = healthRef.getId();

            healthRef.set(health);
            healthDao.deleteHealth(health);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showToast("Finished syncing to the Cloud!");
    }

    private void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ConvertToCloudService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
