package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Medication;
import com.example.nunezjonathan_poc.models.Temperature;

import java.util.List;

@Dao
public interface TemperatureDao {

    @Query("SELECT * FROM temperatures")
    List<Temperature> queryAll();

    @Query("SELECT * FROM temperatures WHERE child_id IN (:childIds)")
    List<Temperature> queryAllByChildId(int[] childIds);

    @Insert
    void insertTemperature(Temperature temperature);

}
