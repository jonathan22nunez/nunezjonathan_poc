package com.example.nunezjonathan_poc.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.EventAdapter;
import com.example.nunezjonathan_poc.adapters.HealthAdapter;
import com.example.nunezjonathan_poc.databases.FirestoreDatabase;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.interfaces.SwipeCallback;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.ui.activities.MainActivity;
import com.example.nunezjonathan_poc.ui.viewModels.FirestoreViewModel;
import com.example.nunezjonathan_poc.ui.viewModels.HealthViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.OptionalServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HealthFragment extends Fragment implements ItemClickListener {

    private FloatingActionButton fab_add_health, fab_add_symptom, fab_add_medication, fab_add_temperature;
    private Animation fab_open, fab_close, fab_rotate_clockwise, fab_rotate_counterclockwise;
    private boolean fabIsOpen = false;
    private RecyclerView recyclerView;
    private HealthAdapter adapter;
    private List<Health> healthList = new ArrayList<>();
    private TextView lastTemp;
    private TextView lastMedication;
    private String childName;
    private HealthViewModel healthViewModel;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_health, container, false);
            healthViewModel = ViewModelProviders.of(this).get(HealthViewModel.class);
            if (healthViewModel.getHealthList() != null) {
                healthViewModel.getHealthList().observe(this, new Observer<List<Health>>() {
                    @Override
                    public void onChanged(List<Health> health) {
                        emptyTextView = root.findViewById(R.id.emptyTextView);
                        emptyTextView.setVisibility(View.GONE);
                        if (health.size() > 0) {
                            healthList = health;
                            if (getContext() != null) {
                                Collections.sort(healthList, new Comparator<Health>() {
                                    @Override
                                    public int compare(Health o1, Health o2) {
                                        Calendar o1Datetime = CalendarUtils.stringToCalendar(o1.datetime);
                                        Calendar o2Datetime = CalendarUtils.stringToCalendar(o2.datetime);
                                        return o2Datetime.compareTo(o1Datetime);
                                    }
                                });

                                adapter = new HealthAdapter(healthList);
                                adapter.setItemClickListener(HealthFragment.this);
                                recyclerView.setAdapter(adapter);

                                for (Health h :
                                        healthList) {
                                    if (h.healthType == Health.HealthType.TEMPERATURE) {
                                        String temperature = h.temperature + "F";
                                        lastTemp.setText(temperature);
                                        break;
                                    }
                                }

                                for (Health h :
                                        healthList) {
                                    if (h.healthType == Health.HealthType.MEDICATION) {
                                        lastMedication.setText(h.toString());
                                        break;
                                    }
                                }
                            }
                        } else {
                            emptyTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        emptyTextView = root.findViewById(R.id.emptyTextView);
        emptyTextView.setVisibility(View.VISIBLE);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fabIsOpen) {
            fabIsOpen = false;
        }

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

        menu.findItem(R.id.menu_item_log).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (getActivity() != null) {
            if (item.getItemId() == R.id.menu_item_children) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_health_to_childrenListFragment);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getContext() != null) {
            lastTemp = view.findViewById(R.id.textView_last_temp);
            lastMedication = view.findViewById(R.id.textView_last_medication);

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

//            healthListView = view.findViewById(R.id.listView_health);
            adapter = new HealthAdapter(healthList);
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);

            SwipeCallback swipeCallback = new SwipeCallback(this, 0, ItemTouchHelper.LEFT) {
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();

                    if (direction == ItemTouchHelper.LEFT) {
                        final Health deletedHealth = healthList.get(position);
                        final int deletedPosition = position;
                        healthViewModel.deleteHealth(deletedHealth);
                        //deleteEvent(deletedEvent);
                        adapter.removeItem(position);
                        // showing snack bar with Undo option
                        if (getActivity() != null) {
                            Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Deleted Event", Snackbar.LENGTH_LONG);
                            snackbar.setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // undo is selected, restore the deleted item
                                    healthViewModel.insertHealth(null, deletedHealth);
                                    //restoreEvent(deletedEvent);
                                    adapter.restoreItem(deletedHealth, deletedPosition);
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

    @Override
    public void onClick(View view, int position) {
        if (healthList.get(position).healthType == Health.HealthType.SYMPTOM) {
            if (healthList.get(position).imageURIs.size() > 0 && getActivity() != null) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imageUris", healthList.get(position).imageURIs);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_health_to_symptomImagesFragment, bundle);
            }
        }
    }
}
