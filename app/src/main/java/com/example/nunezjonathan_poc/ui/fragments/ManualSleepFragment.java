package com.example.nunezjonathan_poc.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.SleepViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.TimeUtils;

import java.util.Calendar;

public class ManualSleepFragment extends Fragment {

    private SleepViewModel sleepViewModel;
    private TextView sleepDate;
    private TimePicker startTime, endTime;
    private Calendar datetime;

    private final View.OnClickListener sleepDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                datetime.set(year, month, dayOfMonth);
                                sleepDate.setText(CalendarUtils.toDateString(datetime.getTime()));
                            }
                        },
                        datetime.get(Calendar.YEAR),
                        datetime.get(Calendar.MONTH),
                        datetime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sleepViewModel = ViewModelProviders.of(this).get(SleepViewModel.class);
        return inflater.inflate(R.layout.fragment_manual_sleep_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datetime = Calendar.getInstance();

        sleepDate = view.findViewById(R.id.textView_sleep_date);
        sleepDate.setText(CalendarUtils.toDateString(datetime.getTime()));
        sleepDate.setOnClickListener(sleepDateClickListener);
        startTime = view.findViewById(R.id.timePicker_startTime);
        endTime = view.findViewById(R.id.timePicker_endTime);

        view.findViewById(R.id.button_save_manual_sleep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    SharedPreferences sharedPrefs = getActivity().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                    long childId = sharedPrefs.getLong("childId", -1);
                    if (childId != -1) {
                        long millis = durationBetweenStartAndEnd();
                        Event sleepEvent = new Event(childId, Event.EventType.SLEEP,
                                CalendarUtils.toDatetimeString(datetime.getTime()),
                                millis,
                                -1, -1, -1, -1, Event.Color.NONE, Event.Hardness.NONE);
                        sleepViewModel.insertSleep(sleepEvent);
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                    }
                }
            }
        });
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
