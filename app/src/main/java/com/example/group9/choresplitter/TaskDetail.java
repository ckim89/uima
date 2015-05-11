package com.example.group9.choresplitter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class TaskDetail extends ActionBarActivity {

    String taskName, taskPoints, taskOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Bundle extras = getIntent().getExtras();
        taskName = extras.getString("taskName");
        taskPoints = extras.getInt("taskPoints") + "";
        taskOwner = extras.getString("taskOwner");


        TextView taskNameText = (TextView) findViewById(R.id.task_detail_name);
        TextView taskPointsText = (TextView) findViewById(R.id.task_detail_points);
        TextView taskOwnerText = (TextView) findViewById(R.id.task_detail_owner);

        taskNameText.setText(taskName);
        taskPointsText.setText(taskPoints);
        taskOwnerText.setText(taskOwner);
    }


    public void completeTask(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("completed", true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
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
