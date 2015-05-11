package com.example.group9.choresplitter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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
                        final String g = groupname.getText().toString();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
                        query.orderByDescending("GroupID");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (e == null) {
                                    for (ParseObject a : parseObjects) {
                                        int highest = Integer.parseInt((String) a.get("GroupID"));
                                        final ParseObject newgroup = new ParseObject("Groups");
                                        newgroup.add("Users", ParseUser.getCurrentUser().getUsername().toString());
                                        newgroup.add("Points", "0");
                                        newgroup.put("GroupID", highest + 1 + "");
                                        newgroup.put("GroupName", g);
                                        newgroup.saveInBackground();
                                        break;
                                    }
                                }
                            }

                        });
                        Toast.makeText(getApplicationContext(), "You have created a new group!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }
}
