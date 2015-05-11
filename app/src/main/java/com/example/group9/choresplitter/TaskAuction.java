package com.example.group9.choresplitter;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TaskAuction extends ActionBarActivity {

    private ArrayList<Bidder> bidders;
    private boolean completed;
    TextView auctionTimeRemainingText;
    private String auctionId;
    String hms;
    List<Integer> dates;

    private MyDate currentDate, finishDate;
    ParseObject task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_auction);

        Task thisTask = (Task) getIntent().getSerializableExtra("thisTask");

        MyDate createdDate = thisTask.getDateCreated();
        Bundle extras = getIntent().getExtras();
        auctionId = extras.getString("taskId");
        System.out.println(auctionId);

        TextView auctionTaskName = (TextView) findViewById(R.id.auction_task_name);

        auctionTaskName.setText(thisTask.getName());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("taskID", auctionId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        if (a.get("taskID").toString().equals(auctionId)) {
                            task = a;
                        }
                    }
                    bidders = new ArrayList<Bidder>();

                    ArrayList<String> tempBidders = (ArrayList<String>) task.get("bidders");
                    ArrayList<Integer> tempBidderPoints = (ArrayList<Integer>) task.get("bidpoints");
                    ArrayList<String> tempBidderTimestamp = (ArrayList<String>) task.get("timestamp");

                    if (tempBidders != null) {
                        for (int i = 0; i < tempBidders.size(); i++) {
                            bidders.add(new Bidder(tempBidders.get(i), tempBidderPoints.get(i), tempBidderTimestamp.get(i)));
                        }
                    }

                    populateListView();
                }
            }

        });
        System.out.println(task);

        /*
        String completeByDay = String.format("%02d/%02d/%04d", dates.get(0), dates.get(1), dates.get(2));
        String completeByTime = String.format("%02d:%02d:%02d", dates.get(3), dates.get(4), dates.get(5));
        completeBy.setText(completeByDay + " at " + completeByTime);
*/


        //TODO: get auction duration from elsewhere
        int auctionDurationHours = 24;

        finishDate = new MyDate(createdDate.getDay() + (auctionDurationHours / 24),
                                createdDate.getHour(),
                                createdDate.getMinute(),
                                createdDate.getSecond());

        //Get the current MyDate
        currentDate = getCurrentDate();

        //Calculate days remaining
        int daysLeft = finishDate.getDay() - currentDate.getDay();
        int hoursLeft = finishDate.getHour() - currentDate.getHour();
        int minutesLeft = finishDate.getMinute() - currentDate.getMinute();
        int secondsLeft = finishDate.getSecond() - currentDate.getSecond();

        //Put days remaining on screen
        auctionTimeRemainingText = (TextView) findViewById(R.id.auction_time_remaining);
        String time = String.format("%02d:%02d:%02d:%02d", daysLeft, hoursLeft, minutesLeft, secondsLeft);
        auctionTimeRemainingText.setText(time);
        completed = false;

        //Create countdown timer based on time remaining
        long timerDuration = (daysLeft*86400 + hoursLeft*3600 + minutesLeft*60 + secondsLeft) * 1000;
        final CounterClass timer = new CounterClass(timerDuration, 1000);
        timer.start();

        /*
        //TODO: get list of bidders from elsewhere
        bidders = new ArrayList<Bidder>();
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("AuctionBids");
        query1.whereEqualTo("groupID", GroupsListActivity.GID);
        query1.findInBackground(new FindCallback<ParseObject>() {
            ArrayList<String> bidders = new ArrayList<String>();
            ArrayList<String> bidderPoints = new ArrayList<String>();
            ArrayList<String> bidderTimestamp = new ArrayList<String>();
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        if (a.get("auctionId").equals(auctionId)) {
                            bidders = (ArrayList<String>) a.get("bidders");
                            bidderPoints = (ArrayList<String>) a.get("bidderPoints");
                            bidderTimestamp = (ArrayList<String>) a.get("bidderTimestamp");
                        }
                    }

                    for (int i = 0; i < bidders.size(); i++) {
                        System.out.println(bidders.get(i) + " " + bidderPoints.get(i) + " " + bidderTimestamp.get(i));
                    }
                }
            }

        });
        */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void populateListView() {
        //Build adapter
        ArrayAdapter<Bidder> adapter = new AuctionListAdapter();

        //Configure list view
        ListView list = (ListView) findViewById(R.id.auction_bid_list_view);
        list.setAdapter(adapter);
    }

    //Array Adapter used to create the member list
    private class AuctionListAdapter extends ArrayAdapter<Bidder> {
        public AuctionListAdapter() {
            super(getApplicationContext(), R.layout.auction_bid_list_view, bidders);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.auction_bid_list_view, parent, false);
            }

            //Find member list item to work with
            Bidder currentItem = bidders.get(position);

            //Fill view
            TextView nameText = (TextView) itemView.findViewById(R.id.member_name_field);
            nameText.setText(currentItem.getMember());

            TextView pointsText = (TextView) itemView.findViewById(R.id.member_points_field);
            pointsText.setText(currentItem.getBid() + "");

            TextView timeText = (TextView) itemView.findViewById(R.id.auction_bid_time_remaining);
            timeText.setText(currentItem.getTimestamp());

            return itemView;
        }
    }


    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            hms = String.format("%02d:%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                    TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            auctionTimeRemainingText.setText(hms);
        }

        @Override
        public void onFinish() {
            completed = true;
            auctionTimeRemainingText.setText("Auction Closed");

            Bidder minBid = bidders.get(bidders.size() - 1);
            String name = minBid.getMember();

            task.put("claimedby", name);
            task.put("status", "claimed");
            task.put("Points", minBid.getBid());
            task.saveInBackground();
        }
    }

    private MyDate getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_YEAR);
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        int currentSecond = c.get(Calendar.SECOND);
        return new MyDate(currentDay, currentHour, currentMinute, currentSecond);
    }

    public void setBid(View view) {
        final EditText auctionBidField = (EditText) findViewById(R.id.auction_bid_field);
        int bid = (int) Integer.parseInt(auctionBidField.getText().toString());

        if (completed) {
            Toast.makeText(getApplicationContext(), "Auction is closed.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bidders.size() > 0) {
            //Check that the bid is a valid bid
            if (bid >= bidders.get(0).getBid()) {
                //invalid bid
                //Bid must be > lowest bid
                Toast.makeText(getApplicationContext(), "Must bid lower than lowest bid.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (bid < 1) {
            Toast.makeText(getApplicationContext(), "Cannot bid lower than 1.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bid == 1) {
            //notify that this will be buyout
        }

        //TODO: add bidder
        String currentUser = ParseUser.getCurrentUser().getUsername().toString();
        bidders.add(new Bidder(currentUser, bid, hms));

        task.add("bidpoints", bid);
        task.add("bidders", currentUser);
        task.add("timestamp", hms);
        task.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //bidders.add(0, new Member("bidder"));
                auctionBidField.setText("");
                populateListView();
            }
        });



        System.out.println(dates);
    }

    private class Bidder {
        private String member;
        private int bid;
        private String timestamp;

        public Bidder(String m, int b, String t) {
            member = m;
            bid = b;
            timestamp = t;
        }

        public String getMember() {
            return member;
        }

        public int getBid() {
            return bid;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_auction, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
