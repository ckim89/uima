package com.example.group9.choresplitter;

/**
 * Created by andrewding on 4/18/15.
 */
public class MemberListItem {
    private String name;
    private int points;
    private int imageId;

    public MemberListItem(String n, int p, int i) {
        name = n;
        points = p;
        imageId = i;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getImageId() {
        return imageId;
    }
}
