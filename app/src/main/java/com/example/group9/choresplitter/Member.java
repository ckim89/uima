package com.example.group9.choresplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by andrewding on 4/20/15.
 */
public class Member {
    private double userId;

    private String name;
    private int points;
    private double completionRate;
    private double freeloaderRate;

    private List<Task> claimedTasks;
    private List<Task> completedTasks;
    private List<Task> approvedTasks;
    private List<Task> disputedTasks;

    //TODO: uh how do i associate picture with member?

    public Member(String n) {
        Random rand = new Random();
        userId = rand.nextLong();
        name = n;
        points = 0;
        completionRate = 0;
        freeloaderRate = 0;

        claimedTasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        approvedTasks = new ArrayList<Task>();
        disputedTasks = new ArrayList<Task>();
    }

    public double getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public double getFreeloaderRate() {
        return freeloaderRate;
    }

    public List<Task> getClaimedTasks() {
        return claimedTasks;
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }

    public List<Task> getApprovedTasks() {
        return approvedTasks;
    }

    public List<Task> getDisputedTasks() {
        return disputedTasks;
    }
}
