package com.example.group9.choresplitter;

import java.util.Calendar;

/**
 * Created by andrewding on 4/20/15.
 */
public class Task {
    private String name;
    private int points;
    private Calendar timeCreated;

    public Task(String n, int p) {
        name = n;
        points = p;
        timeCreated = Calendar.getInstance();
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public Calendar getTimeCreated() {
        return timeCreated;
    }
}
