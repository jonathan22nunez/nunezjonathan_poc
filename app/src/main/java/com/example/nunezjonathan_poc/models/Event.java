package com.example.nunezjonathan_poc.models;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Entity(tableName = "events",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdEvent"))
public class Event {

    @IntDef({EventType.SLEEP, EventType.NURSE, EventType.BOTTLE, EventType.WET, EventType.POOPY, EventType.MIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType {
        int SLEEP = 1;
        int NURSE = 2;
        int BOTTLE = 3;
        int WET = 4;
        int POOPY = 5;
        int MIXED = 6;
    }

    @IntDef({Color.NONE, Color.BLACK, Color.YELLOW, Color.BROWN, Color.GREEN, Color.ORANGE, Color.RED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Color {
        int NONE = -1;
        int BLACK = R.drawable.black_poo;
        int YELLOW = R.drawable.yellow_poo;
        int BROWN = R.drawable.brown_poo;
        int GREEN = R.drawable.green_poo;
        int ORANGE = R.drawable.orange_poo;
        int RED = R.drawable.red_poo;
    }

//    @IntDef({Hardness.NONE, Hardness.LOOSE, Hardness.SOFT, Hardness.HARD})
    @StringDef({Hardness.NONE, Hardness.LOOSE, Hardness.SOFT, Hardness.HARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Hardness {
        String NONE = "";
        String LOOSE = "Loose";
        String SOFT = "Soft";
        String HARD = "Hard";
    }

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo(name = "child_id")
    public long childId;

    @ColumnInfo(name = "event_type")
    public int eventType;

    @ColumnInfo
    public String datetime;

    //Sleep & Feeding

    @ColumnInfo
    public long duration;

    //Feeding

    @ColumnInfo(name = "left_side_duration")
    public long leftSideDuration;

    @ColumnInfo(name = "right_side_duration")
    public long rightSideDuration;

    @ColumnInfo(name = "start_amount")
    public double startAmount;

    @ColumnInfo(name = "end_amount")
    public double endAmount;

    //Diaper

    @ColumnInfo
    public int color;

    @ColumnInfo
    public String hardness;

    @Ignore
    public Event() { }

    public Event(long childId, @EventType int eventType, String datetime, long duration, long leftSideDuration,
                 long rightSideDuration, double startAmount, double endAmount,
                 @Color int color, @Hardness String hardness) {
        this.childId = childId;
        this.eventType = eventType;
        this.datetime = datetime;
        this.duration = duration;
        this.leftSideDuration = leftSideDuration;
        this.rightSideDuration = rightSideDuration;
        this.startAmount = startAmount;
        this.endAmount = endAmount;
        this.color = color;
        this.hardness = hardness;
    }

    @NonNull
    @Override
    public String toString() {
        switch (eventType) {
            case EventType.SLEEP:
                return "Sleep @ " + CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(datetime));
            case EventType.NURSE:
            case EventType.BOTTLE:
                return "Feeding @ " + CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(datetime));
            case EventType.WET:
            case EventType.POOPY:
            case EventType.MIXED:
                return "Diaper Change @ " + CalendarUtils.toTimeHMString(CalendarUtils.stringToCalendar(datetime));
        }

        return super.toString();
    }
}
