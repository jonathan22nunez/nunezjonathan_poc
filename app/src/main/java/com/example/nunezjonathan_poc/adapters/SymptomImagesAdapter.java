package com.example.nunezjonathan_poc.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.nunezjonathan_poc.R;

import java.util.ArrayList;

public class SymptomImagesAdapter extends BaseAdapter {

    private static final long BASE_ID = 0x072793259;

    private ArrayList<String> imageUris;

    public SymptomImagesAdapter(ArrayList<String> imageUris) {
        this.imageUris = imageUris;
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public Object getItem(int position) {
        if (imageUris.size() > 0 && position < imageUris.size()) {
            return imageUris.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return BASE_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom_image_item, parent, false);
        }

        String imageUri = (String) getItem(position);

        ImageView iv = convertView.findViewById(R.id.imageView);
        iv.setImageURI(Uri.parse(imageUri));

        return convertView;
    }
}
