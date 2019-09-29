package com.example.nunezjonathan_poc.ui.children;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;

import java.util.ArrayList;

public class ChildrenListFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_children, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.children_list_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> stringArrayList = new ArrayList<String>(){{add("Charlotte - 08/13/19");}};

        if (getContext() != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, stringArrayList);
            setListAdapter(arrayAdapter);
        }
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
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            String itemText = (String) l.getItemAtPosition(position);
            String[] splitItemText = itemText.split("-");
            bundle.putString("name", splitItemText[0]);
            bundle.putString("dob", splitItemText[1]);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_childrenListFragment_to_childrenDetailsFragment, bundle);
        }
    }
}
