package com.example.group9.choresplitter;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by andrewding on 4/20/15.
 */

@SuppressWarnings("serial")
public class Task implements Serializable{
    private String name;
    private int points;
    private MyDate dateCreated;

    public Task(String n, int p) {
        name = n;
        points = p;
    }

    public void createNow() {
        Calendar time = Calendar.getInstance();
        dateCreated = new MyDate(
                        time.get(Calendar.DAY_OF_YEAR),
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE),
                        time.get(Calendar.SECOND));
    }

    public void incrementPoints() {
        points++;
    }

    public void decrementPoints() {
        points--;
    }

    public void setPoints(int p) {
        points = p;
    }

    public void print() {
        System.out.println(name + " " + points);
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public MyDate getDateCreated() {
        return dateCreated;
    }

}
