package com.example.nunezjonathan_poc.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "bottle_activities",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdBottle"))
public class Bottle {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public int child_id;

    @ColumnInfo
    public String start_datetime;

    @ColumnInfo
    public String end_datetime;

    @ColumnInfo
    public double start_amount;

    @ColumnInfo
    public double end_amount;

    public Bottle(int child_id, String start_datetime, String end_datetime, double start_amount, double end_amount) {
        this.child_id = child_id;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
        this.start_amount = start_amount;
        this.end_amount = end_amount;
    }

    public int get_id() {
        return _id;
    }

    public int getChild_id() {
        return child_id;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public double getStart_amount() {
        return start_amount;
    }

    public double getEnd_amount() {
        return end_amount;
    }
}
