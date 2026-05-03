package com.example.vacationplanner;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vacationplanner.data.model.Vacation;
import com.example.vacationplanner.data.model.VacationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationActivity extends AppCompatActivity {

    private EditText nameInput, hotelInput, locationInput, startDateInput, endDateInput;
    private Button addButton, updateButton, deleteButton, listButton, shareButton,
            viewExcursionsButton, manageExcursionsButton, vacationDetailsButton;
    private VacationRepository repository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation);

        repository = new VacationRepository(this);

        // --- Input fields ---
        nameInput = findViewById(R.id.nameInput);
        hotelInput = findViewById(R.id.hotelInput);
        locationInput = findViewById(R.id.locationInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);

        // --- Buttons ---
        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        listButton = findViewById(R.id.listButton);
        shareButton = findViewById(R.id.shareButton);
        viewExcursionsButton = findViewById(R.id.viewExcursionsButton);
        manageExcursionsButton = findViewById(R.id.excursionButton);
        vacationDetailsButton = findViewById(R.id.vacationDetailsButton);

        // --- Set button listeners ---
        addButton.setOnClickListener(v -> addVacation());
        updateButton.setOnClickListener(v -> updateVacation());
        deleteButton.setOnClickListener(v -> deleteVacation());
        listButton.setOnClickListener(v -> listVacations());
        shareButton.setOnClickListener(v -> shareVacation());
        viewExcursionsButton.setOnClickListener(v -> openExcursionList());
        manageExcursionsButton.setOnClickListener(v -> openExcursionManager());
        vacationDetailsButton.setOnClickListener(v -> openVacationDetail());
    }

    // --- Vacation CRUD operations ---

    private void addVacation() {
        if (!validateDates()) return;

        Vacation vacation = new Vacation();
        vacation.setName(SecurityUtil.sanitize(nameInput.getText().toString()));
        vacation.setHotel(SecurityUtil.sanitize(hotelInput.getText().toString()));
        vacation.setLocation(SecurityUtil.sanitize(locationInput.getText().toString()));
        vacation.setStartDate(startDateInput.getText().toString());
        vacation.setEndDate(endDateInput.getText().toString());

        repository.insertVacation(vacation);

        // Schedule alerts for start and end dates
        scheduleAlert(vacation.getName(), vacation.getStartDate(), "starting");
        scheduleAlert(vacation.getName(), vacation.getEndDate(), "ending");

        Toast.makeText(this, "Vacation added and alerts set!", Toast.LENGTH_SHORT).show();
        clearInputs();
    }

    private void updateVacation() {
        if (!validateDates()) return;

        List<Vacation> vacations = repository.getAllVacations();
        if (vacations.isEmpty()) {
            Toast.makeText(this, "No vacation to update.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vacation v = vacations.get(0);
        v.setName(SecurityUtil.sanitize(nameInput.getText().toString()));
        v.setHotel(SecurityUtil.sanitize(hotelInput.getText().toString()));
        v.setLocation(SecurityUtil.sanitize(locationInput.getText().toString()));
        v.setStartDate(startDateInput.getText().toString());
        v.setEndDate(endDateInput.getText().toString());

        repository.updateVacation(v);
        Toast.makeText(this, "Vacation updated!", Toast.LENGTH_SHORT).show();
    }

    private void deleteVacation() {
        List<Vacation> vacations = repository.getAllVacations();
        if (!vacations.isEmpty()) {
            Vacation v = vacations.get(0);
            boolean deleted = repository.deleteVacation(v);
            if (deleted)
                Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Cannot delete: vacation has excursions.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No vacations to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void listVacations() {
        List<Vacation> vacations = repository.getAllVacations();
        if (vacations.isEmpty()) {
            Toast.makeText(this, "No vacations found.", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder sb = new StringBuilder();
            for (Vacation v : vacations) {
                sb.append(v.getName()).append(" | ")
                        .append(v.getHotel()).append(" | ")
                        .append(v.getLocation()).append("\n")
                        .append(v.getStartDate()).append(" → ").append(v.getEndDate()).append("\n\n");
            }
            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void shareVacation() {
        String title = nameInput.getText().toString();
        String hotel = hotelInput.getText().toString();
        String location = locationInput.getText().toString();
        String start = startDateInput.getText().toString();
        String end = endDateInput.getText().toString();

        if (title.isEmpty() || start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "Please fill in all details before sharing.", Toast.LENGTH_SHORT).show();
            return;
        }

        String shareText = "Vacation Details:\n" +
                "Title: " + title + "\n" +
                "Hotel: " + hotel + "\n" +
                "Location: " + location + "\n" +
                "Start Date: " + start + "\n" +
                "End Date: " + end + "\n";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Vacation Plan: " + title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(shareIntent, "Share Vacation Details"));
    }

    // --- Excursion navigation ---

    private void openExcursionList() {
        Intent intent = new Intent(this, ExcursionListActivity.class);
        List<Vacation> vacations = repository.getAllVacations();
        if (!vacations.isEmpty()) {
            intent.putExtra("vacationId", vacations.get(0).getId());
        } else {
            intent.putExtra("vacationId", 1);
        }
        startActivity(intent);
    }

    private void openExcursionManager() {
        Intent intent = new Intent(this, ExcursionActivity.class);
        startActivity(intent);
    }

    // --- New Vacation Detail Navigation ---

    private void openVacationDetail() {
        List<Vacation> vacations = repository.getAllVacations();

        if (vacations.isEmpty()) {
            Toast.makeText(this, "No vacations available to view.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vacation selectedVacation = vacations.get(0);

        Intent intent = new Intent(this, VacationDetailActivity.class);
        intent.putExtra("vacationId", selectedVacation.getId());
        startActivity(intent);
    }

    // --- Helpers ---

    private boolean validateDates() {
        String startStr = startDateInput.getText().toString().trim();
        String endStr = endDateInput.getText().toString().trim();

        try {
            Date start = dateFormat.parse(startStr);
            Date end = dateFormat.parse(endStr);
            if (start == null || end == null) throw new ParseException("Null", 0);
            if (end.before(start)) {
                Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearInputs() {
        nameInput.setText("");
        hotelInput.setText("");
        locationInput.setText("");
        startDateInput.setText("");
        endDateInput.setText("");
    }

    private void scheduleAlert(String title, String dateStr, String type) {
        try {
            Date date = dateFormat.parse(dateStr);
            if (date == null) return;

            long triggerTime = date.getTime();

            Intent intent = new Intent(this, VacationAlertReceiver.class);
            intent.putExtra("vacationTitle", title);
            intent.putExtra("type", type);

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

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
