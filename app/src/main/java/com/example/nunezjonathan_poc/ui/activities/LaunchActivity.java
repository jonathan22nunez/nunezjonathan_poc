package com.example.nunezjonathan_poc.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 100;
    private static final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private static boolean linkWithFamily = false;

    private Button loginButton;
    private Button linkButton;

    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        linkButton = findViewById(R.id.button_link);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkWithFamily();
            }
        });

        if (OptionalServices.signInEnabled(this)) {
            loginButton.setVisibility(View.VISIBLE);
            linkButton.setVisibility(View.VISIBLE);
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                beginApp();
            }
        } else {
            beginApp();
        }
    }

    private void signIn() {
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .setTheme(R.style.AppTheme)
                        .build(),
                REQUEST_CODE_SIGN_IN);
    }

    private void linkWithFamily() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
        builder.setTitle("Copy and paste the code, you received, below:");
        final EditText codeInput = new EditText(LaunchActivity.this);
        builder.setView(codeInput);
        builder.setPositiveButton("Continue to Sign-Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (codeInput.getText().toString().isEmpty()) {
                    //Whoops
                    Toast.makeText(LaunchActivity.this, "Do not leave 'Code' blank", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPrefs = getSharedPreferences("familyData", Context.MODE_PRIVATE);
                    sharedPrefs.edit().putString("family_id", codeInput.getText().toString()).apply();

                    // Enable Cloud Services
                    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.this);
                    sharedPrefs.edit().putBoolean("cloudSyncEnabled", true).apply();

                    // Set linkWithFamily to true
                    linkWithFamily = true;
                    // Start signIn()
                    signIn();


                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                user = FirestoreDatabase.getCurrentUser();

                // Signed in successfully
                // Is CloudSyncEnabled?
                if (OptionalServices.cloudSyncEnabled(this)) {
                    // Cloud Services are enabled
                    // Are we linking to an existing Family?
                    if (linkWithFamily) {
                        // We are linking to an existing Family
                        // Get the FamilyId from SharedPreferences
                        SharedPreferences sharedPrefs = getSharedPreferences("familyData", Context.MODE_PRIVATE);
                        final String familyId = sharedPrefs.getString("family_id", null);
                        if (familyId != null) {
                            // Is the FamilyId code good?
                            // Test if code links to a database that exists
                            final DocumentReference docRef = FirebaseFirestore.getInstance()
                                    .collection("families")
                                    .document(familyId);

                            docRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                // No error accessing Firebase
                                                final DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    // Database exists, this is a valid code
                                                    // Create User profile using FamilyId
                                                    Map<String, Object> userDetails = new HashMap<>();
                                                    userDetails.put("family_id", familyId);

                                                    // Create User profile
                                                    FirebaseFirestore.getInstance()
                                                            .collection("users")
                                                            .document(user.getUid())
                                                            .set(userDetails)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Successfully created User profile

                                                                    // Update the family document
                                                                    Map<String, Object> documentMap = document.getData();
                                                                    if (documentMap != null) {
                                                                        Map<String, Object> memberDetails = new HashMap<>();
                                                                        if (user.getDisplayName() != null) memberDetails.put("name", user.getDisplayName());
                                                                        if (user.getEmail() != null) memberDetails.put("email", user.getEmail());
                                                                        memberDetails.put("role", "member");

                                                                        Map<String, Object> members = (Map<String, Object>) documentMap.get("members");
                                                                        if (members != null) {

                                                                            members.put(user.getUid(), memberDetails);

                                                                            docRef.update("members", members);
                                                                        } else {
                                                                            members = new HashMap<>();
                                                                            members.put(user.getUid(), memberDetails);
                                                                            Map<String, Object> docData = new HashMap<>();
                                                                            docData.put("members", members);
                                                                            docRef.update(docData);
                                                                        }
                                                                    }

                                                                    beginApp();
                                                                }
                                                            });
                                                } else {
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LaunchActivity.this);
                                                    builder1.setTitle("Uh-Oh");
                                                    builder1.setMessage("Looks like the code you provided didn't work.\n" +
                                                            "Check your code, and try again.");
                                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            AuthUI.getInstance()
                                                                    .signOut(LaunchActivity.this);
                                                        }
                                                    });
                                                    AlertDialog alertDialog = builder1.create();
                                                    alertDialog.show();
                                                }
                                            } else {
                                                Log.i("TestFirebase", "Error getting firebase");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }
                    } else {
                        // Does the user have a User profile in the cloud? (answer should always be 'yes'
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            // No error accessing Firebase
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                // The user has an existing User profile in the cloud (expected)
                                                // Get the user's 'familyId' and save to SharedPreferences
                                                SharedPreferences sharedPreferences = getSharedPreferences("familyData", Context.MODE_PRIVATE);
                                                sharedPreferences.edit().putString("familyId", (String) document.get("family_id")).apply();

                                                beginApp();
                                            }
                                            //TODO later on implement else{} to handle no data retrieve for user
                                        }
                                    }
                                });
                    }
                } else {
                    // Cloud Services are disabled

                    beginApp();
                }
            } else {
                if (response != null && response.getError() != null) {
                    Log.i("TestAuthentication", String.valueOf(response.getError().getErrorCode()));
                }
            }
        }
    }

    private void beginApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
