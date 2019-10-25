package com.example.nunezjonathan_poc.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.services.BottleTimerService;
import com.example.nunezjonathan_poc.ui.viewModels.FeedingViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.Calendar;

public class BottleFragment extends Fragment {

    private FeedingActivityListener mListener;
    private TextView timerLabel, manualEntryLabel;
    private Button timerButton, finishButton;
    private long millis = 0;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            millis = intent.getLongExtra(BottleTimerService.EXTRA_TIME_MILLIS, 0);
            timerLabel.setText(TimeUtils.timerMS(millis));
        }
    };

    private final View.OnClickListener timerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), BottleTimerService.class);
                if (BottleTimerService.isRunning) {
                    getActivity().stopService(intent);
                    timerButton.setText(getString(R.string.start));
                    manualEntryLabel.setVisibility(View.GONE);
                    finishButton.setVisibility(View.VISIBLE);
                } else {
                    intent.putExtra(BottleTimerService.EXTRA_TIME_MILLIS, millis);
                    ContextCompat.startForegroundService(getActivity(), intent);
                    timerButton.setText(R.string.stop);
                    finishButton.setVisibility(View.GONE);
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
                mListener.manualBottleEntry();
            }
        }
    };

    private final View.OnClickListener finishButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Bundle bundle = new Bundle();
                bundle.putLong("childId", -1);
                bundle.putString("datetime", CalendarUtils.toDatetimeString(BottleTimerService.datetime.getTime()));
                bundle.putLong("duration", millis);

                mListener.inputBottleDetails(bundle);
                resetUI();
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof FeedingActivityListener) {
            mListener = (FeedingActivityListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            IntentFilter filter = new IntentFilter(BottleTimerService.ACTION_UPDATE_TIMER);
            getActivity().registerReceiver(receiver, filter);
        }
        return inflater.inflate(R.layout.fragment_bottle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerLabel = view.findViewById(R.id.textView_bottle_timer);
        timerButton = view.findViewById(R.id.button_start_stop_bottle_timer);
        if (BottleTimerService.isRunning) {
            timerButton.setText(getString(R.string.stop));
        }
        manualEntryLabel = view.findViewById(R.id.textView_manual_bottle_entry);
        finishButton = view.findViewById(R.id.button_finish_bottle);

        timerButton.setOnClickListener(timerButtonClickListener);
        timerButton.setOnLongClickListener(timerButtonLongClickListener);
        manualEntryLabel.setOnClickListener(manualButtonClickListener);
        finishButton.setOnClickListener(finishButtonClickListener);
    }

    private void resetUI() {
        millis = 0;
        timerLabel.setText(getString(R.string.zeroed_HMS_timer));
        timerButton.setText(getString(R.string.start));
        finishButton.setVisibility(View.GONE);
        manualEntryLabel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getActivity() != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
