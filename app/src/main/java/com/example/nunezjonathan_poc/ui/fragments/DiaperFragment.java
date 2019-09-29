package com.example.nunezjonathan_poc.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.ui.viewModels.DiaperViewModel;

public class DiaperFragment extends Fragment {

    private DiaperViewModel diaperViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        diaperViewModel = ViewModelProviders.of(this).get(DiaperViewModel.class);
        return inflater.inflate(R.layout.fragment_diaper, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.activities_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_diaper_to_soiledDiaperFragment);
                }
            }
        };
        view.findViewById(R.id.textView_poopy_activity).setOnClickListener(clickListener);
        view.findViewById(R.id.textView_mixed_activity).setOnClickListener(clickListener);

        view.findViewById(R.id.textView_wet_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Saved Diaper Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
