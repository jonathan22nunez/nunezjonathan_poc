package com.example.nunezjonathan_poc.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.EventAdapter;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.interfaces.SwipeCallback;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.DiaperViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.FirestoreViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiaperLogListFragment extends Fragment implements ItemClickListener {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> diaperEvents = new ArrayList<>();
    private DiaperViewModel diaperViewModel;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_children_test, container, false);
        diaperViewModel = ViewModelProviders.of(this).get(DiaperViewModel.class);
        if (diaperViewModel.getDiaperList() != null) {
            diaperViewModel.getDiaperList().observe(this, new Observer<List<Event>>() {
                @Override
                public void onChanged(List<Event> events) {
                    if (events.size() > 0) {
                        Collections.sort(events, new Comparator<Event>() {
                            @Override
                            public int compare(Event o1, Event o2) {
                                Calendar o1Datetime = CalendarUtils.stringToCalendar(o1.datetime);
                                Calendar o2Datetime = CalendarUtils.stringToCalendar(o2.datetime);
                                return o2Datetime.compareTo(o1Datetime);
                            }
                        });

                        diaperEvents = events;
                        Event event = new Event();
                        event.eventType = -1;
                        diaperEvents.add(0, event);
                        adapter = new EventAdapter(getContext(), events);
                        adapter.setItemClickListener(DiaperLogListFragment.this);

                        recyclerView.setAdapter(adapter);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new EventAdapter(diaperEvents);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        SwipeCallback swipeCallback = new SwipeCallback(this, 0, ItemTouchHelper.LEFT) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    final Event deletedEvent = diaperEvents.get(position);
                    final int deletedPosition = position;
                    diaperViewModel.deleteDiaper(deletedEvent);
//                    deleteEvent(deletedEvent);
                    adapter.removeItem(position);
                    // showing snack bar with Undo option
                    if (getActivity() != null) {
                        Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Deleted Event", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // undo is selected, restore the deleted item
//                                restoreEvent(deletedEvent);
                                diaperViewModel.insertDiaperEvent(null, deletedEvent);
                                adapter.restoreItem(deletedEvent, deletedPosition);
//                            adapter.restoreItem(deletedModel, deletedPosition);
                            }
                        });
                        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent, null));
                        snackbar.show();
                    }
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view, int position) {
        Event event = diaperEvents.get(position);
        Toast.makeText(getContext(), "Clicked Event..." + event.documentId, Toast.LENGTH_SHORT).show();
    }
}
