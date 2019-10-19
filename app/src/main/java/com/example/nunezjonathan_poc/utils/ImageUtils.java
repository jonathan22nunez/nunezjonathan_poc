package com.example.nunezjonathan_poc.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    private static final int REQUEST_CODE_CAMERA_IMAGE = 101;
    private static final int REQUEST_CODE_GALLERY_IMAGE = 102;
    private static final String[] dialogOptions = {"Camera", "Gallery"};
    private static final String MIME_TYPE = "image/*";
    private static final String JPEG_MIME = "image/jpeg";
    private static final String PNG_MIME = "image/png";

    private static File profileImagePath;
    public static Uri imageUri;

    public static void getImageFromIntent(final Fragment fragment) {
        final Context context = fragment.getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Get photo from");
        builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context != null) {
                    if (which == 0) {
                        startCameraIntent(fragment);
                    } else if (which == 1) {
                        startGalleryIntent(fragment);
                    }
                }
            }
        });
        builder.setPositiveButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void startCameraIntent(Fragment fragment) {
        if (fragment.getContext() != null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
                File imageFile = null;
                try {
                    imageFile = createImageFile(fragment.getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (imageFile != null) {
                    imageUri = FileProvider.getUriForFile(fragment.getContext(),
                            "com.example.nunezjonathan_poc.fileprovider",
                            imageFile);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    fragment.startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA_IMAGE);
                }
            }
        }
    }

    private static void startGalleryIntent(Fragment fragment) {
        if (fragment.getContext() != null) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType(MIME_TYPE);
            String[] mimeTypes = {JPEG_MIME, PNG_MIME};
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            if (galleryIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
                fragment.startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY_IMAGE);
            }
        }
    }

    private static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        profileImagePath = image.getAbsoluteFile();
        return image;
    }
}
