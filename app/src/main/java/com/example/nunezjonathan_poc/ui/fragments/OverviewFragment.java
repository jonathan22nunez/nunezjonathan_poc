package com.example.nunezjonathan_poc.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.EventAdapter;
import com.example.nunezjonathan_poc.adapters.NurseListAdapter;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.EventViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.FirestoreViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.OverviewViewModel;
import com.example.nunezjonathan_poc.utils.OptionalServices;

import java.util.ArrayList;
import java.util.List;

//TODO fix issue where selecting a Child doesn't update the Overview & Health list
public class OverviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private String childName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_children_test, container, false);
        OverviewViewModel overviewViewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        if (overviewViewModel.getOverviewList() != null) {
            overviewViewModel.getOverviewList().observe(this, new Observer<List<Event>>() {
                @Override
                public void onChanged(List<Event> events) {
                    if (getContext() != null) {
                        adapter = new EventAdapter(getContext(), events);

                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.overview_menu, menu);
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
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (childName != null) {
            menu.findItem(R.id.menu_item_children).setTitle(childName);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
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
