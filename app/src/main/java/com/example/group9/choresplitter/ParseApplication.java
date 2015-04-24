package com.example.group9.choresplitter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        // Application ID and client key
        Parse.initialize(this, "Sex1r9LMKbuxlxtEkIROfPcPL8NwNu889SUw14Xo",
                "XmKRoxMzUYjlDxpflwfiNix4vK6WRlB5fyxiae7l");

    }
}