package com.example.group9.choresplitter;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MemberDetail extends ActionBarActivity {

    Context context;
    private List<Task> claimedChores;
    private List<Task> completedChores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        //TODO: pull lists from database
        claimedChores = new ArrayList<Task>();
        completedChores = new ArrayList<Task>();
        claimedChores.add(new Task("Task1", 0));
        claimedChores.add(new Task("Task2", 5));
        claimedChores.add(new Task("Task3", 39));
        completedChores.add(new Task("Task4", 3));
        completedChores.add(new Task("Task5", 22));
        completedChores.add(new Task("Task6", 44));
        completedChores.add(new Task("Task7", 13));

        context = getApplicationContext();
        populateListView();
        registerClickCallbackClaimed();
        registerClickCallbackCompleted();
    }


    private void populateListView() {

        //Populate claimed chores
        //Create list of items

        //Build adapter
        ArrayAdapter<Task> claimedAdapter = new ClaimedTaskListAdapter();
        //Configure list view
        ListView claimedList = (ListView) findViewById(R.id.chores_claimed_list_view);
        claimedList.setAdapter(claimedAdapter);

        //Populate completed chores
        //Create list of items

        //Build adapter
        ArrayAdapter<Task> completedAdapter = new CompletedTaskListAdapter();
        //Configure list view
        ListView completedList = (ListView) findViewById(R.id.chores_completed_list_view);
        completedList.setAdapter(completedAdapter);


    }


    //Array Adapter used to create the member list
    private class ClaimedTaskListAdapter extends ArrayAdapter<Task> {
        public ClaimedTaskListAdapter() {
            super(context, R.layout.chores_list_view, claimedChores);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.chores_list_view, parent, false);
            }

            //Find member list item to work with
            Task currentTask = claimedChores.get(position);

            //Fill view
            TextView nameText = (TextView) itemView.findViewById(R.id.chore_list_name);
            nameText.setText(currentTask.getName());

            TextView pointsText = (TextView) itemView.findViewById(R.id.chore_list_points);
            pointsText.setText(currentTask.getPoints() + "");

            TextView timeText = (TextView) itemView.findViewById(R.id.chore_list_time);
            //TODO: figure out time remaining
            //timeText.setText((currentTask.getTimeCreated()) + "");
            timeText.setText(("Time Remaining"));

            return itemView;
        }
    }

    private void registerClickCallbackClaimed() {
        ListView list = (ListView) findViewById(R.id.chores_claimed_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = claimedChores.get(position);
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Array Adapter used to create the member list
    private class CompletedTaskListAdapter extends ArrayAdapter<Task> {
        public CompletedTaskListAdapter() {
            super(context, R.layout.chores_list_view, completedChores);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.chores_list_view, parent, false);
            }

            //Find member list item to work with
            Task currentTask = completedChores.get(position);

            //Fill view
            TextView nameText = (TextView) itemView.findViewById(R.id.chore_list_name);
            nameText.setText(currentTask.getName());

            TextView pointsText = (TextView) itemView.findViewById(R.id.chore_list_points);
            pointsText.setText(currentTask.getPoints() + "");

            TextView timeText = (TextView) itemView.findViewById(R.id.chore_list_time);
            timeText.setText(("00:00:00"));

            return itemView;
        }
    }

    private void registerClickCallbackCompleted() {
        ListView list = (ListView) findViewById(R.id.chores_completed_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = completedChores.get(position);
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_member_detail, menu);
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
