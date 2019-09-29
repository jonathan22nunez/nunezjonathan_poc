package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.WetDiaper;

import java.util.List;

@Dao
public interface WetDiaperDao {

    @Query("SELECT * FROM wet_diaper_activities")
    List<WetDiaper> queryAll();

    @Query("SELECT * FROM wet_diaper_activities WHERE child_id IN (:childIds)")
    List<WetDiaper> queryAllByChildId(int[] childIds);

    @Insert
    void insertWetDiaper(WetDiaper wetDiaper);
}
