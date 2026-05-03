package com.example.vacationplanner.data.model;

public abstract class BaseItem {

    protected int id;
    protected String title;

    public BaseItem() {}

    public int getId() { return id; }
    public void setId(int id) {this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDisplayLabel() {
        return title;
    }
}
