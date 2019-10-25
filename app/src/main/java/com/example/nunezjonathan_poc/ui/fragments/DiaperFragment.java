package com.example.nunezjonathan_poc.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.interfaces.EventActivityListener;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.DiaperViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.Calendar;

import cdflynn.android.library.checkview.CheckView;

public class DiaperFragment extends Fragment implements EventActivityListener {

    private DiaperViewModel diaperViewModel;
    private Calendar startDatetime;
    private TextView date, time, wetEvent;
    private String childName;
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

    private View.OnClickListener wetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                Event diaperEvent = new Event(Event.EventType.WET, CalendarUtils.toDatetimeString(startDatetime.getTime()),
                        -1, -1, -1, -1, -1, Event.Color.NONE, Event.Hardness.NONE);
                diaperViewModel.insertDiaperEvent(DiaperFragment.this, diaperEvent);
            }
        }
    };

    private View.OnClickListener soiledClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null && getActivity() != null) {
                Bundle bundle = new Bundle();
                bundle.putLong("childId", -1);

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
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        diaperViewModel = ViewModelProviders.of(this).get(DiaperViewModel.class);
        return inflater.inflate(R.layout.fragment_diaper, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getContext() != null) {
            SharedPreferences sharedPrefs = getContext().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            String childName = sharedPrefs.getString("childName", null);
            if (childName != null) {
                this.childName = childName;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.activities_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (childName != null) {
            menu.findItem(R.id.menu_item_children).setTitle(childName);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_log) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_diaper_to_diaperLogListFragment);
            }
        } else if (item.getItemId() == R.id.menu_item_children) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_diaper_to_childrenListFragment);
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

        checkView = view.findViewById(R.id.check);
        wetEvent = view.findViewById(R.id.textView_wet_activity);
        wetEvent.setOnClickListener(wetClickListener);

        view.findViewById(R.id.textView_poopy_activity).setOnClickListener(soiledClickListener);
        view.findViewById(R.id.textView_mixed_activity).setOnClickListener(soiledClickListener);
    }

    @Override
    public void savedSuccessfully() {
        wetEvent.setVisibility(View.GONE);
        checkView.check();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkView.uncheck();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wetEvent.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        }, 1000);
    }
}
