package com.example.nunezjonathan_poc.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.ui.viewModels.DiaperViewModel;

public class SoiledDiaperFragment extends Fragment {

    private DiaperViewModel diaperViewModel;
    private ImageView black, yellow, brown, green, orange, red;
    private int colorSelected = Event.Color.NONE;
    private String hardnessSelected = Event.Hardness.LOOSE;

    private View.OnClickListener colorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetImageViews();
            switch (v.getId()) {
                case R.id.imageView_color_black:
                    colorSelected = Event.Color.BLACK;
                    black.setBackground(getResources().getDrawable(R.drawable.green_rounded_corners_button, null));
                    break;
                case R.id.imageView_color_yellow:
                    colorSelected = Event.Color.YELLOW;
                    yellow.setBackground(getResources().getDrawable(R.drawable.green_rounded_corners_button, null));
                    break;
                case R.id.imageView_color_brown:
                    colorSelected = Event.Color.BROWN;
                    brown.setBackground(getResources().getDrawable(R.drawable.green_rounded_corners_button, null));
                    break;
                case R.id.imageView_color_green:
                    colorSelected = Event.Color.GREEN;
                    green.setBackground(getResources().getDrawable(R.drawable.green_rounded_corners_button, null));
                    break;
                case R.id.imageView_color_orange:
                    colorSelected = Event.Color.ORANGE;
                    orange.setBackground(getResources().getDrawable(R.drawable.green_rounded_corners_button, null));
                    break;
                case R.id.imageView_color_red:
                    colorSelected = Event.Color.RED;
                    red.setBackground(getResources().getDrawable(R.drawable.green_rounded_corners_button, null));
                    break;
            }
        }
    };

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null && getArguments() != null) {
                long childId = getArguments().getLong("childId");
                int eventType = getArguments().getInt("eventType");
                String datetime = getArguments().getString("datetime");

                Event diaperEvent = new Event(eventType, datetime,
                        -1, -1, -1, -1, -1, colorSelected, hardnessSelected);
                diaperViewModel.insertDiaperEvent(diaperEvent);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        diaperViewModel = ViewModelProviders.of(this).get(DiaperViewModel.class);
        return inflater.inflate(R.layout.fragment_soiled_diaper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        black = view.findViewById(R.id.imageView_color_black);
        yellow = view.findViewById(R.id.imageView_color_yellow);
        brown = view.findViewById(R.id.imageView_color_brown);
        green = view.findViewById(R.id.imageView_color_green);
        orange = view.findViewById(R.id.imageView_color_orange);
        red = view.findViewById(R.id.imageView_color_red);

        black.setOnClickListener(colorClickListener);
        yellow.setOnClickListener(colorClickListener);
        brown.setOnClickListener(colorClickListener);
        green.setOnClickListener(colorClickListener);
        orange.setOnClickListener(colorClickListener);
        red.setOnClickListener(colorClickListener);

        final SeekBar hardnessSB = view.findViewById(R.id.seekbar_hardness);
        hardnessSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    hardnessSelected = Event.Hardness.LOOSE;
                } else if (progress == 1) {
                    hardnessSelected = Event.Hardness.SOFT;
                } else if (progress == 2) {
                    hardnessSelected = Event.Hardness.HARD;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.button_save_soiled_diaper).setOnClickListener(saveButtonClickListener);
    }

    private void resetImageViews() {
        black.setBackground(null);
        yellow.setBackground(null);
        brown.setBackground(null);
        green.setBackground(null);
        orange.setBackground(null);
        red.setBackground(null);
    }
}
