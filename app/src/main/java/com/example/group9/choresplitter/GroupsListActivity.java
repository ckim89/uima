package com.example.group9.choresplitter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GroupsListActivity extends ActionBarActivity {

    Button B1;
    Button B2;
    TextView H1, H2;
    String GID;
    String name;

    fragment1 f1;
    fragment2 f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);

        Bundle extra = getIntent().getExtras();
        String groupid = extra.getString("GroupID");
        name = extra.getString("GroupName");
        setTitle(name);
        GID = groupid;
        f1 = new fragment1();
        B1 = (Button) findViewById(R.id.button);
        B2 = (Button) findViewById(R.id.button2);
        H1 = (TextView) findViewById(R.id.highlight1);
        H2 = (TextView) findViewById(R.id.highlight2);

        if (getSupportFragmentManager().findFragmentById(R.id.fr1) != null) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.fr1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fr1, f1).commit();
        } else {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fr1, f1).commit();
        }
        setButtons();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.credits:
                Intent intent = new Intent(this, Credits.class);
                startActivity(intent);
                return true;
            case R.id.add_member:
                final GroupsListActivity mac = (GroupsListActivity) this;
                final String id = mac.getGroupID();
                Intent intent2 = new Intent(this, Addmember.class);
                intent2.putExtra("GID", id).putExtra("name", mac.getGroupname());
                startActivity(intent2);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setButtons() {
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                H1.setBackgroundColor(Color.parseColor("#FF9999"));
                H2.setBackgroundColor(Color.parseColor("#EAEAEA"));
                f1 = new fragment1();
                if (getSupportFragmentManager().findFragmentById(R.id.fr1) != null) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fr1)).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr1, f1).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().
                            add(R.id.fr1, f1).commit();
                }
            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                H1.setBackgroundColor(Color.parseColor("#EAEAEA"));
                H2.setBackgroundColor(Color.parseColor("#FF9999"));
                f2 = new fragment2();
                if (getSupportFragmentManager().findFragmentById(R.id.fr1) != null) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fr1)).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr1, f2).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().
                            add(R.id.fr1, f2).commit();
                }
            }
        });
    }

    public void changeLoserMessage(View view) {
        TextView loserTextView = (TextView) findViewById(R.id.loser_text_view);
        EditText messageField = (EditText) findViewById(R.id.message_field);
        loserTextView.setText(messageField.getText().toString());
    }


    public void cancelUnclaimedChore(View view) {
        f2.cancelUnclaimedChore(view);
    }

    public void approvePendingChore(View view) {
        f2.approvePendingChore(view);
    }

    public void disputeCompletedChore(View view) {
        f2.disputeCompletedChore(view);
    }

    public String getGroupID() {
        return GID;
    }

    public String getGroupname() {
        return name;
    }

    public void addPending(View view) {
        //ListView l = (ListView) findViewById(R.id.pending_chores_list_view);
        SignIn.pendingTasks.add(new Task("EUWEH", 1));
    }
}