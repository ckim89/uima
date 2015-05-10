package com.example.group9.choresplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by andrewding on 4/20/15.
 */
public class Member {

    private String userName;
    private int points;

    private List<Task> claimedTasks;
    private List<Task> completedTasks;
    private List<Task> approvedTasks;
    private List<Task> disputedTasks;


    public Member(String u, int p) {
        userName = u;
        points = p;

        claimedTasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        disputedTasks = new ArrayList<Task>();
    }

    public String getUserName() {
        return userName;
    }

    public int getPoints() {
        return points;
    }

    public List<Task> getClaimedTasks() {
        return claimedTasks;
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
    }

    public List<Task> getDisputedTasks() {
        return disputedTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return userName.equals(member.userName);

    }

}
