package com.example.group9.choresplitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class GroupList extends ActionBarActivity {

    List<String> groups;
    List<ParseObject> group;
    List<String> pgroups;
    List<ParseObject> pgroup;
    GroupList a = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        final ProgressDialog dlg = new ProgressDialog(GroupList.this);
        dlg.setTitle("Please Wait");
        dlg.setMessage("Loading Groups. Please wait.");
        dlg.show();

        groups = new ArrayList<String>();
        group = new ArrayList<ParseObject>();
        pgroups = new ArrayList<String>();
        pgroup = new ArrayList<ParseObject>();
        Bundle extra = getIntent().getExtras();
        final String user = extra.getString("username");
        System.out.println(user);

        final ParseUser u = ParseUser.getCurrentUser();
        groups = (ArrayList<String>) u.get("Groups");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereContainedIn("GroupID", groups);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    groups.clear();
                    for (ParseObject a : parseObjects) {
                        String add = (String) a.get("GroupName");
                        groups.add(add);
                        group.add(a);
                    } //hi dan

                    ListView lv = (ListView) findViewById(R.id.GroupList);
                    ListAdapter adapt = new ArrayAdapter<String>(
                           a , android.R.layout.simple_list_item_1, groups);

                    lv.setAdapter(adapt);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ParseObject item = group.get(position);
                            Intent intent = new Intent(getApplicationContext(), GroupsListActivity.class)
                                    .putExtra("GroupID", item.getString("GroupID"))
                                    .putExtra("GroupName", item.getString("GroupName"));
                            startActivity(intent);
                        }
                    });
                    findPending(user);
                }
            }
        });
        dlg.dismiss();
    }

    public void findPending(String user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Invite");
        query.whereEqualTo("invited", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        String add = (String) a.get("GroupName");
                        System.out.println(a.get("GroupName"));
                        pgroups.add(add);
                        pgroup.add(a);
                    } //hi dan

                    ListView lv = (ListView) findViewById(R.id.pending_groups);
                    ListAdapter adapt = new ArrayAdapter<String>(
                            a , android.R.layout.simple_list_item_1, pgroups);

                    lv.setAdapter(adapt);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ParseObject item = pgroup.get(position);
                            Intent intent = new Intent(getApplicationContext(), GroupsListActivity.class)
                                    .putExtra("GroupID", item.getString("GroupID"))
                                    .putExtra("GroupName", item.getString("GroupName"));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_list, menu);
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
