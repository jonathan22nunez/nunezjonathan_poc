package com.example.nunezjonathan_poc.repos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Member;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembersRepository {

    private final Application mApplication;
    private MutableLiveData<List<Member>> membersList = new MutableLiveData<>();

    public MembersRepository(Application application) {
        this.mApplication = application;
        if (OptionalServices.cloudSyncEnabled(application)) {
            String familyId = FirestoreDatabase.getFamilyId(application);
            if (familyId != null) {
                FirebaseFirestore.getInstance()
                        .collection(FirestoreDatabase.COLLECTION_FAMILIES)
                        .document(familyId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> documentMap = document.getData();
                                        if (documentMap != null) {
                                            if (documentMap.get("members") instanceof Map) {
                                                Map members = (HashMap) documentMap.get("members");
                                                if (members != null) {
                                                    List<Member> memberTempList = new ArrayList<>();

                                                    for (Object member :
                                                            members.keySet()) {
                                                        if (member instanceof String) {
                                                            Member retrievedMember = new Member();
                                                            retrievedMember.id = (String) member;
                                                            Map memberDetails = (Map) members.get(member);
                                                            if (memberDetails != null) {
                                                                retrievedMember.email = (String) memberDetails.get("email");
                                                                retrievedMember.name = (String) memberDetails.get("name");
                                                            }

                                                            memberTempList.add(retrievedMember);
                                                        }
                                                    }

                                                    membersList.postValue(memberTempList);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
            }
        }
    }

    public MutableLiveData<List<Member>> getMembersList() {
        return membersList;
    }

    public void removeMember(final Member member) {
        String familyId = FirestoreDatabase.getFamilyId(mApplication);
        if (familyId != null) {
            final DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("families")
                    .document(familyId);

            docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> documentMap = document.getData();
                                    if (documentMap != null) {
                                        if (documentMap.get("members") instanceof Map) {
                                            Map members = (HashMap) documentMap.get("members");
                                            if (members != null) {
                                                members.remove(member.id);

                                                docRef.update("members", members);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
    }
}
