package com.example.group9.choresplitter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class Addmember extends ActionBarActivity {

    Button invite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);

        Bundle extra = getIntent().getExtras();
        final String groupid = extra.getString("GID");
        final String groupname = extra.getString("name");
        setTitle("Invite to: " + groupname);

        invite = (Button) findViewById(R.id.sendInvite);
        invite.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        TextView username = (TextView) findViewById(R.id.invuser);
                        String u = username.getText().toString();
                        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
                        query1.whereEqualTo("username", u);
                        query1.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> parseUsers, ParseException e) {
                                if (e==null) {
                                    for (ParseUser a : parseUsers) {
                                        JSONArray b = new JSONArray();
                                        b.put(groupid);
                                        a.put("pendingGroups", b);
                                        a.saveInBackground();
                                    }
                                    Toast.makeText(getApplicationContext(), "Invitation Sent!",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), GroupsListActivity.class)
                                            .putExtra("GroupID", groupid)
                                            .putExtra("GroupName", groupname);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addmember, menu);
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
