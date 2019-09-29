package com.example.nunezjonathan_poc.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "wet_diaper_activities",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdWetDiaper"))
public class WetDiaper {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public int child_id;

    @ColumnInfo
    public String datetime;

    public WetDiaper(int child_id, String datetime) {
        this.child_id = child_id;
        this.datetime = datetime;
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
}
