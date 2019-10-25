package com.example.nunezjonathan_poc.models;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

@Entity(tableName = "health_events",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdHealth"))
public class Health {

    @IntDef({HealthType.SYMPTOM, HealthType.MEDICATION, HealthType.TEMPERATURE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HealthType {
        int SYMPTOM = 1;
        int MEDICATION = 2;
        int TEMPERATURE = 3;
    }

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo(name = "child_id")
    public long childId;

    @Ignore
    public String childDocumentId;

    @Ignore
    public String documentId;

    @ColumnInfo(name = "health_type")
    public int healthType;

    @ColumnInfo
    public String datetime;

    @ColumnInfo
    public String notes;

    //Symptom Event

    @ColumnInfo
    public String symptom;

    @ColumnInfo(name = "image_uris)")
    public ArrayList<String> imageURIs;

    //Medication Event

    @ColumnInfo(name = "drug_name")
    public String drugName;

    @ColumnInfo(name = "brand_name")
    public String brandName;

    @ColumnInfo
    public double dosage;

    @ColumnInfo
    public String unit;

    //Temperature Event

    @ColumnInfo
    public double temperature;

    @Ignore
    public Health() {
    }

    public Health(@HealthType int healthType, String datetime, String notes, String symptom,
                  ArrayList<String> imageURIs, String drugName, String brandName, double dosage, String unit,
                  double temperature) {
        this.healthType = healthType;
        this.datetime = datetime;
        this.notes = notes;
        this.symptom = symptom;
        this.imageURIs = imageURIs;
        this.drugName = drugName;
        this.brandName = brandName;
        this.dosage = dosage;
        this.unit = unit;
        this.temperature = temperature;
    }


    @NonNull
    @Override
    public String toString() {
        switch (healthType) {
            case HealthType.SYMPTOM:
                if (symptom.isEmpty()) {
                    return "Symptom";
                } else {
                    return symptom;
                }
            case HealthType.MEDICATION:
                if (drugName.isEmpty()) {
                    return "Medication";
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Took ");
                    sb.append(drugName);

                    if (dosage > 0) {
                        sb.append(" : ");
                        sb.append(dosage);
                        if (unit != null) {
                            sb.append(unit);
                        }
                    }

                    return sb.toString();
                }
            case HealthType.TEMPERATURE:
                return "Temperature was " + temperature + "F";
        }

        return super.toString();
    }
}
