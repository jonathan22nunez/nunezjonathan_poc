package com.example.nunezjonathan_poc.models;

import android.os.Bundle;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "children")
public class Child {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @Ignore
    public String documentId;

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
    public Child(){}

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

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public int getSex() {
        return sex;
    }

    public String getNotes() {
        return notes;
    }

    public String getImageStringUri() {
        return imageStringUri;
    }

    public Bundle getChildBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("_id", _id);
        bundle.putString("documentId", documentId);
        bundle.putString("name", name);
        bundle.putString("dob", dob);
        bundle.putInt("sex", sex);
        bundle.putString("notes", notes);
        bundle.putString("image", imageStringUri);

        return bundle;
    }
}
