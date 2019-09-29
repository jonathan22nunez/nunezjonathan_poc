package com.example.nunezjonathan_poc.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "nurse_activities",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdNurse"))
public class Nurse {

    @PrimaryKey
    public int _id;

    @ColumnInfo
    public int child_id;

    @ColumnInfo
    public String start_datetime;

    @ColumnInfo
    public String end_datetime;

    @ColumnInfo
    public int left_side_duration;

    @ColumnInfo
    public int right_side_duration;

    public Nurse(int child_id, String start_datetime, String end_datetime, int left_side_duration, int right_side_duration) {
        this.child_id = child_id;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
        this.left_side_duration = left_side_duration;
        this.right_side_duration = right_side_duration;
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

    public int getLeft_side_duration() {
        return left_side_duration;
    }

    public int getRight_side_duration() {
        return right_side_duration;
    }
}
