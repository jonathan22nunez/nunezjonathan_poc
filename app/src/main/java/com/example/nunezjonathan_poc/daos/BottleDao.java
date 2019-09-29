package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Bottle;

import java.util.List;

@Dao
public interface BottleDao {

    @Query("SELECT * FROM bottle_activities")
    List<Bottle> queryAll();

    @Query("SELECT * FROM bottle_activities WHERE child_id IN (:childIds)")
    List<Bottle> queryAllByChildId(int[] childIds);

    @Insert
    void insertBottle(Bottle bottle);
}
