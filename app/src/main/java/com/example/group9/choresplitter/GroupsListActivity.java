package com.example.group9.choresplitter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GroupsListActivity extends ActionBarActivity {

    Button B1;
    Button B2;
    String GID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);

        Bundle extra = getIntent().getExtras();
        String groupid = extra.getString("GroupID");
        String name = extra.getString("GroupName");
        setTitle(name);
        GID = groupid;
        fragment1 f1 = new fragment1();
        B1 = (Button)findViewById(R.id.button);
        B2 = (Button)findViewById(R.id.button2);
        B2.setBackgroundColor(Color.GRAY);
        B2.setTextColor(Color.DKGRAY);
        B1.setBackgroundColor(Color.BLUE);
        B1.setTextColor(Color.WHITE);
        if (getSupportFragmentManager().findFragmentById(R.id.fr1) != null) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.fr1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fr1, f1).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fr1, f1).commit();
        }
        setButtons();
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
            default:
                return super.onOptionsItemSelected(item);
        }    }

    public void setButtons() {
        B1 = (Button)findViewById(R.id.button);
        B2 = (Button)findViewById(R.id.button2);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B2.setBackgroundColor(Color.GRAY);
                B2.setTextColor(Color.DKGRAY);
                B1.setBackgroundColor(Color.BLUE);
                B1.setTextColor(Color.WHITE);
                fragment1 f1 = new fragment1();
                if (getSupportFragmentManager().findFragmentById(R.id.fr1) != null) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fr1)).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr1, f1).commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction().
                            add(R.id.fr1, f1).commit();
                }
            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B1.setBackgroundColor(Color.GRAY);
                B1.setTextColor(Color.DKGRAY);
                B2.setBackgroundColor(Color.BLUE);
                B2.setTextColor(Color.WHITE);
                fragment2 f2 = new fragment2();
                if (getSupportFragmentManager().findFragmentById(R.id.fr1) != null) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.fr1)).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fr1, f2).commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction().
                            add(R.id.fr1, f2).commit();
                }
            }
        });
    }

    public void changeLoserMessage(View view) {
        TextView loserTextView = (TextView) findViewById(R.id.loser_text_view);
        //EditText messageField = (EditText) findViewById(R.id.message_field);
        //loserTextView.setText(messageField.getText().toString());
    }


    public void removeUnclaimedChore(View view) {
        LinearLayout clickedRow = (LinearLayout) view.getParent();
        TextView pointsText = (TextView) clickedRow.getChildAt(1);
        pointsText.setText("EUWEH");

    }

    public String getGroupID() {
        return GID;
    }

}