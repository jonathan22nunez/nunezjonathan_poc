package com.example.nunezjonathan_poc.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "medications",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdMedication"))
public class Medication {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public int child_id;

    @ColumnInfo
    public String datetime;

    @ColumnInfo
    public String drug_name;

    @ColumnInfo
    public String brand;

    public Medication(int child_id, String datetime, String drug_name, String brand) {
        this.child_id = child_id;
        this.datetime = datetime;
        this.drug_name = drug_name;
        this.brand = brand;
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

    public String getDrug_name() {
        return drug_name;
    }

    public String getBrand() {
        return brand;
    }
}
