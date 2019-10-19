package com.example.nunezjonathan_poc.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.SymptomImagesAdapter;

import java.util.ArrayList;

public class SymptomImagesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_symptom_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            ArrayList<String> imageUris = getArguments().getStringArrayList("imageUris");

            GridView gv = view.findViewById(R.id.gridView);
            gv.setAdapter(new SymptomImagesAdapter(imageUris));
        }
    }
}
