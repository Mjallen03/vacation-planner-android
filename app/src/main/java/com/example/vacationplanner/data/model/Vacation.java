package com.example.vacationplanner.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacation")
public class Vacation extends BaseItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;        // title
    private String hotel;
    private String location;
    private String startDate;
    private String endDate;

    // --- Getters and Setters ---
    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    @Override
    public String getTitle() { return name; }

    @Override
    public void setTitle(String title) { this.name = title; }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        this.title = title;
    }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    // Polymorphism override
    @Override
    public String getDisplayLabel() {
        return "Vacation: " + name;
    }
}
