package com.example.nunezjonathan_poc.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "soiled_diaper_activities",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childId"))
public class SoiledDiaper {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public int child_id;

    @ColumnInfo
    public String datetime;

    @ColumnInfo
    public boolean isWet;

    @ColumnInfo
    public String color;

    @ColumnInfo
    public String hardness;

    public SoiledDiaper(int child_id, String datetime, boolean isWet, String color, String hardness) {
        this.child_id = child_id;
        this.datetime = datetime;
        this.isWet = isWet;
        this.color = color;
        this.hardness = hardness;
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

    public boolean isWet() {
        return isWet;
    }

    public String getColor() {
        return color;
    }

    public String getHardness() {
        return hardness;
    }
}
