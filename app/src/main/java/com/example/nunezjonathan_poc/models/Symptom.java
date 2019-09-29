package com.example.nunezjonathan_poc.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "symptoms",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdSymptom"))
public class Symptom {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public int child_id;

    @ColumnInfo
    public String datetime;

    @ColumnInfo
    public String symptom;

    @ColumnInfo
    public String image_uri;

    public Symptom(int child_id, String datetime, String symptom, String image_uri) {
        this.child_id = child_id;
        this.datetime = datetime;
        this.symptom = symptom;
        this.image_uri = image_uri;
    }

    public int get_id() {
        return _id;
    }

    public int getChild_id() {
        return child_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getSymptom() {
        return symptom;
    }

    public String getImage_uri() {
        return image_uri;
    }
}
