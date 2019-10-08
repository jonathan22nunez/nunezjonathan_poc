package com.example.nunezjonathan_poc.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.nunezjonathan_poc.ui.viewModels.DiaperViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.Calendar;

public class DiaperFragment extends Fragment {

    private DiaperViewModel diaperViewModel;
    private Calendar startDatetime;
    private TextView date, time;

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

    private View.OnClickListener wetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                long childId = getContext().getSharedPreferences("currentChild",
                        Context.MODE_PRIVATE).getLong("childId", -1);
                if (childId != -1) {
                    Event diaperEvent = new Event(childId, Event.EventType.WET, CalendarUtils.toDatetimeString(startDatetime.getTime()),
                            -1, -1, -1, -1, -1, Event.Color.NONE, Event.Hardness.NONE);
                    diaperViewModel.insertDiaperEvent(diaperEvent);
                }
            }
        }
    };

    private View.OnClickListener soiledClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null && getActivity() != null) {
                long childId = getContext().getSharedPreferences("currentChild",
                        Context.MODE_PRIVATE).getLong("childId", -1);
                if (childId != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("childId", childId);

                    if (v.getId() == R.id.textView_mixed_activity) {
                        bundle.putInt("eventType", Event.EventType.MIXED);
                    } else {
                        bundle.putInt("eventType", Event.EventType.POOPY);
                    }

                    bundle.putString("datetime", CalendarUtils.toDatetimeString(startDatetime.getTime()));

                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.action_navigation_diaper_to_soiledDiaperFragment, bundle);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        diaperViewModel = ViewModelProviders.of(this).get(DiaperViewModel.class);
        return inflater.inflate(R.layout.fragment_diaper, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.activities_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_log) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_diaper_to_diaperLogListFragment);
            }
        } else if (item.getItemId() == R.id.menu_item_children) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_sleep_to_childrenListFragment);
            }
        }
        return true;
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

        view.findViewById(R.id.textView_wet_activity).setOnClickListener(wetClickListener);
        view.findViewById(R.id.textView_poopy_activity).setOnClickListener(soiledClickListener);
        view.findViewById(R.id.textView_mixed_activity).setOnClickListener(soiledClickListener);
    }

}
