package com.example.nunezjonathan_poc.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.FeedingViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.Calendar;
import java.util.Locale;

public class ManualBottleFragment extends Fragment {

    private FeedingViewModel feedingViewModel;
    private TextView dateLabel, startAmount, endAmount;
    private TimePicker startTime, endTime;
    private Calendar datetime;
    private double startingAmount = 0, endingAmount = 0;

    private final View.OnClickListener dateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                datetime.set(year, month, dayOfMonth);
                                dateLabel.setText(CalendarUtils.toDateString(datetime.getTime()));
                            }
                        },
                        datetime.get(Calendar.YEAR),
                        datetime.get(Calendar.MONTH),
                        datetime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        }
    };

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                long childId = sharedPrefs.getLong("childId", -1);
                if (childId != -1) {
                    long millis = durationBetweenStartAndEnd();
                    Event feedingEvent = new Event(Event.EventType.BOTTLE,
                            CalendarUtils.toDatetimeString(datetime.getTime()),
                            millis,
                            -1, -1 ,
                            startingAmount, endingAmount,
                            Event.Color.NONE, Event.Hardness.NONE);
                    feedingViewModel.insertFeedingEvent(feedingEvent);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        feedingViewModel = ViewModelProviders.of(this).get(FeedingViewModel.class);
        return inflater.inflate(R.layout.fragment_manual_bottle_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datetime = Calendar.getInstance();

        dateLabel = view.findViewById(R.id.textView_date);
        dateLabel.setText(CalendarUtils.toDateString(datetime.getTime()));
        dateLabel.setOnClickListener(dateClickListener);
        startTime = view.findViewById(R.id.timePicker_startTime);
        endTime = view.findViewById(R.id.timePicker_endTime);
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

        view.findViewById(R.id.button_save).setOnClickListener(saveButtonClickListener);

    }

    private long durationBetweenStartAndEnd() {
        datetime.set(datetime.get(Calendar.YEAR), datetime.get(Calendar.MONTH),
                datetime.get(Calendar.DAY_OF_MONTH), startTime.getHour(), startTime.getMinute());

        long startMillis = TimeUtils.convertToMillis(startTime.getHour(), startTime.getMinute());
        long endMillis = TimeUtils.convertToMillis(endTime.getHour(), endTime.getMinute());

        if (startMillis > endMillis) {
            endMillis = TimeUtils.add24HourMillis(endMillis);
        }

        return endMillis - startMillis;
    }
}
