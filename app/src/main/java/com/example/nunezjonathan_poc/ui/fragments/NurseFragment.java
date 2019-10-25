package com.example.nunezjonathan_poc.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.services.NurseTimerService;
import com.example.nunezjonathan_poc.ui.viewModels.EventViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.FeedingViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.Calendar;

import cdflynn.android.library.checkview.CheckView;

public class NurseFragment extends Fragment implements EventActivityListener {

    public static final int DEFAULT = 0;
    public static final int LEFT_SIDE = 1;
    public static final int RIGHT_SIDE = 2;

    private FeedingViewModel feedingViewModel;
    private FeedingActivityListener mListener;
    public static int nursingSide = DEFAULT;
    private TextView timerLabel, manualEntryLabel;
    private ImageView leftSideIndicator, rightSideIndicator;
    private Button saveButton;
    private CheckView checkView;
    private long millis = 0;
    private long leftMillis = 0;
    private long rightMillis = 0;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            millis = intent.getLongExtra(NurseTimerService.EXTRA_TIME_MILLIS, 0);
            timerLabel.setText(TimeUtils.timerMS(millis));
            leftMillis = intent.getLongExtra(NurseTimerService.EXTRA_LEFT_MILLIS, 0);
            rightMillis = intent.getLongExtra(NurseTimerService.EXTRA_RIGHT_MILLIS, 0);
        }
    };

    private final View.OnClickListener timerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("nursing", Context.MODE_PRIVATE);
                Intent intent = new Intent(getActivity(), NurseTimerService.class);
                if (NurseTimerService.isRunning) {
                    if (v.getId() == R.id.button_nurse_left_timer) {
                        if (nursingSide == LEFT_SIDE) {
                            getActivity().stopService(intent);
                            manualEntryLabel.setVisibility(View.GONE);
                            saveButton.setVisibility(View.VISIBLE);
                        } else {
                            setLeftSide();
                        }
                    } else if (v.getId() == R.id.button_nurse_right_timer) {
                        if (nursingSide == RIGHT_SIDE) {
                            getActivity().stopService(intent);
                            manualEntryLabel.setVisibility(View.GONE);
                            saveButton.setVisibility(View.VISIBLE);
                        } else {
                            setRightSide();
                        }
                    }

                    sharedPrefs.edit().putInt("nursingSide", nursingSide).apply();

                } else {
                    if (v.getId() == R.id.button_nurse_left_timer) {
                        setLeftSide();
                    } else if (v.getId() == R.id.button_nurse_right_timer) {
                        setRightSide();
                    }

                    intent.putExtra(NurseTimerService.EXTRA_TIME_MILLIS, millis);
                    intent.putExtra(NurseTimerService.EXTRA_LEFT_MILLIS, leftMillis);
                    intent.putExtra(NurseTimerService.EXTRA_RIGHT_MILLIS, rightMillis);
                    ContextCompat.startForegroundService(getActivity(), intent);
                    manualEntryLabel.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
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

    private final View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Event feedingEvent = new Event(Event.EventType.NURSE,
                        CalendarUtils.toDatetimeString(NurseTimerService.datetime.getTime()),
                        millis, leftMillis, rightMillis,
                        -1, -1, Event.Color.NONE, Event.Hardness.NONE);
                feedingViewModel.insertFeedingEvent(NurseFragment.this, feedingEvent);
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
        feedingViewModel = ViewModelProviders.of(this).get(FeedingViewModel.class);
        if (getActivity() != null) {
            IntentFilter filter = new IntentFilter(NurseTimerService.ACTION_UPDATE_TIMER);
            getActivity().registerReceiver(receiver, filter);
        }

        return inflater.inflate(R.layout.fragment_nurse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerLabel = view.findViewById(R.id.textView_nurse_timer);
        leftSideIndicator = view.findViewById(R.id.left_indicator);
        rightSideIndicator = view.findViewById(R.id.right_indicator);
        SharedPreferences sharedPrefs = view.getContext().getSharedPreferences("nursing", Context.MODE_PRIVATE);
        int savedNursingSide = sharedPrefs.getInt("nursingSide", -1);
        if (savedNursingSide == LEFT_SIDE) {
            leftSideIndicator.setVisibility(View.VISIBLE);
        } else if (savedNursingSide == RIGHT_SIDE) {
            rightSideIndicator.setVisibility(View.VISIBLE);
        }
        manualEntryLabel = view.findViewById(R.id.textView_manual_nurse_entry);
        saveButton = view.findViewById(R.id.button_save_nurse);

        view.findViewById(R.id.button_nurse_left_timer).setOnClickListener(timerButtonClickListener);
        view.findViewById(R.id.button_nurse_left_timer).setOnLongClickListener(timerButtonLongClickListener);
        view.findViewById(R.id.button_nurse_right_timer).setOnClickListener(timerButtonClickListener);
        view.findViewById(R.id.button_nurse_right_timer).setOnLongClickListener(timerButtonLongClickListener);

        manualEntryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.manualNurseEntry();
            }
        });

        checkView = view.findViewById(R.id.check);
        saveButton.setOnClickListener(saveButtonClickListener);
    }

    private void resetUI() {
        nursingSide = DEFAULT;
        if (getContext()!= null) {
            SharedPreferences sharedPrefs = getContext().getSharedPreferences("nursing", Context.MODE_PRIVATE);
            sharedPrefs.edit().putInt("nursingSide", -1).apply();
        }
        millis = 0;
        leftMillis = 0;
        rightMillis = 0;
        timerLabel.setText(getString(R.string.zeroed_MS_timer));
        leftSideIndicator.setVisibility(View.INVISIBLE);
        rightSideIndicator.setVisibility(View.INVISIBLE);
        manualEntryLabel.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
    }

    private void setLeftSide() {
        nursingSide = LEFT_SIDE;
        leftSideIndicator.setVisibility(View.VISIBLE);
        rightSideIndicator.setVisibility(View.INVISIBLE);
        manualEntryLabel.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
    }

    private void setRightSide() {
        nursingSide = RIGHT_SIDE;
        leftSideIndicator.setVisibility(View.INVISIBLE);
        rightSideIndicator.setVisibility(View.VISIBLE);
        manualEntryLabel.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
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
//                        saveButton.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        }, 1000);
    }
}
