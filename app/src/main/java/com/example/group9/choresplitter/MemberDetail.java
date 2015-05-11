package com.example.group9.choresplitter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MemberDetail extends ActionBarActivity {

    Context context;
    private List<Task> claimedTasks;
    private List<Task> completedTasks;
    ImageView imgFavorite;
    String username;
    boolean completed = false;
    int clickedPosition;

    final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        Intent myIntent = getIntent(); // gets the previously created intent
        username = myIntent.getStringExtra("name"); // will return "FirstKeyValue"
        setTitle(username);


        imgFavorite = (ImageView)findViewById(R.id.prof_pic);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

        //TODO: pull lists from database
        claimedTasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        claimedTasks.add(new Task("hello", 1));

        context = getApplicationContext();
        populateListView();
        registerClickCallbackClaimed();
        registerClickCallbackCompleted();

        TextView memberName = (TextView) findViewById(R.id.member_detail_name_text);
        TextView memberPoints = (TextView) findViewById(R.id.member_detail_points_text);


        Bundle extras = getIntent().getExtras();

        memberName.setText(username);
        memberPoints.setText(extras.getInt("points") + "");


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseQuery<ParseUser> userqueries = query.whereEqualTo("username", username);
        try {
            ParseUser user = userqueries.getFirst();
            if (user.getParseFile("picture") != null)
            {
                ParseFile file = user.getParseFile("picture");
                ImageView imageView = (ImageView) findViewById(R.id.prof_pic);
                byte[] bitmapdata = file.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                imageView.setImageBitmap(bitmap);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            super(context, R.layout.chores_list_view, claimedTasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.chores_list_view, parent, false);
            }

            //Find member list item to work with
            Task currentTask = claimedTasks.get(position);

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
        final TextView owner = (TextView) findViewById(R.id.member_detail_name_text);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = claimedTasks.get(position);
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), TaskDetail.class);
                intent.putExtra("taskName", clickedItem.getName());
                intent.putExtra("taskPoints", clickedItem.getPoints());
                intent.putExtra("taskOwner", owner.getText().toString());
                startActivityForResult(intent, REQUEST_CODE);
                clickedPosition = position;
            }
        });
    }

    //Array Adapter used to create the member list
    private class CompletedTaskListAdapter extends ArrayAdapter<Task> {
        public CompletedTaskListAdapter() {
            super(context, R.layout.chores_list_view, completedTasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.chores_list_view, parent, false);
            }

            //Find member list item to work with
            Task currentTask = completedTasks.get(position);

            //Fill view
            TextView nameText = (TextView) itemView.findViewById(R.id.chore_list_name);
            nameText.setText(currentTask.getName());

            TextView pointsText = (TextView) itemView.findViewById(R.id.chore_list_points);
            pointsText.setText(currentTask.getPoints() + "");

            TextView timeText = (TextView) itemView.findViewById(R.id.chore_list_time);
            MyDate c = currentTask.getDateCompleted();
            int month = c.getMonth();
            int day = c.getMonthDay();
            int year = c.getYear();
            String time = String.format("%02d/%02d/%04d", month, day, year);
            timeText.setText(time);

            return itemView;
        }
    }

    private void registerClickCallbackCompleted() {
        ListView list = (ListView) findViewById(R.id.chores_completed_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = completedTasks.get(position);
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.credits:
                Intent intent = new Intent(this, Credits.class);
                startActivity(intent);
                return true;
            case R.id.camera:
                open();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void open(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imgFavorite.setImageBitmap(bp);
            try {
                saveImage(bp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            ImageView img = (ImageView) findViewById(R.id.prof_pic);
            img.setImageBitmap(bp);
        }

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                completed = data.getBooleanExtra("completed", false);

                if (completed) {
                    Task moveTask = claimedTasks.get(clickedPosition);
                    moveTask.completeNow();
                    claimedTasks.remove(moveTask);
                    completedTasks.add(moveTask);
                    populateListView();
                }
            }
        }

    }

    protected void saveImage(Bitmap bp) throws IOException, ParseException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bp != null) {
            bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("prof_pic.png", byteArray);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseQuery<ParseUser> userqueries = query.whereEqualTo("username", username);
        ParseUser user = userqueries.getFirst();
        user.put("picture", file);
        user.saveInBackground();
    }
}
