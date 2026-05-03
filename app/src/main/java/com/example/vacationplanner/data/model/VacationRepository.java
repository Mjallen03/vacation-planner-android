package com.example.vacationplanner.data.model;

import android.content.Context;
import java.util.List;

public class VacationRepository {

    private final VacationDao vacationDao;
    private final ExcursionDao excursionDao;

    public VacationRepository(Context context) {
        VacationDatabase db = VacationDatabase.getDatabase(context);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
    }

    // Insert vacation on a background thread
    public void insertVacation(Vacation vacation) {
        new Thread(() -> vacationDao.insert(vacation)).start();
    }

    // Update vacation on a background thread
    public void updateVacation(Vacation vacation) {
        new Thread(() -> vacationDao.update(vacation)).start();
    }

    // Delete vacation only if it has no excursions
    public boolean deleteVacation(Vacation vacation) {
        final boolean[] canDelete = {false};
        Thread thread = new Thread(() -> {
            List<Excursion> excursions = excursionDao.getExcursionsForVacation(vacation.getId());
            if (excursions == null || excursions.isEmpty()) {
                vacationDao.delete(vacation);
                canDelete[0] = true;
            }
        });
        thread.start();
        try {
            thread.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return canDelete[0];
    }

    // Retrieve all vacations safely off the main thread
    public List<Vacation> getAllVacations() {
        final List<Vacation>[] result = new List[1];
        Thread thread = new Thread(() -> result[0] = vacationDao.getAllVacations());
        thread.start();
        try {
            thread.join(); // Wait for the background query to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }
}
