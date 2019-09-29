package com.example.nunezjonathan_poc.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.ui.viewModels.HealthViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HealthFragment extends Fragment {

    private HealthViewModel healthViewModel;
    private FloatingActionButton fab_add_health, fab_add_symptom, fab_add_medication, fab_add_temperature;
    private Animation fab_open, fab_close, fab_rotate_clockwise, fab_rotate_counterclockwise;
    private boolean fabIsOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        healthViewModel = ViewModelProviders.of(this).get(HealthViewModel.class);

        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fabIsOpen) {
            fabIsOpen = false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
            fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
            fab_rotate_clockwise = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_rotate_clockwise);
            fab_rotate_counterclockwise = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_rotate_counterclockwise);

            fab_add_health = view.findViewById(R.id.fab_add_health_activity);
            fab_add_symptom = view.findViewById(R.id.fab_add_symptom_activity);
            fab_add_medication = view.findViewById(R.id.fab_add_medication_activity);
            fab_add_temperature = view.findViewById(R.id.fab_add_temperature_activity);

            fab_add_health.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fabIsOpen) {
                        fab_add_symptom.startAnimation(fab_close);
                        fab_add_medication.startAnimation(fab_close);
                        fab_add_temperature.startAnimation(fab_close);
                        fab_add_health.startAnimation(fab_rotate_counterclockwise);
                        fab_add_symptom.setClickable(false);
                        fab_add_medication.setClickable(false);
                        fab_add_temperature.setClickable(false);
                        fabIsOpen = false;
                    } else {
                        fab_add_symptom.startAnimation(fab_open);
                        fab_add_medication.startAnimation(fab_open);
                        fab_add_temperature.startAnimation(fab_open);
                        fab_add_health.startAnimation(fab_rotate_clockwise);
                        fab_add_symptom.setClickable(true);
                        fab_add_medication.setClickable(true);
                        fab_add_temperature.setClickable(true);
                        fabIsOpen = true;
                    }
                }
            });

            ListView healthListView = view.findViewById(R.id.listView_health);
            ArrayList<String> stringArrayList = new ArrayList<String>(){{
                add("Itchy throat - 12:00 PM");
                add("Took [medicine name] at 11:45 AM");
                add("Temp. 100.0 F at 11:30 AM");
                add("Head felt warm - 11:00 AM");
            }};
            if (getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stringArrayList);
                healthListView.setAdapter(adapter);
            }

            fab_add_symptom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_health_to_symptomFragment);
                    }
                }
            });

            fab_add_medication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_health_to_medicationFragment);
                    }
                }
            });

            fab_add_temperature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_health_to_temperatureFragment);
                    }
                }
            });
        }
    }
}
