package com.example.vacationplanner.data.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VacationDao {
    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacation ORDER BY id ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacation WHERE id = :id LIMIT 1")
    Vacation getVacationById(int id);

}
