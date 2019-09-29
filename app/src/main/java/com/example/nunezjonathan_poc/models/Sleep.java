package com.example.nunezjonathan_poc.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.nunezjonathan_poc.utils.CalendarUtils;

import java.util.Calendar;

@Entity (tableName = "sleep_activities",
        foreignKeys = @ForeignKey(entity = Child.class,
                parentColumns = "_id",
                childColumns = "child_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "child_id", name = "childIdSleep"))
public class Sleep {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo
    public long child_id;

    @ColumnInfo
    public String start_datetime;

    @ColumnInfo
    public long duration;

    public Sleep(long child_id, String start_datetime, long duration) {
        this.child_id = child_id;
        this.start_datetime = start_datetime;
        this.duration = duration;
    }

    public String getEndDatetime() {
        Calendar start = CalendarUtils.stringToCalendar(start_datetime);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(start.getTimeInMillis() + duration);
        return CalendarUtils.toTimeHMSString(end);
    }
}
