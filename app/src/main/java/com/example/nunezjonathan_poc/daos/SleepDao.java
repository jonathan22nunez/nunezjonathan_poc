package com.example.nunezjonathan_poc.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Sleep;

import java.util.List;

@Dao
public interface SleepDao {

    @Query("SELECT * FROM sleep_activities")
    List<Sleep> queryAll();

    @Query("SELECT * FROM sleep_activities WHERE child_id IN (:childIds)")
    LiveData<List<Sleep>> queryAllByChildId(long childIds);

    @Insert
    long insertSleep(Sleep sleep);
}
