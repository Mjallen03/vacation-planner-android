package com.example.vacationplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacationplanner.data.model.Excursion;
import com.example.vacationplanner.data.model.Vacation;
import com.example.vacationplanner.data.model.VacationDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionActivity extends AppCompatActivity {

    private EditText vacationIdInput, nameInput, descInput, priceInput, dateInput;
    private Button addBtn, updateBtn, deleteBtn, listBtn;
    private VacationDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion);

        db = VacationDatabase.getDatabase(getApplicationContext());

        vacationIdInput = findViewById(R.id.vacationIdInput);
        nameInput = findViewById(R.id.nameInput);
        descInput = findViewById(R.id.descInput);
        priceInput = findViewById(R.id.priceInput);
        dateInput = findViewById(R.id.dateInput);
        addBtn = findViewById(R.id.addBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        listBtn = findViewById(R.id.listBtn);

        addBtn.setOnClickListener(v -> addExcursion());
        updateBtn.setOnClickListener(v -> updateExcursion());
        deleteBtn.setOnClickListener(v -> deleteExcursion());
        listBtn.setOnClickListener(v -> listExcursions());
    }

    private void addExcursion() {
        new Thread(() -> {
            try {
                String vacationIdText = vacationIdInput.getText().toString().trim();
                String name = nameInput.getText().toString().trim();
                String desc = descInput.getText().toString().trim();
                String priceText = priceInput.getText().toString().trim();
                String date = dateInput.getText().toString().trim();

                if (vacationIdText.isEmpty() || name.isEmpty() || desc.isEmpty() || priceText.isEmpty() || date.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show());
                    return;
                }

                if (!isValidDate(date)) {
                    runOnUiThread(() -> Toast.makeText(this, "Date must be in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show());
                    return;
                }

                int vacationId = Integer.parseInt(vacationIdText);
                double price = Double.parseDouble(priceText);

                // Validate excursion date falls within vacation period
                Vacation vacation = db.vacationDao().getVacationById(vacationId);
                if (vacation != null) {
                    Date start = dateFormat.parse(vacation.getStartDate());
                    Date end = dateFormat.parse(vacation.getEndDate());
                    Date excursionDate = dateFormat.parse(date);

                    if (excursionDate.before(start) || excursionDate.after(end)) {
                        runOnUiThread(() ->
                                Toast.makeText(this, "Excursion date must fall within the vacation period.", Toast.LENGTH_SHORT).show());
                        return;
                    }
                }

                Excursion e = new Excursion(vacationId, name, desc, price, date);
                db.excursionDao().insert(e);

                // Schedule alert on excursion date
                scheduleExcursionAlert(name, date);

                runOnUiThread(() ->
                        Toast.makeText(this, "Excursion added successfully and alert set!", Toast.LENGTH_SHORT).show());

            } catch (Exception ex) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error adding excursion: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateExcursion() {
        new Thread(() -> {
            try {
                String name = nameInput.getText().toString().trim();
                String desc = descInput.getText().toString().trim();
                String priceText = priceInput.getText().toString().trim();
                String date = dateInput.getText().toString().trim();

                if (name.isEmpty() || desc.isEmpty() || priceText.isEmpty() || date.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "Please fill in all fields before updating.", Toast.LENGTH_SHORT).show());
                    return;
                }

                if (!isValidDate(date)) {
                    runOnUiThread(() -> Toast.makeText(this, "Date must be in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show());
                    return;
                }

                double price = Double.parseDouble(priceText);

                List<Excursion> excursions = db.excursionDao().getAllExcursions();
                if (excursions.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(this, "No excursions available to update.", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Update first excursion (for demo)
                Excursion e = excursions.get(0);
                e.setName(name);
                e.setDescription(desc);
                e.setPrice(price);
                e.setDate(date);
                db.excursionDao().update(e);

                // Reschedule alert
                scheduleExcursionAlert(name, date);

                runOnUiThread(() -> Toast.makeText(this, "Excursion updated and alert reset!", Toast.LENGTH_SHORT).show());

            } catch (Exception ex) {
                runOnUiThread(() -> Toast.makeText(this, "Error updating excursion: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void deleteExcursion() {
        new Thread(() -> {
            List<Excursion> excursions = db.excursionDao().getAllExcursions();
            if (!excursions.isEmpty()) {
                db.excursionDao().delete(excursions.get(0));
                runOnUiThread(() -> Toast.makeText(this, "Excursion deleted successfully.", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(this, "No excursions to delete.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void listExcursions() {
        new Thread(() -> {
            List<Excursion> excursions = db.excursionDao().getAllExcursions();
            runOnUiThread(() -> {
                if (excursions.isEmpty()) {
                    Toast.makeText(this, "No excursions found.", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Excursion e : excursions) {
                        sb.append("Title: ").append(e.getName())
                                .append("\nDate: ").append(e.getDate())
                                .append("\nPrice: $").append(e.getPrice())
                                .append("\nDescription: ").append(e.getDescription())
                                .append("\n\n");
                    }
                    Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    // --- Validation Helpers ---
    private boolean isValidDate(String dateStr) {
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Excursion Alert ---
    private void scheduleExcursionAlert(String title, String dateStr) {
        try {
            Date date = dateFormat.parse(dateStr);
            if (date == null) return;

            long triggerTime = date.getTime();

            Intent intent = new Intent(this, ExcursionAlertReceiver.class);
            intent.putExtra("excursionTitle", title);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    (int) System.currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
