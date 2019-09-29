package com.example.nunezjonathan_poc.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.nunezjonathan_poc.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public interface SettingsListener {
        void signOut();
    }

    private SettingsListener mListener;
    SwitchPreference cloudSyncEnabled;
    Preference syncDevices, backupData, restoreData, signOut, deleteAll;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SettingsListener) {
            mListener = (SettingsListener) context;
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        cloudSyncEnabled = findPreference("cloudSyncEnabled");
        syncDevices = findPreference("syncDevices");
        backupData = findPreference("backupData");
        restoreData = findPreference("restoreData");
        signOut = findPreference("signOut");
        deleteAll = findPreference("deleteAll");

        checkIfSignInEnabled();
    }

    private void checkIfSignInEnabled() {
        boolean enableStatus = getPreferenceManager().getSharedPreferences().getBoolean("signInEnabled", false);
        cloudSyncEnabled.setEnabled(enableStatus);
        signOut.setEnabled(enableStatus);
        deleteAll.setEnabled(enableStatus);

        if (cloudSyncEnabled.isEnabled()) {
            enableStatus = getPreferenceManager().getSharedPreferences().getBoolean("cloudSyncEnabled", false);
            syncDevices.setEnabled(enableStatus);
            backupData.setEnabled(enableStatus);
            restoreData.setEnabled(enableStatus);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("signInEnabled")) {
            final boolean enableStatus = sharedPreferences.getBoolean(key, false);
            if (enableStatus) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Sign-In Enabled");
                builder.setMessage("The next time you open the app, you'll be asked to sign in.");
                builder.setPositiveButton("OK", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            cloudSyncEnabled.setEnabled(enableStatus);
            signOut.setEnabled(enableStatus);
            deleteAll.setEnabled(enableStatus);
        } else if (key.equals("cloudSyncEnabled")) {
            boolean enableStatus = sharedPreferences.getBoolean(key, false);
            syncDevices.setEnabled(enableStatus);
            backupData.setEnabled(enableStatus);
            restoreData.setEnabled(enableStatus);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case "syncDevices":
                Log.i("SettingsPreference", "Sync Devices Clicked");
                break;
            case "backupData":
                Log.i("SettingsPreference", "Backup Data Clicked");
                break;
            case "restoreData":
                Log.i("SettingsPreference", "Restore Data Clicked");
                break;
            case "signOut":
                mListener.signOut();
                break;
            case "deleteAll":
                Log.i("SettingsPreference", "Delete All Clicked");
                break;

        }
        if (preference == syncDevices) {
        }
        return super.onPreferenceTreeClick(preference);
    }
}
