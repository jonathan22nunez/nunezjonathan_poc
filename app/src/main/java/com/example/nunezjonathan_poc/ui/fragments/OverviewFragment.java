package com.example.nunezjonathan_poc.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.NurseListAdapter;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.EventViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.FirestoreViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.OverviewViewModel;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        if (OptionalServices.cloudSyncEnabled(getContext())) {
            FirestoreViewModel firestoreViewModel = ViewModelProviders.of(this).get(FirestoreViewModel.class);
            if (getContext() != null) {
                long childId = getContext().getSharedPreferences("currentChild",
                        Context.MODE_PRIVATE).getLong("childId", -1);
                if (childId != -1) {
                    firestoreViewModel.getAllEventsList(FirestoreDatabase.getCurrentUser().getUid(), childId).observe(this, new Observer<List<Event>>() {
                        @Override
                        public void onChanged(List<Event> events) {
                            if (getContext() != null) {
                                ArrayAdapter<Event> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, events);
                                setListAdapter(adapter);
                            }
                        }
                    });
                }
            }
        } else {
            OverviewViewModel overviewViewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
            if (overviewViewModel.getOverviewList() != null) {
                overviewViewModel.getOverviewList().observe(this, new Observer<List<Event>>() {
                    @Override
                    public void onChanged(List<Event> events) {
                        if (getContext() != null) {
                            ArrayAdapter<Event> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, events);
                            setListAdapter(adapter);
                        }
                    }
                });
            }
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.overview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (getActivity() != null) {
            if (item.getItemId() == R.id.menu_item_settings) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_overview_to_settingsFragment);
            } else if (item.getItemId() == R.id.menu_item_children) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_overview_to_childrenListFragment);
            }
        }
        return true;
    }
}
