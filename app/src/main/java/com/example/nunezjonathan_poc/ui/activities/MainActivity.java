package com.example.nunezjonathan_poc.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.databases.AppDatabase;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FeedingActivityListener {

    private static final int REQUEST_CODE_SIGN_IN = 100;

    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private AppDatabase roomDB;
    private FirebaseUser user;
    private HandlerThread handlerThread;
    private Handler handler;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isSignInEnabled();
        setupUI();

        roomDB = AppDatabase.getDatabase(this);
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
    public void viewLog() {
        navController.navigate(R.id.feedingLogListFragment);
    }

    @Override
    public void manualNurseEntry() {
        navController.navigate(R.id.manualNurseFragment);
    }

    @Override
    public void manualBottleEntry() {
        navController.navigate(R.id.manualBottleFragment);
    }

    @Override
    public void inputBottleDetails(Bundle bundle) {
        navController.navigate(R.id.bottleDetailsFragment, bundle);
    }

    private void isSignInEnabled() {
        if (OptionalServices.signInEnabled(this)) {
            user = FirestoreDatabase.getCurrentUser();
            if (user == null) {
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setLogo(R.drawable.logo)
                                .setTheme(R.style.AppTheme)
                                .build(),
                        REQUEST_CODE_SIGN_IN);
            }
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
                user = FirestoreDatabase.getCurrentUser();
            } else {
                if (response != null && response.getError() != null) {
                    Log.i("TestAuthentication", String.valueOf(response.getError().getErrorCode()));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handlerThread.quit();
    }
}
