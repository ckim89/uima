package com.example.group9.choresplitter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Kevin on 5/10/2015.
 */
public class AddGroup extends ActionBarActivity{

    Button create;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);

        create = (Button) findViewById(R.id.createGroup);
        create.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        TextView groupname = (TextView) findViewById(R.id.invuser);
                        String g = groupname.getText().toString();
                        final ParseObject newGroup = new ParseObject("Groups");
                        newGroup.add("Users", ParseUser.getCurrentUser().getUsername());
                        newGroup.put("GroupName", g);
                        newGroup.put("GroupID", 3);
                        newGroup.saveInBackground();
                        Toast.makeText(getApplicationContext(), "You have created a new group!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }
}
