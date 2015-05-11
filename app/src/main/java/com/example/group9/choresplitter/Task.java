package com.example.group9.choresplitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by andrewding on 4/20/15.
 */

public class Task implements Serializable{
    private String name;
    private String id;
    private int points;
    private MyDate dateCreated;
    private MyDate dateCompleted;
    private int upVotes;
    private int allUsers;
    private ArrayList<String> approval;

    public Task(String n, int p) {
        name = n;
        points = p;
        approval = new ArrayList<String>();
    }

    public void createNow() {
        Calendar time = Calendar.getInstance();
        dateCreated = new MyDate(
                        time.get(Calendar.DAY_OF_YEAR),
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE),
                        time.get(Calendar.SECOND));
    }

    public void completeNow() {
        Calendar time = Calendar.getInstance();
        dateCompleted = new MyDate(
                time.get(Calendar.MONTH) + 1,
                time.get(Calendar.DAY_OF_MONTH),
                time.get(Calendar.YEAR));
    }

    public void createNow(int day, int hour, int minute, int seconds) {
        dateCreated = new MyDate(day, hour, minute, seconds);
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

    public void setId(String a) {
        id = a;
    }

    public void setUpVotes(int p) {
        upVotes = p;
    }

    public void setAllUsers(int a) {
        allUsers = a;
    }

    public void print() {
        System.out.println(name + " " + points);
    }

    public void approvedBy(String m) {
        if (!approval.contains(m)) {
            approval.add(m);
        }
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

    public MyDate getDateCompleted() {
        return dateCompleted;
    }

    public ArrayList<String> getApprovedBy() {
        return approval;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getAllUsers() {
        return allUsers;
    }

    public String getId() {
        return id;
    }

    public void removeUser(String u) {
        if (approval.contains(u)) {
            approval.remove(u);
        }
    }

}
