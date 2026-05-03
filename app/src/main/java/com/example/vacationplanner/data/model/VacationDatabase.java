package com.example.vacationplanner.data.model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Vacation.class, Excursion.class}, version = 4, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {

    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();

    private static volatile VacationDatabase INSTANCE;

    public static VacationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabase.class, "vacation_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
