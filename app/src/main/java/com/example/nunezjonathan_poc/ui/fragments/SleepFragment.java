package com.example.nunezjonathan_poc.ui.sleep;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;

public class SleepFragment extends Fragment {

    private SleepViewModel sleepViewModel;
    private TextView timerLabel, manualEntryLabel;
    private Button timerButton, saveButton;
    private boolean timerIsRunning = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sleepViewModel = ViewModelProviders.of(this).get(SleepViewModel.class);
        return inflater.inflate(R.layout.fragment_sleep, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.activities_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerLabel = view.findViewById(R.id.textView_sleep_timer);
        timerButton = view.findViewById(R.id.button_start_stop_sleep_timer);
        manualEntryLabel = view.findViewById(R.id.textView_manual_sleep_entry);
        saveButton = view.findViewById(R.id.button_save_sleep);

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerIsRunning) {
                    timerIsRunning = false;
                    timerButton.setText("Start");
                    manualEntryLabel.setVisibility(View.GONE);
                    saveButton.setVisibility(View.VISIBLE);
                } else {
                    timerIsRunning = true;
                    timerLabel.setText("01:34:59");
                    timerButton.setText("Stop");
                    saveButton.setVisibility(View.GONE);
                    manualEntryLabel.setVisibility(View.VISIBLE);
                }
            }
        });

        timerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetScreen();
                return true;
            }
        });

        manualEntryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_manualSleepFragment);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Saved Sleep activity", Toast.LENGTH_SHORT).show();
                resetScreen();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_log) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_sleepLogLIstFragment);
            }
        } else if (item.getItemId() == R.id.menu_item_children) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_childrenListFragment);
            }
        }
        return true;
    }

    private void resetScreen() {
        timerLabel.setText("00:00:00");
        timerIsRunning = false;
        timerButton.setText("Start");
        saveButton.setVisibility(View.GONE);
        manualEntryLabel.setVisibility(View.VISIBLE);
    }
}
