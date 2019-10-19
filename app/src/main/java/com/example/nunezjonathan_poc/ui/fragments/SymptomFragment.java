package com.example.nunezjonathan_poc.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Calendar;

public class SymptomFragment extends Fragment {

    private static final int REQUEST_CODE_CAMERA_IMAGE = 101;
    private static final int REQUEST_CODE_GALLERY_IMAGE = 102;

    private HealthViewModel healthViewModel;
    private Calendar startDatetime;
    private TextView date, time;
    private EditText symptomInput, notes;
    private ArrayList<String> imageURIs = new ArrayList<>();

    private LinearLayout imagesContainer;


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
            String[] imageOptions = {
                    "Camera",
                    "Gallery"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Get image from...");
            builder.setItems(imageOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        ImageUtils.startCameraIntent(SymptomFragment.this);
                    } else if (which == 1) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(intent, "Select Image(s)..."), 500);
                    }
                }
            });
            builder.setPositiveButton("Cancel", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                    Health health = new Health(Health.HealthType.SYMPTOM,
                            CalendarUtils.toDatetimeString(startDatetime.getTime()),
                            notes.getText().toString(), symptomInput.getText().toString(), imageURIs,
                            null, null, -1, null, -1);
                    healthViewModel.insertHealth(health);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
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

        view.findViewById(R.id.button_add_image).setOnClickListener(imageClickListener);

        imagesContainer = view.findViewById(R.id.symptom_images_container);

        view.findViewById(R.id.button_save_symptom).setOnClickListener(saveButtonClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CAMERA_IMAGE) {
                imagesContainer.addView(createImageButton(ImageUtils.imageUri));

                imageURIs.add(ImageUtils.imageUri.toString());

            } else if (requestCode == 500) {
                //TODO for images from Gallery, create a Bitmap and save to local storage
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                // String path = saveImage(bitmap);
                if (data.getData() != null) {
                    imagesContainer.addView(createImageButton(data.getData()));

                    imageURIs.add(data.getData().toString());
                } else if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);

                        imagesContainer.addView(createImageButton(item.getUri()));

                        imageURIs.add(item.getUri().toString());
                    }
                }
            }
        }
    }

    private ImageButton createImageButton(Uri imageUri) {
        ImageButton imageButton = new ImageButton(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, 250);
        imageButton.setLayoutParams(params);
        imageButton.setBackground(null);
        imageButton.setImageURI(imageUri);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return imageButton;
    }
}
