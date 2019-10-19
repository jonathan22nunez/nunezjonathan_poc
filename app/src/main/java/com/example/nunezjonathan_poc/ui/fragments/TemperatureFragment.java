package com.example.nunezjonathan_poc.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.ui.viewModels.HealthViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Locale;

public class TemperatureFragment extends Fragment {

    private HealthViewModel healthViewModel;
    private Calendar startDatetime;
    private TextView date, time;
    private EditText temperatureInput, notes;
    private SeekBar temperatureSB;
    private double temp = 98.6;

    private View.OnClickListener dateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDatetime.set(year, month, dayOfMonth);
                        date.setText(CalendarUtils.toDateString(startDatetime.getTime()));
                    }
                },
                        startDatetime.get(Calendar.YEAR),
                        startDatetime.get(Calendar.MONTH),
                        startDatetime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        }
    };

    private View.OnClickListener timeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar tempCal = startDatetime;
                        startDatetime.set(
                                tempCal.get(Calendar.YEAR),
                                tempCal.get(Calendar.MONTH),
                                tempCal.get(Calendar.DAY_OF_MONTH),
                                hourOfDay, minute);
                        time.setText(CalendarUtils.toTimeHMString(startDatetime));
                    }
                },
                        startDatetime.get(Calendar.HOUR_OF_DAY),
                        startDatetime.get(Calendar.MINUTE),
                        false)
                        .show();
            }
        }
    };

    private View.OnFocusChangeListener temperatureInputFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                double inputTemperature;
                if (temperatureInput.getText().toString().contains("F")) {
                    String[] splitTemperatureString = temperatureInput.getText().toString().split("F");
                    inputTemperature = Double.parseDouble(splitTemperatureString[0]);
                } else {
                    inputTemperature = Double.parseDouble(temperatureInput.getText().toString());
                }

                if (inputTemperature < 95) {
                    temperatureSB.setProgress(0);
                } else if (inputTemperature > 105) {
                    temperatureSB.setProgress(100);
                } else {
                    temperatureSB.setProgress((int) (inputTemperature * 10) - 950);

                }
            }
        }
    };

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                    Health health = new Health(Health.HealthType.TEMPERATURE,
                            CalendarUtils.toDatetimeString(startDatetime.getTime()),
                            notes.getText().toString(),
                            null, null, null, null, -1, null,
                            temp);
                    healthViewModel.insertHealth(health);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        healthViewModel = ViewModelProviders.of(this).get(HealthViewModel.class);
        return inflater.inflate(R.layout.fragment_temperature, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startDatetime = Calendar.getInstance();

        date = view.findViewById(R.id.textView_date);
        date.setText(CalendarUtils.toDateString(startDatetime.getTime()));
        date.setOnClickListener(dateClickListener);
        time = view.findViewById(R.id.textView_time);
        time.setText(CalendarUtils.toTimeHMString(startDatetime));
        time.setOnClickListener(timeClickListener);

        temperatureInput = view.findViewById(R.id.editText_temperature);
        temperatureInput.setOnFocusChangeListener(temperatureInputFocusChangeListener);

        temperatureSB = view.findViewById(R.id.seekbar_temperature);
        temperatureSB.setProgress(36);
        temperatureSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp = 95.0 + ((double) progress / 10);
                String temperatureString = String.format(Locale.getDefault(), "%.1f", temp) + "F";
                temperatureInput.setText(temperatureString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        notes = view.findViewById(R.id.editText_notes);

        view.findViewById(R.id.button_save_temperature).setOnClickListener(saveButtonClickListener);
    }
}
