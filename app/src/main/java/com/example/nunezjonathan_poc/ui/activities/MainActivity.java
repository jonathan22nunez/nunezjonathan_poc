package com.example.nunezjonathan_poc.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.interfaces.DatabaseListener;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Sleep;
import com.example.nunezjonathan_poc.ui.fragments.SettingsFragment;
import com.example.nunezjonathan_poc.ui.viewModels.DatabaseViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatabaseListener, FeedingActivityListener, SettingsFragment.SettingsListener {

    private static final int REQUEST_CODE_SIGN_IN = 100;

    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private AppDatabase roomDB;
    private HandlerThread handlerThread;
    private Handler handler;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifySignInEnabled();

        roomDB = AppDatabase.getInstance(this);
        handlerThread = new HandlerThread("DatabaseHandler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp();
    }

    @Override
    public void manualNurseEntry() {
        navController.navigate(R.id.manualSleepFragment);
    }

    @Override
    public void manualBottleEntry() {
        navController.navigate(R.id.manualBottleFragment);
    }

    @Override
    public void inputBottleDetails() {
        navController.navigate(R.id.bottleDetailsFragment);
    }

    private void verifySignInEnabled() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPrefs.getBoolean("signInEnabled", false)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                setupUI();
            } else {
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setLogo(R.drawable.logo)
                                .setTheme(R.style.AppTheme)
                                .build(),
                        REQUEST_CODE_SIGN_IN);
            }
        } else {
            setupUI();
        }
    }

    private void setupUI() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_overview,
                R.id.navigation_sleep,
                R.id.navigation_feeding,
                R.id.navigation_diaper,
                R.id.navigation_health)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    setupUI();
                }
            } else {
                if (response != null && response.getError() != null) {
                    Log.i("TestAuthentication", String.valueOf(response.getError().getErrorCode()));

                }
            }
        }
    }

    @Override
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signed Out successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    public void createChildProfile(final Child child) {
        //handlerThread.start();
        //Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                long rowId = roomDB.childDao().insertChild(child);
                if (rowId != -1) {
                    SharedPreferences sharedPrefs = getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                    sharedPrefs.edit().putLong("childId", rowId).apply();
                    Toast.makeText(MainActivity.this,
                            "Successfully created Child Profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void saveSleepActivity(final Sleep sleep) {
        //Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                long rowId = roomDB.sleepDao().insertSleep(sleep);
                if (rowId != -1) {
                    Toast.makeText(MainActivity.this, "Successfully created Sleep activity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handlerThread.quit();
    }
}
