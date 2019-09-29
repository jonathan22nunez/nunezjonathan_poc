package com.example.nunezjonathan_poc.ui.feeding;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;

public class BottleFragment extends Fragment {

    private FeedingActivityListener mListener;
    private boolean timerIsRunning = false;
    private TextView timerLabel, manualEntryLabel;
    private Button timerButton, saveButton;

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
        return inflater.inflate(R.layout.fragment_bottle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timerLabel = view.findViewById(R.id.textView_bottle_timer);
        timerButton = view.findViewById(R.id.button_start_stop_bottle_timer);
        manualEntryLabel = view.findViewById(R.id.textView_manual_bottle_entry);
        saveButton = view.findViewById(R.id.button_finish_bottle);

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
                    timerLabel.setText("16:34");
                    timerButton.setText("Stop");
                    saveButton.setVisibility(View.GONE);
                    manualEntryLabel.setVisibility(View.VISIBLE);
                }
            }
        });

        timerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                timerLabel.setText("00:00");
                manualEntryLabel.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    mListener.inputBottleDetails();
                }
            }
        });

        view.findViewById(R.id.textView_manual_bottle_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.manualBottleEntry();
            }
        });
    }
}
