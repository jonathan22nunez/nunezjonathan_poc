package com.example.nunezjonathan_poc.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.ui.viewModels.HealthViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.Calendar;

import cdflynn.android.library.checkview.CheckView;

public class MedicationFragment extends Fragment implements EventActivityListener {

    private HealthViewModel healthViewModel;
    private Calendar startDatetime;
    private TextView date, time;
    private EditText drugName, brandName, dosage, notes;
    private String dosageUnit = "";
    private Button saveButton;
    private CheckView checkView;

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

    private AdapterView.OnItemSelectedListener dosageUnitItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 1:
                    dosageUnit = "drops";
                    break;
                case 2:
                    dosageUnit = "oz";
                    break;
                case 3:
                    dosageUnit = "tsp";
                    break;
                case 4:
                    dosageUnit = "tbsp";
                    break;
                case 5:
                    dosageUnit = "mL";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                    double dosageNum;
                    if (dosage.getText().toString().isEmpty()) {
                        dosageNum = 0;
                    } else {
                        dosageNum = Double.parseDouble(dosage.getText().toString());
                    }
                    Health health = new Health(Health.HealthType.MEDICATION,
                            CalendarUtils.toDatetimeString(startDatetime.getTime()),
                            notes.getText().toString(),
                            null, null,
                            drugName.getText().toString(),
                            brandName.getText().toString(),
                            dosageNum,
                            dosageUnit,
                            -1);
                    healthViewModel.insertHealth(MedicationFragment.this, health);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        healthViewModel = ViewModelProviders.of(this).get(HealthViewModel.class);
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startDatetime = Calendar.getInstance();

        if (getContext() != null) {
            date = view.findViewById(R.id.textView_date);
            date.setText(CalendarUtils.toDateString(startDatetime.getTime()));
            date.setOnClickListener(dateClickListener);
            time = view.findViewById(R.id.textView_time);
            time.setText(CalendarUtils.toTimeHMString(startDatetime));
            time.setOnClickListener(timeClickListener);

            drugName = view.findViewById(R.id.editText_drug_name);
            brandName = view.findViewById(R.id.editText_brand_name);
            dosage = view.findViewById(R.id.editText_dosage);

            Spinner dosageUnits = view.findViewById(R.id.spinner_unit);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.dosage_units, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dosageUnits.setAdapter(spinnerAdapter);
            dosageUnits.setOnItemSelectedListener(dosageUnitItemSelectedListener);

            notes = view.findViewById(R.id.editText_notes);

            checkView = view.findViewById(R.id.check);
            saveButton = view.findViewById(R.id.button_save_medication);
            saveButton.setOnClickListener(saveButtonClickListener);
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
                        if (getActivity() != null) {
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                        }
                    }
                }, 500);
            }
        }, 1000);
    }
}
