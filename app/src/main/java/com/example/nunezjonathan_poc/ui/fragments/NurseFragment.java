package com.example.nunezjonathan_poc.ui.feeding;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;

public class NurseFragment extends Fragment {

    private static final int DEFAULT = 0;
    private static final int LEFT_SIDE = 1;
    private static final int RIGHT_SIDE = 2;

    private FeedingActivityListener mListener;
    private int nursingSide = DEFAULT;
    private boolean timerIsRunning;
    private TextView timerLabel, manualEntryLabel;
    private ImageView leftSideIndicator, rightSideIndicator;
    private Button leftTimerButton, rightTimerButton, saveButton;


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
        return inflater.inflate(R.layout.fragment_nurse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerLabel = view.findViewById(R.id.textView_nurse_timer);
        leftSideIndicator = view.findViewById(R.id.left_indicator);
        rightSideIndicator = view.findViewById(R.id.right_indicator);
        leftTimerButton = view.findViewById(R.id.button_nurse_left_timer);
        rightTimerButton = view.findViewById(R.id.button_nurse_right_timer);
        manualEntryLabel = view.findViewById(R.id.textView_manual_nurse_entry);
        saveButton = view.findViewById(R.id.button_save_nurse);

        leftTimerButton.setOnClickListener(timerButtonClick);
        leftTimerButton.setOnLongClickListener(timerButtonLongClick);
        rightTimerButton.setOnClickListener(timerButtonClick);
        rightTimerButton.setOnLongClickListener(timerButtonLongClick);

        manualEntryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.manualNurseEntry();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Saved Nurse activity", Toast.LENGTH_SHORT).show();
                resetScreen();
            }
        });
    }

    private View.OnClickListener timerButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (timerIsRunning) {
                if (v.getId() == R.id.button_nurse_left_timer) {
                    if (nursingSide == LEFT_SIDE) {
                        stopTimer();
                    } else {
                        setLeftSide();
                    }
                } else if (v.getId() == R.id.button_nurse_right_timer) {
                    if (nursingSide == RIGHT_SIDE) {
                        stopTimer();
                    } else {
                        setRightSide();
                    }
                }
            } else {
                timerIsRunning = true;
                timerLabel.setText("16:37");
                if (v.getId() == R.id.button_nurse_left_timer) {
                    setLeftSide();
                } else if (v.getId() == R.id.button_nurse_right_timer) {
                    setRightSide();
                }
                manualEntryLabel.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
            }
        }
    };

    private View.OnLongClickListener timerButtonLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            resetScreen();
            return true;
        }
    };

    private void stopTimer() {
        timerIsRunning = false;
        saveButton.setVisibility(View.VISIBLE);
        manualEntryLabel.setVisibility(View.GONE);
    }

    private void resetScreen() {
        timerIsRunning = false;
        nursingSide = DEFAULT;
        timerLabel.setText("00:00");
        leftSideIndicator.setVisibility(View.INVISIBLE);
        rightSideIndicator.setVisibility(View.INVISIBLE);
        manualEntryLabel.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
    }

    private void setLeftSide() {
        nursingSide = LEFT_SIDE;
        leftSideIndicator.setVisibility(View.VISIBLE);
        rightSideIndicator.setVisibility(View.INVISIBLE);
    }

    private void setRightSide() {
        nursingSide = RIGHT_SIDE;
        leftSideIndicator.setVisibility(View.INVISIBLE);
        rightSideIndicator.setVisibility(View.VISIBLE);
    }
}
