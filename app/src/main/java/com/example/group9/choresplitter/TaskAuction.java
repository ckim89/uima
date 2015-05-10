package com.example.group9.choresplitter;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TaskAuction extends ActionBarActivity {

    private ArrayList<Bidder> bidders;
    private boolean completed;
    TextView auctionTimeRemainingText;

    private MyDate currentDate, finishDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_auction);

        Task thisTask = (Task) getIntent().getSerializableExtra("thisTask");

        MyDate createdDate = thisTask.getDateCreated();
        int createdDay = 122;
        int createdHour = 20;
        int createdMinute = 59;
        int createdSecond = 59;

        //TODO: get auction duration from elsewhere
        int auctionDurationHours = 24;

        //Create the finishing MyDate based on creation date and auction duration
        /*
        finishDate = new MyDate(createdDay + (auctionDurationHours / 24),
                                createdHour,
                                createdMinute,
                                createdSecond);
        */

        finishDate = new MyDate(createdDate.getDay() + (auctionDurationHours / 24),
                                createdDate.getHour(),
                                createdDate.getMinute(),
                                createdDate.getSecond());

        //Get the current MyDate
        currentDate = getCurrentDate();

        /*
        System.out.print("currentDate: ");
        currentDate.print();
        System.out.print("finishDate: ");
        finishDate.print();
        */

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

        //TODO: get list of bidders from elsewhere
        bidders = new ArrayList<Bidder>();
        /*
        bidders.add(new Member("kevin"));
        bidders.add(new Member("dan"));
        bidders.add(new Member("andrew"));
        bidders.add(new Member("kevin"));
        */

        populateListView();
        updateMembers();
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
            nameText.setText(currentItem.getMember().getUserName());

            TextView pointsText = (TextView) itemView.findViewById(R.id.member_points_field);
            pointsText.setText(currentItem.getBid() + "");

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
            String hms = String.format("%02d:%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                    TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            auctionTimeRemainingText.setText(hms);
        }

        @Override
        public void onFinish() {
            completed = true;
            auctionTimeRemainingText.setText("Auction Closed");
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
        EditText auctionBidField = (EditText) findViewById(R.id.auction_bid_field);
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


        //bidders.add(0, new Member("bidder"));
        populateListView();
    }

    private void updateMembers() {

    }

    private class Bidder {
        private Member member;
        private int bid;

        public Bidder(Member m, int b) {
            member = m;
            bid = b;
        }

        public Member getMember() {
            return member;
        }

        public int getBid() {
            return bid;
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
