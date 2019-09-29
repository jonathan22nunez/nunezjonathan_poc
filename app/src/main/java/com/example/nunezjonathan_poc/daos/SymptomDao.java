package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Symptom;

import java.util.List;

@Dao
public interface SymptomDao {

    @Query("SELECT * FROM symptoms")
    List<Symptom> queryAll();

    @Query("SELECT * FROM symptoms WHERE child_id IN (:childIds)")
    List<Symptom> queryAllByChildId(int[] childIds);

    @Insert
    void insertSymptom(Symptom symptom);
}
