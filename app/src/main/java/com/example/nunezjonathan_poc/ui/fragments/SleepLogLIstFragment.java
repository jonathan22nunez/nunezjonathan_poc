package com.example.nunezjonathan_poc.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.SleepListAdapter;
import com.example.nunezjonathan_poc.models.Sleep;
import com.example.nunezjonathan_poc.ui.viewModels.DatabaseViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.SleepViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.SleepViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SleepLogLIstFragment extends ListFragment {

    private DatabaseViewModel databaseViewModel;
    private SleepViewModel sleepViewModel;
    private SleepViewModelFactory factory;
    private List<Sleep> mSleepList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sleep_log, container, false);
        if (getActivity() != null) {
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            long childId = sharedPrefs.getLong("childId", -1);
            if (childId != -1) {
                factory = new SleepViewModelFactory(getActivity().getApplication(), childId);
                sleepViewModel = ViewModelProviders.of(this, factory).get(SleepViewModel.class);

                sleepViewModel.getSleepListLiveData().observe(this, new Observer<List<Sleep>>() {
                    @Override
                    public void onChanged(List<Sleep> sleeps) {
                        mSleepList = sleeps;
                        SleepListAdapter adapter = new SleepListAdapter(mSleepList);
                        setListAdapter(adapter);
                    }
                });
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
