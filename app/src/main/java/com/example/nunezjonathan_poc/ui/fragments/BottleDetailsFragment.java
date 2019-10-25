package com.example.nunezjonathan_poc.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.FeedingViewModel;

import java.util.Locale;

import cdflynn.android.library.checkview.CheckView;

public class BottleDetailsFragment extends Fragment implements EventActivityListener {

    private FeedingViewModel feedingViewModel;
    private TextView startAmount, endAmount;
    private double startingAmount;
    private double endingAmount;
    private Button saveButton;
    private CheckView checkView;

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null && getArguments() != null) {
                long childId = getArguments().getLong("childId");
                String startDatetime = getArguments().getString("datetime");
                long duration = getArguments().getLong("duration");

                Event feedingEvent = new Event(Event.EventType.BOTTLE, startDatetime, duration,
                        -1, -1, startingAmount, endingAmount, Event.Color.NONE, Event.Hardness.NONE);

                feedingViewModel.insertFeedingEvent(BottleDetailsFragment.this, feedingEvent);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        feedingViewModel = ViewModelProviders.of(this).get(FeedingViewModel.class);
        return inflater.inflate(R.layout.fragment_bottle_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startAmount = view.findViewById(R.id.editText_starting_amount);
        endAmount = view.findViewById(R.id.editText_ending_amount);

        SeekBar startAmountSB = view.findViewById(R.id.seekBar_starting_amount);
        startAmountSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startingAmount = (double) progress/10;
                startAmount.setText(String.format(Locale.getDefault(), "%.1f", startingAmount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        SeekBar endAmountSB = view.findViewById(R.id.seekbar_ending_amount);
        endAmountSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                endingAmount = (double) progress/10;
                endAmount.setText(String.format(Locale.getDefault(), "%.1f", endingAmount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkView = view.findViewById(R.id.check);
        saveButton = view.findViewById(R.id.button_save_bottle);
        saveButton.setOnClickListener(saveButtonClickListener);
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
                        if (getActivity() != null) {
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                        }
                    }
                }, 500);
            }
        }, 1000);
    }
}
