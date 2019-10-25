package com.example.nunezjonathan_poc.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.services.ConvertToCloudService;
import com.example.nunezjonathan_poc.services.DeleteAllData;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SwitchPreference cloudSyncEnabled;
    private Preference linkedAccounts;
    private SwitchPreference signIn;
    private Preference signOut;
    private Preference deleteAll;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        cloudSyncEnabled = findPreference("cloudSyncEnabled");

        linkedAccounts = findPreference("linkedAccounts");
        signIn = findPreference("signInEnabled");
        signOut = findPreference("signOut");
        deleteAll = findPreference("deleteAll");

        checkIfSignInEnabled();
    }

    private void checkIfSignInEnabled() {
        boolean enableStatus = getPreferenceManager().getSharedPreferences().getBoolean("signInEnabled", false);
        cloudSyncEnabled.setEnabled(enableStatus);
        signOut.setEnabled(enableStatus);
        deleteAll.setEnabled(enableStatus);

        if (cloudSyncEnabled.isEnabled() && FirestoreDatabase.getCurrentUser() != null) {
            enableStatus = getPreferenceManager().getSharedPreferences().getBoolean("cloudSyncEnabled", false);
            linkedAccounts.setEnabled(enableStatus);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String key) {
        if (key.equals("signInEnabled")) {
            final boolean enableStatus = sharedPreferences.getBoolean(key, false);
            if (enableStatus) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Sign-In Enabled");
                builder.setMessage("The next time you open the app, you'll be asked to sign in.");
                builder.setPositiveButton("OK", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                if (FirestoreDatabase.getCurrentUser() != null) {
                    cloudSyncEnabled.setEnabled(enableStatus);
                    signOut.setEnabled(enableStatus);
                    deleteAll.setEnabled(enableStatus);
                }
            } else {
                if (getContext() != null) {
                    cloudSyncEnabled.setChecked(false);
                    cloudSyncEnabled.setEnabled(false);
                    signOut.setEnabled(false);
                    deleteAll.setEnabled(false);
                    OptionalServices.signOut((getContext()));
                }
            }
        } else if (key.equals("cloudSyncEnabled")) {
            boolean enableStatus = sharedPreferences.getBoolean(key, false);
            if (enableStatus) {
                // Cloud Services is enabled
                // Get currently signed in user
                //TODO verify this is working properly. Do full tests
                // also implement Security rules in Firebase
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Check if this user already has a User profile in the Cloud
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            // User profile already exists
                                            // Set familyId in SharedPreferences
                                            String familyId = (String) document.get("family_id");
                                            if (familyId != null && getContext() != null) {
                                                SharedPreferences sharedPrefs = getContext().getSharedPreferences("familyData", Context.MODE_PRIVATE);
                                                sharedPrefs.edit().putString("family_id", familyId).apply();
                                                sharedPrefs = getContext().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                                                sharedPrefs.edit().putString("childName", "Child").apply();
                                            }
                                        } else {
                                            // Need to create a User profile and Family
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Enable CloudSync Services");
                                            builder.setMessage("You are about to enable CloudSync Services\n\n" +
                                                    "Features:\n" +
                                                    "- All of your data will be backed up and protected in the cloud.\n" +
                                                    "- Never worry about losing data. Simply sign-in from any device and all your data will be there.\n" +
                                                    "- Invite others to become a member of your Family; they will be able to view & add entries as well.\n\n" +
                                                    "Note: Proceeding will move all of your local data to the cloud, in the process your local data will be erased.");
                                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DocumentReference docRef = FirebaseFirestore.getInstance()
                                                            .collection("families")
                                                            .document();

                                                    final String familyId = docRef.getId();

                                                    Map<String, Object> userDetails = new HashMap<>();
                                                    userDetails.put("family_id", familyId);

                                                    FirebaseFirestore.getInstance()
                                                            .collection("users")
                                                            .document(user.getUid())
                                                            .set(userDetails)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    Map<String, Object> docData = new HashMap<>();
                                                                    docData.put("owner", user.getUid());

                                                                    FirebaseFirestore.getInstance()
                                                                            .collection("families")
                                                                            .document(familyId)
                                                                            .set(docData)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Intent intent = new Intent(getActivity(), ConvertToCloudService.class);
                                                                                    intent.putExtra("familyId", familyId);
                                                                                    ConvertToCloudService.enqueueWork(getContext(), intent);
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    cloudSyncEnabled.setChecked(false);
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    }
                                }
                            });
                }
            } else {
                sharedPreferences.edit().putString("family_id", null).apply();
                if (getActivity() != null) {
                    SharedPreferences sharedPrefs = getActivity().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                    sharedPrefs.edit().putString("childName", "Child").apply();
                    sharedPrefs.edit().putLong("childId", -1).apply();
                    sharedPrefs.edit().putString("childDocumentId", null).apply();
                }
            }
            linkedAccounts.setEnabled(enableStatus);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "linkedAccounts":
                if (getActivity() != null) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_settingsFragment_to_linkedAccountsFragment);
                }
                break;
            case "signOut":
                if (getContext() != null) {
                    OptionalServices.signOut(getContext());
                }
                break;
            case "deleteAll":
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete All");
                builder.setMessage("You've requested to delete all your data. Do you wish to proceed?");
                builder.setPositiveButton("No", null);
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setTitle("Delete All");
                        builder1.setMessage("This is not reversible. Are you sure you'd like to delete all of your data?");
                        builder1.setPositiveButton("No", null);
                        builder1.setNegativeButton("Delete All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getContext() != null) {
                                    SharedPreferences sharedPrefs = getContext().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                                    sharedPrefs.edit().putLong("childId", -1).apply();
                                    sharedPrefs.edit().putString("childName", "Child").apply();
                                    if (OptionalServices.cloudSyncEnabled(getContext())) {
                                        final FirebaseUser user = FirestoreDatabase.getCurrentUser();
                                        if (user != null && getActivity() != null) {
                                            FirestoreDatabase.deleteChildren(getActivity().getApplication());
                                        }
                                    } else {
                                        Intent intent = new Intent(getActivity(), DeleteAllData.class);
                                        DeleteAllData.enqueueWork(getContext(), intent);
                                    }
                                }
                            }
                        });
                        AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

        }

//        return super.onPreferenceTreeClick(preference);
        return true;
    }
}
