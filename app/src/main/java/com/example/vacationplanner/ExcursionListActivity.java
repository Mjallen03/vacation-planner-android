package com.example.vacationplanner;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacationplanner.data.model.Excursion;
import com.example.vacationplanner.data.model.Vacation;
import com.example.vacationplanner.data.model.VacationDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcursionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);

        ListView listView = findViewById(R.id.excursionListView);

        new Thread(() -> {
            try {
                VacationDatabase db = VacationDatabase.getDatabase(getApplicationContext());

                // Get all excursions
                List<Excursion> excursions = db.excursionDao().getAllExcursions();

                // Get all vacations (map excursion.vacationId → vacation.name)
                List<Vacation> vacations = db.vacationDao().getAllVacations();
                Map<Integer, String> vacationNames = new HashMap<>();
                for (Vacation v : vacations) {
                    vacationNames.put(v.getId(), v.getName());
                }

                runOnUiThread(() -> {
                    if (excursions == null || excursions.isEmpty()) {
                        Toast.makeText(this, "No excursions found.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Display vacation name (if found) or fallback to ID
                    List<String> excursionStrings = excursions.stream()
                            .map(e -> {
                                String vacationName = vacationNames.getOrDefault(
                                        e.getVacationId(), "Vacation ID: " + e.getVacationId()
                                );
                                return "Vacation: " + vacationName +
                                        "\nTitle: " + e.getName() +
                                        "\nDate: " + (e.getDate() != null ? e.getDate() : "N/A") +
                                        "\nPrice: $" + e.getPrice() +
                                        (e.getDescription() != null && !e.getDescription().isEmpty()
                                                ? "\nDescription: " + e.getDescription()
                                                : "");
                            })
                            .collect(Collectors.toList());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_list_item_1,
                            excursionStrings
                    );

                    listView.setAdapter(adapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Error loading excursions: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
