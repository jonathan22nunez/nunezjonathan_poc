package com.example.nunezjonathan_poc.models;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
    public final long childId;

    @ColumnInfo(name = "health_type")
    public final int healthType;

    @ColumnInfo
    public final String datetime;

    @ColumnInfo
    public final String notes;

    //Symptom Event

    @ColumnInfo
    public final String symptom;

    @ColumnInfo(name = "image1")
    public final String image1_uri;

    @ColumnInfo(name = "image2")
    public final String image2_uri;

    @ColumnInfo(name = "image3")
    public final String image3_uri;

    @ColumnInfo(name = "image4")
    public final String image4_uri;

    @ColumnInfo(name = "image5")
    public final String image5_uri;

    //Medication Event

    @ColumnInfo(name = "drug_name")
    public final String drugName;

    @ColumnInfo(name = "brand_name")
    public final String brandName;

    @ColumnInfo
    public final double dosage;

    @ColumnInfo
    public final String unit;

    //Temperature Event

    @ColumnInfo
    public final double temperature;

    public Health(long childId, @HealthType int healthType, String datetime, String notes, String symptom,
                  String image1_uri, String image2_uri, String image3_uri, String image4_uri,
                  String image5_uri, String drugName, String brandName, double dosage, String unit,
                  double temperature) {
        this.childId = childId;
        this.healthType = healthType;
        this.datetime = datetime;
        this.notes = notes;
        this.symptom = symptom;
        this.image1_uri = image1_uri;
        this.image2_uri = image2_uri;
        this.image3_uri = image3_uri;
        this.image4_uri = image4_uri;
        this.image5_uri = image5_uri;
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
                return symptom + " @ " + CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(datetime));
            case HealthType.MEDICATION:
                return "Took " + drugName + " @ " + CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(datetime));
            case HealthType.TEMPERATURE:
                return "Temperature was " + temperature + "F @ " + CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(datetime));
        }

        return super.toString();
    }
}
