package com.example.nunezjonathan_poc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nunezjonathan_poc.ui.activities.LaunchActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class OptionalServices {

    public static boolean signInEnabled(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean("signInEnabled", false);
    }

    public static void signOut(final Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences("familyData", Context.MODE_PRIVATE);
        sharedPrefs.edit().putString("family_id", null).apply();
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Signed Out successfully", Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    }
                });
    }

    public static boolean cloudSyncEnabled(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean("cloudSyncEnabled", false);
    }
}
