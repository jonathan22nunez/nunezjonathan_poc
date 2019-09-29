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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.ChildRecyclerAdapter;
import com.example.nunezjonathan_poc.interfaces.ItemClickListener;
import com.example.nunezjonathan_poc.interfaces.SwipeToDeleteCallback;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.ui.viewModels.DatabaseViewModel;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.material.snackbar.Snackbar;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

public class ChildrenListFragment extends Fragment implements ItemClickListener {

    private RecyclerView recyclerView;
    private ChildRecyclerAdapter recyclerAdapter;
    private List<Child> mChildren;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_children_test, container, false);
        DatabaseViewModel databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        databaseViewModel.getChildrenListLiveData().observe(this, new Observer<List<Child>>() {
            @Override
            public void onChanged(List<Child> children) {
                mChildren = children;
                recyclerAdapter = new ChildRecyclerAdapter(children);
                recyclerAdapter.setItemClickListener(ChildrenListFragment.this);
                recyclerView.setAdapter(recyclerAdapter);
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.children_list_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(view.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Child childItem = recyclerAdapter.getChildren().get(position);

                if (getActivity() != null) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.action_childrenListFragment_to_childrenDetailsFragment,
                                    childItem.getChildBundle());
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (getActivity() != null) {
            if (item.getItemId() == R.id.menu_item_add_child) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_childrenListFragment_to_childrenDetailsFragment);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view, int position) {
        if (getActivity() != null) {
            mChildren.get(position).makeCurrentlySelected(getActivity());
        }
    }
}
