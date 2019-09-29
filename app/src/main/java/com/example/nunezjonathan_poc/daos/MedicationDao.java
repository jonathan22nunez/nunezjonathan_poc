package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Medication;

import java.util.List;

@Dao
public interface MedicationDao {

    @Query("SELECT * FROM medications")
    List<Medication> queryAll();

    @Query("SELECT * FROM medications WHERE child_id IN (:childIds)")
    List<Medication> queryAllByChildId(int[] childIds);

    @Insert
    void insertMedication(Medication medication);
}
