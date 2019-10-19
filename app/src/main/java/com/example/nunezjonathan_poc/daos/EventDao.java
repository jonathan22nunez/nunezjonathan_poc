package com.example.nunezjonathan_poc.daos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM events")
    List<Event> queryAll();

    @Query("SELECT * FROM events WHERE child_id IN (:childIds)")
    LiveData<List<Event>> queryAllByChildId(long childIds);

    @Query("SELECT * FROM events WHERE child_id IN (:childIds)")
    List<Event> queryAllByChildIdForConvert(long childIds);

    @Query("SELECT * FROM events WHERE child_id IN (:childIds) AND event_type IN (:eventType)")
    LiveData<List<Event>> queryAllByChildIdAndType(long childIds, int eventType);

    @Query("SELECT * FROM events WHERE child_id IN (:childIds) AND (event_type IN (:eventType) OR event_type IN (:eventType2))")
    LiveData<List<Event>> queryAllByChildIdAndType(long childIds, int eventType, int eventType2);

    @Query("SELECT * FROM events WHERE child_id IN (:childIds) AND (event_type IN (:eventType) OR event_type IN (:eventType2) OR event_type IN (:eventType3))")
    LiveData<List<Event>> queryAllByChildIdAndType(long childIds, int eventType, int eventType2, int eventType3);

    @Insert
    long insertEvent(Event event);

    @Delete
    int deleteEvent(Event event);
}
