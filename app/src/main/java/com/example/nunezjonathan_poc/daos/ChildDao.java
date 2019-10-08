package com.example.nunezjonathan_poc.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nunezjonathan_poc.models.Child;

import java.util.List;

@Dao
public interface ChildDao {

    @Query("SELECT * FROM children")
    List<Child> queryAllForConvert();

    @Query("SELECT * FROM children")
    LiveData<List<Child>> queryAll();

    @Query("SELECT * FROM children WHERE _id IN (:childIds)")
    List<Child> queryAllById(int[] childIds);

    @Query("SELECT * FROM children WHERE name LIKE :name LIMIT 1")
    Child findByName(String name);

    @Insert
    long insertChild(Child child);

    @Insert
    void insertAll(Child... children);

    @Delete
    int delete(Child... children);
}