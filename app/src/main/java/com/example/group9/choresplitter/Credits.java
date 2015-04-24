package com.example.group9.choresplitter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Kevin on 4/19/2015.
 */
public class Credits extends ActionBarActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        EditText text1 = (EditText)findViewById(R.id.editText);
        text1.setKeyListener(null);
    }
}
