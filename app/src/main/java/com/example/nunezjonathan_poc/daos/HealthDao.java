package com.example.nunezjonathan_poc.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Health;

import java.util.List;

@Dao
public interface HealthDao {

    @Query("SELECT * FROM health_events")
    List<Health> queryAll();

    @Query("SELECT * FROM health_events WHERE child_id IN (:childIds)")
    LiveData<List<Health>> queryAllByChildId(long childIds);

    @Query("SELECT * FROM health_events WHERE child_id IN (:childIds)")
    List<Health> queryAllByChildIdForConvert(long childIds);

    @Insert
    long insertHealth(Health health);

    @Delete
    int deleteHealth(Health health);
}
