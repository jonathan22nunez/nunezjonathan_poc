package com.example.nunezjonathan_poc.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.EventListAdapter;
import com.example.nunezjonathan_poc.adapters.SleepListAdapter;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.FirestoreViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.SleepViewModel;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.List;

public class SleepLogListFragment extends ListFragment {

//    private List<Sleep> mSleepList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        if (OptionalServices.cloudSyncEnabled(getContext())) {
            FirestoreViewModel firestoreViewModel = ViewModelProviders.of(this).get(FirestoreViewModel.class);
            if (getContext() != null) {
                long childId = getContext().getSharedPreferences("currentChild",
                        Context.MODE_PRIVATE).getLong("childId", -1);
                if (childId != -1) {
                    firestoreViewModel.getSleepList(FirestoreDatabase.getCurrentUser().getUid(), childId).observe(this, new Observer<List<Event>>() {
                        @Override
                        public void onChanged(List<Event> events) {
                            setListAdapter(new SleepListAdapter(events));
                        }
                    });
                }
            }
        } else {
            SleepViewModel sleepViewModel = ViewModelProviders.of(this).get(SleepViewModel.class);
            if (sleepViewModel.getSleepList() != null) {
                sleepViewModel.getSleepList().observe(this, new Observer<List<Event>>() {
                    @Override
                    public void onChanged(List<Event> events) {
                        setListAdapter(new SleepListAdapter(events));
                        //setListAdapter(new EventListAdapter(events));
                    }
                });
            }
        }

        return root;
    }
}
