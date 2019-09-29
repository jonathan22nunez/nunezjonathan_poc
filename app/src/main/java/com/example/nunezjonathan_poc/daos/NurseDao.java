package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Nurse;

import java.util.List;

@Dao
public interface NurseDao {

    @Query("SELECT * FROM nurse_activities")
    List<Nurse> queryAll();

    @Query("SELECT * FROM nurse_activities WHERE child_id IN (:childIds)")
    List<Nurse> queryAllByChildId(int[] childIds);

    @Insert
    void insertNurse(Nurse nurse);
}
