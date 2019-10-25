package com.example.nunezjonathan_poc.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.services.SleepTimerService;
import com.example.nunezjonathan_poc.ui.viewModels.SleepViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.Calendar;

import cdflynn.android.library.checkview.CheckView;

public class SleepFragment extends Fragment implements EventActivityListener {

    private SleepViewModel sleepViewModel;
    private TextView timerLabel, manualEntryLabel;
    private Button timerButton, saveButton;
    private CheckView checkView;
    private long millis = 0;
    private String childName;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            millis = intent.getLongExtra(SleepTimerService.EXTRA_TIME_MILLIS, 0);

            timerLabel.setText(TimeUtils.timerHMS(millis));
        }
    };

    private final View.OnClickListener timerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), SleepTimerService.class);
                if (SleepTimerService.isRunning) {
                    getActivity().stopService(intent);
                    timerButton.setText(getString(R.string.start));
                    manualEntryLabel.setVisibility(View.GONE);
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    intent.putExtra(SleepTimerService.EXTRA_TIME_MILLIS, millis);
                    ContextCompat.startForegroundService(getActivity(), intent);
                    timerButton.setText(R.string.stop);
                    saveButton.setVisibility(View.GONE);
                    manualEntryLabel.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private final View.OnLongClickListener timerButtonLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            resetUI();
            return true;
        }
    };

    private final View.OnClickListener manualButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_manualSleepFragment);
            }
        }
    };

    private final View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Event sleepEvent = new Event(
                        Event.EventType.SLEEP,
                        CalendarUtils.toDatetimeString(SleepTimerService.datetime.getTime()),
                        millis,
                        -1, -1, -1, -1,
                        Event.Color.NONE,
                        Event.Hardness.NONE);
                sleepViewModel.insertSleep(SleepFragment.this, sleepEvent);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sleepViewModel = ViewModelProviders.of(this).get(SleepViewModel.class);
        if (getActivity() != null) {
            IntentFilter filter = new IntentFilter(SleepTimerService.ACTION_UPDATE_TIMER);
            getActivity().registerReceiver(receiver, filter);
        }

        return inflater.inflate(R.layout.fragment_sleep, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.activities_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getContext() != null) {
            SharedPreferences sharedPrefs = getContext().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            String childName = sharedPrefs.getString("childName", null);
            if (childName != null) {
                this.childName = childName;
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (childName != null) {
            menu.findItem(R.id.menu_item_children).setTitle(childName);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerLabel = view.findViewById(R.id.textView_sleep_timer);
        timerButton = view.findViewById(R.id.button_start_stop_sleep_timer);
        if (SleepTimerService.isRunning) {
            timerButton.setText(getString(R.string.stop));
        }
        manualEntryLabel = view.findViewById(R.id.textView_manual_sleep_entry);
        saveButton = view.findViewById(R.id.button_save_sleep);

        timerButton.setOnClickListener(timerButtonClickListener);
        timerButton.setOnLongClickListener(timerButtonLongClickListener);
        manualEntryLabel.setOnClickListener(manualButtonClickListener);

        checkView = view.findViewById(R.id.check);
        saveButton.setOnClickListener(saveButtonClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_log) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_sleepLogListFragment);
            }
        } else if (item.getItemId() == R.id.menu_item_children) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_childrenListFragment);
            }
        }
        return true;
    }

    private void resetUI() {
        millis = 0;
        timerLabel.setText(getString(R.string.zeroed_HMS_timer));
        timerButton.setText(getString(R.string.start));
        saveButton.setVisibility(View.GONE);
        manualEntryLabel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getActivity() != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void savedSuccessfully() {
        saveButton.setVisibility(View.GONE);
        checkView.check();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkView.uncheck();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetUI();
                    }
                }, 500);
            }
        }, 1000);

    }
}
