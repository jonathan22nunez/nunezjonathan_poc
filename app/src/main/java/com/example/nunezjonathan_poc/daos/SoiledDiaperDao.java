package com.example.nunezjonathan_poc.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.SoiledDiaper;

import java.util.List;

@Dao
public interface SoiledDiaperDao {

    @Query("SELECT * FROM soiled_diaper_activities")
    List<SoiledDiaper> queryAll();

    @Query("SELECT * FROM soiled_diaper_activities WHERE child_id IN (:childIds)")
    List<SoiledDiaper> queryAllByChildId(int[] childIds);

    @Insert
    void insertSoiledDiaper(SoiledDiaper soiledDiaper);
}
