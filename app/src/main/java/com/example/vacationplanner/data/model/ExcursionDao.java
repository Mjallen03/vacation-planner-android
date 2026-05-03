package com.example.vacationplanner.data.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExcursionDao {

    @Query("SELECT * FROM excursion WHERE vacationId = :vacationId")
    List<Excursion> getExcursionsForVacation(int vacationId);

    @Query("SELECT * FROM excursion")
    List<Excursion> getAllExcursions();

    @Insert
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);
}
