package com.example.group9.choresplitter;

import java.io.Serializable;

/**
 * Created by andrewding on 5/2/15.
 */
public class MyDate implements Serializable{
    private int day;
    private int hour;
    private int minute;
    private int second;

    private int month;
    private int monthDay;
    private int year;

    public MyDate(int d, int h, int m, int s) {
        day = d;
        hour = h;
        minute = m;
        second = s;
    }

    public MyDate(int m, int d, int y) {
        month = m;
        monthDay = d;
        year = y;
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

    public int getMonth() {
        return month;
    }

    public int getMonthDay() {
        return monthDay;
    }

    public int getYear() {
        return year;
    }

    public void print() {
        System.out.println(String.format("%02d:%02d:%02d:%02d", day, hour, minute, second));
    }
}
