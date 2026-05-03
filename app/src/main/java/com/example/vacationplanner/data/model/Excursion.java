package com.example.vacationplanner.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "excursion",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "id",
                childColumns = "vacationId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Excursion extends BaseItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int vacationId;
    private String name;
    private String description;
    private double price;
    private String date;

    // --- Constructor ---
    public Excursion(int vacationId, String name, String description, double price, String date) {
        this.vacationId = vacationId;
        this.name = name;
        this.title = name;
        this.description = description;
        this.price = price;
        this.date = date;
    }

    // --- Empty constructor for Room ---
    public Excursion() {}

    // --- Getters and Setters ---
    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    public int getVacationId() { return vacationId; }
    public void setVacationId(int vacationId) { this.vacationId = vacationId; }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        this.title = name;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // Polymorphism override
    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public void setTitle(String title) {
        this.name = title;
        this.title = title;
    }

    @Override
    public String getDisplayLabel() {
        return "Excursion: " + name;
    }
}
