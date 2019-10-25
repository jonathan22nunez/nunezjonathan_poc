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
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.EventAdapter;
import com.example.nunezjonathan_poc.adapters.OverviewAdapter;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.OverviewViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OverviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private OverviewAdapter adapter;
    private String childName;
    private OverviewViewModel overviewViewModel;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_children_test, container, false);
        overviewViewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        if (overviewViewModel.getOverviewList() != null) {
            overviewViewModel.getOverviewList().observe(this, new Observer<List<Event>>() {
                @Override
                public void onChanged(List<Event> events) {
                    if (events.size() > 0) {
                        if (getContext() != null) {
                            Collections.sort(events, new Comparator<Event>() {
                                @Override
                                public int compare(Event o1, Event o2) {
                                    Calendar o1Datetime = CalendarUtils.stringToCalendar(o1.datetime);
                                    Calendar o2Datetime = CalendarUtils.stringToCalendar(o2.datetime);
                                    return o2Datetime.compareTo(o1Datetime);
                                }
                            });

                            adapter = new OverviewAdapter(getContext(), events);

                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        emptyTextView = root.findViewById(R.id.emptyTextView);
                        emptyTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            emptyTextView = root.findViewById(R.id.emptyTextView);
            emptyTextView.setVisibility(View.VISIBLE);
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
//        emptyTextView = view.findViewById(R.id.emptyTextView);
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
