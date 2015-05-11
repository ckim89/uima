package com.example.group9.choresplitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TaskDetail extends ActionBarActivity {

    String taskName, taskPoints, taskOwner, taskid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Bundle extras = getIntent().getExtras();
        taskName = extras.getString("taskName");
        taskPoints = extras.getInt("taskPoints") + "";
        taskOwner = extras.getString("taskOwner");
        taskid = extras.getString("taskid");


        TextView taskNameText = (TextView) findViewById(R.id.task_detail_name);
        TextView taskPointsText = (TextView) findViewById(R.id.task_detail_points);
        TextView taskOwnerText = (TextView) findViewById(R.id.task_detail_owner);

        taskNameText.setText(taskName);
        taskPointsText.setText(taskPoints);
        taskOwnerText.setText(taskOwner);
    }


    public void completeTask(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("taskID", taskid);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject a : list) {
                        a.put("status", "completed");
                        a.saveInBackground();
                        break;
                    }
                }
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Groups");
                query1.whereEqualTo("GroupID", GroupsListActivity.GID);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            for (ParseObject a : list) {
                                ArrayList<String> users = new ArrayList<String>();
                                ArrayList<String> userpoint = new ArrayList<String>();
                                users = (ArrayList<String>) a.get("Users");
                                userpoint = (ArrayList<String>) a.get("points");
                                a.remove("points");
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).equals(taskOwner)) {
                                        String c = userpoint.get(i);
                                        int d = Integer.parseInt(c);
                                        int g = Integer.parseInt(taskPoints);
                                        d = d + g;
                                        String output = d + "";
                                        userpoint.set(i, output);
                                        for (String q: userpoint) {
                                            a.add("points", q);
                                        }
                                        break;
                                    }
                                }
                                a.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("completed", true);
                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
