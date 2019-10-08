package com.example.nunezjonathan_poc.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.ui.viewModels.ChildrenViewModel;
import com.example.nunezjonathan_poc.utils.CalendarUtils;
import com.example.nunezjonathan_poc.utils.ImageUtils;

import java.util.Calendar;

public class ChildrenDetailsFragment extends Fragment {

    private static final int REQUEST_CODE_CAMERA_IMAGE = 101;
    private static final int REQUEST_CODE_GALLERY_IMAGE = 102;

    private ChildrenViewModel mViewModel;

    private ImageView childImage;
    private Spinner sexSelector;
    private EditText childName, childDob, childNotes;
    private TextView deleteChildProfile;
    private Calendar calendar;
    private Uri imageUri;

    private final View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageUtils.getImageFromIntent(ChildrenDetailsFragment.this);
        }
    };

    private final View.OnClickListener dobClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                childDob.setText(CalendarUtils.toDateString(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_children_details, container, false);
        mViewModel = ViewModelProviders.of(this).get(ChildrenViewModel.class);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.children_details_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getContext() != null) {
            calendar = Calendar.getInstance();

            sexSelector = view.findViewById(R.id.spinner_child_sex);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.child_sex_spinner,
                    android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sexSelector.setAdapter(spinnerAdapter);

            childImage = view.findViewById(R.id.imageView_child_image);
            childName = view.findViewById(R.id.editText_child_name);
            childDob = view.findViewById(R.id.editText_child_dob);
            childNotes = view.findViewById(R.id.editText_child_notes);
            deleteChildProfile = view.findViewById(R.id.textView_delete_child_profile);

            childImage.setOnClickListener(imageClickListener);
            childDob.setOnClickListener(dobClickListener);

            showChildDetailsIfExists();
        }
    }

    private void showChildDetailsIfExists() {
        if (getArguments() != null) {
            final Child child = new Child(getArguments().getString("name"));
            child._id = getArguments().getInt("_id");
            child.dob = getArguments().getString("dob");
            child.sex = getArguments().getInt("sex");
            child.notes = getArguments().getString("notes");
            child.imageStringUri = getArguments().getString("image");

            childName.setText(child.name);
            childDob.setText(child.dob);
            sexSelector.setSelection(child.sex);
            childNotes.setText(child.notes);
            imageUri = Uri.parse(child.imageStringUri);
            if (imageUri != null) {
                childImage.setImageURI(imageUri);
            }

            deleteChildProfile.setVisibility(View.VISIBLE);
            deleteChildProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Child Profile");
                    builder.setMessage("You're about to delete all of this child profile's data. This is not reversible.");
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mViewModel.delete(child);
                            if (getActivity() != null) {
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                            }
                        }
                    });
                    builder.setPositiveButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

    private boolean verifyInputs() {
        if (childName.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Missing Field");
            builder.setMessage("You must at least enter a Name");
            builder.setPositiveButton("OK", null);
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }

        return true;
    }

    private void createChildProfile() {
        String imageUriString = "";
        if (imageUri != null) {
            imageUriString = imageUri.toString();
        }
        Child child = new Child(childName.getText().toString(),
                childDob.getText().toString(),
                sexSelector.getSelectedItemPosition(),
                childNotes.getText().toString(),
                imageUriString);
        mViewModel.insert(child);
        if (getActivity() != null) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_save_child) {
            if (verifyInputs()) {
                createChildProfile();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_CAMERA_IMAGE) {
                imageUri = ImageUtils.imageUri;
                childImage.setImageURI(imageUri);
            } else if (requestCode == REQUEST_CODE_GALLERY_IMAGE) {
                imageUri = data.getData();
                childImage.setImageURI(imageUri);
            }
        }
    }
}
