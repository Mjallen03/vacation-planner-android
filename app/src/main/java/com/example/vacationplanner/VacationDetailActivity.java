package com.example.vacationplanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacationplanner.data.model.Vacation;
import com.example.vacationplanner.data.model.VacationDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class VacationDetailActivity extends AppCompatActivity {

    private TextView vacationNameTextView;
    private EditText searchInput;
    private Button searchButton;

    private VacationDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        // Connect XML views
        vacationNameTextView = findViewById(R.id.vacationNameTextView);
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);

        db = VacationDatabase.getDatabase(getApplicationContext());

        // Load ALL vacations at start
        loadAllVacations();

        // Set search button action
        searchButton.setOnClickListener(v -> performSearch());
    }
    private void loadAllVacations() {
        new Thread(() -> {
            List<Vacation> vacations = db.vacationDao().getAllVacations();

            runOnUiThread(() -> {
                if (vacations == null || vacations.isEmpty()) {
                    vacationNameTextView.setText("No vacations found.");
                    return;
                }

                vacationNameTextView.setText(formatVacations(vacations));
            });
        }).start();
    }

    private void performSearch() {
        String text = searchInput.getText().toString().trim().toLowerCase();

        new Thread(() -> {
            List<Vacation> vacations = db.vacationDao().getAllVacations();

            // Filter results based on name, hotel, or location
            List<Vacation> filtered = vacations.stream()
                    .filter(v ->
                            v.getName().toLowerCase().contains(text) ||
                                    v.getHotel().toLowerCase().contains(text) ||
                                    v.getLocation().toLowerCase().contains(text)
                    )
                    .collect(Collectors.toList());

            runOnUiThread(() -> {
                if (filtered.isEmpty()) {
                    vacationNameTextView.setText("No results found.");
                } else {
                    vacationNameTextView.setText(formatVacations(filtered));
                }
            });
        }).start();
    }

    private String formatVacations(List<Vacation> vacations) {

        // Generate timestamp
        @SuppressLint("SimpleDateFormat") String timestamp = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a")
                .format(new java.util.Date());

        StringBuilder builder = new StringBuilder();

        // Title + timestamp
        builder.append("Vacation Report\n");
        builder.append("Generated: ").append(timestamp).append("\n\n");

        for (Vacation v : vacations) {
            builder.append("Vacation ID: ").append(v.getId()).append("\n")
                    .append("Name: ").append(v.getName()).append("\n")
                    .append("Hotel: ").append(v.getHotel()).append("\n")
                    .append("Location: ").append(v.getLocation()).append("\n")
                    .append("Dates: ")
                    .append(v.getStartDate() != null ? v.getStartDate() : "N/A")
                    .append(" - ")
                    .append(v.getEndDate() != null ? v.getEndDate() : "N/A")
                    .append("\n\n");
        }
        return builder.toString();
    }
}
