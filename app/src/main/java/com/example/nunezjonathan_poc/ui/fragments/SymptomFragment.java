package com.example.nunezjonathan_poc.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.ui.viewModels.HealthViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.ImageUtils;

import java.util.Calendar;

public class SymptomFragment extends Fragment {

    private static final int REQUEST_CODE_CAMERA_IMAGE = 101;
    private static final int REQUEST_CODE_GALLERY_IMAGE = 102;

    private HealthViewModel healthViewModel;
    private Calendar startDatetime;
    private TextView date, time;
    private EditText symptomInput, notes;
    private ImageButton image1, image2, image3, image4, image5;
    private String image1UriString = "", image2UriString = "", image3UriString = "", image4UriString = "", image5UriString = "";
    private Uri imageUri;
    private int selectedImageButton;

    private View.OnClickListener dateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDatetime.set(year, month, dayOfMonth);
                        date.setText(CalendarUtils.toDateString(startDatetime.getTime()));
                    }
                },
                        startDatetime.get(Calendar.YEAR),
                        startDatetime.get(Calendar.MONTH),
                        startDatetime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        }
    };

    private View.OnClickListener timeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar tempCal = startDatetime;
                        startDatetime.set(
                                tempCal.get(Calendar.YEAR),
                                tempCal.get(Calendar.MONTH),
                                tempCal.get(Calendar.DAY_OF_MONTH),
                                hourOfDay, minute);
                        time.setText(CalendarUtils.toTimeHMString(startDatetime));
                    }
                },
                        startDatetime.get(Calendar.HOUR_OF_DAY),
                        startDatetime.get(Calendar.MINUTE),
                        false)
                        .show();

            }
        }
    };

    private View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectedImageButton = v.getId();
            ImageUtils.getImageFromIntent(SymptomFragment.this);
        }
    };

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
                long childId = sharedPrefs.getLong("childId", -1);
                if (childId != -1) {
                    Health health = new Health(childId, Health.HealthType.SYMPTOM,
                            CalendarUtils.toDatetimeString(startDatetime.getTime()),
                            notes.getText().toString(), symptomInput.getText().toString(),
                            image1UriString, image2UriString, image3UriString, image4UriString, image5UriString,
                            null, null, -1, null, -1);
                    healthViewModel.insertHealth(health);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        healthViewModel = ViewModelProviders.of(this).get(HealthViewModel.class);
        return inflater.inflate(R.layout.fragment_symptom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startDatetime = Calendar.getInstance();

        date = view.findViewById(R.id.textView_date);
        date.setText(CalendarUtils.toDateString(startDatetime.getTime()));
        date.setOnClickListener(dateClickListener);
        time = view.findViewById(R.id.textView_time);
        time.setText(CalendarUtils.toTimeHMSString(startDatetime));
        time.setOnClickListener(timeClickListener);

        symptomInput = view.findViewById(R.id.editText_symptom);
        notes = view.findViewById(R.id.editText_notes);

        image1 = view.findViewById(R.id.symptom_image_1);
        image1.setOnClickListener(imageClickListener);
        image2 = view.findViewById(R.id.symptom_image_2);
        image2.setOnClickListener(imageClickListener);
        image3 = view.findViewById(R.id.symptom_image_3);
        image3.setOnClickListener(imageClickListener);
        image4 = view.findViewById(R.id.symptom_image_4);
        image4.setOnClickListener(imageClickListener);
        image5 = view.findViewById(R.id.symptom_image_5);
        image5.setOnClickListener(imageClickListener);

        view.findViewById(R.id.button_save_symptom).setOnClickListener(saveButtonClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CAMERA_IMAGE) {
                imageUri = ImageUtils.imageUri;
            } else if (requestCode == REQUEST_CODE_GALLERY_IMAGE) {
                imageUri = data.getData();
            }

            switch (selectedImageButton) {
                case R.id.symptom_image_1:
                    image1.setImageURI(imageUri);
                    image1UriString = imageUri.toString();
                    break;
                case R.id.symptom_image_2:
                    image2.setImageURI(imageUri);
                    image2UriString = imageUri.toString();
                    break;
                case R.id.symptom_image_3:
                    image3.setImageURI(imageUri);
                    image3UriString = imageUri.toString();
                    break;
                case R.id.symptom_image_4:
                    image4.setImageURI(imageUri);
                    image4UriString = imageUri.toString();
                    break;
                case R.id.symptom_image_5:
                    image5.setImageURI(imageUri);
                    image5UriString = imageUri.toString();
                    break;
            }
        }
    }
}
