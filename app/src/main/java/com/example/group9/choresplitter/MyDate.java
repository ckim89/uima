package com.example.group9.choresplitter;

/**
 * Created by andrewding on 5/2/15.
 */
public class MyDate {
    private int day;
    private int hour;
    private int minute;
    private int second;

    public MyDate(int d, int h, int m, int s) {
        day = d;
        hour = h;
        minute = m;
        second = s;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public void print() {
        System.out.println(String.format("%02d:%02d:%02d:%02d", day, hour, minute, second));
    }
}
