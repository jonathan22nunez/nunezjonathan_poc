package com.example.nunezjonathan_poc.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "children")
public class Child {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String dob;

    @ColumnInfo
    public int sex;

    @ColumnInfo
    public String notes;

    @ColumnInfo(name = "image")
    public String imageStringUri;

    @Ignore
    public Child(String name) {
        this.name = name;
    }

    public Child(String name, String dob, int sex, String notes, String imageStringUri) {
        this.name = name;
        this.dob = dob;
        this.sex = sex;
        this.notes = notes;
        this.imageStringUri = imageStringUri;
    }

    public Bundle getChildBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("dob", dob);
        bundle.putInt("sex", sex);
        bundle.putString("notes", notes);
        bundle.putString("image", imageStringUri);

        return bundle;
    }

    public void makeCurrentlySelected(Activity activity) {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("currentChild", Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong("childId", _id).apply();
        Toast.makeText(activity, "Current child is now... " + name, Toast.LENGTH_SHORT).show();
    }
}
