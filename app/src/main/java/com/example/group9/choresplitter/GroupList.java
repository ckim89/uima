package com.example.group9.choresplitter;

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

import java.util.ArrayList;
import java.util.List;


public class GroupList extends ActionBarActivity {

    List<String> groups;
    List<ParseObject> group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        groups = new ArrayList<String>();
        group = new ArrayList<ParseObject>();
        Bundle extra = getIntent().getExtras();
        String user = extra.getString("username");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("UserID", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        String add = (String) a.get("GroupName");
                        groups.add(add);
                        group.add(a);
                    }
                }
            }
        });

        ListView lv = (ListView) findViewById(R.id.GroupList);
        ListAdapter adapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, groups);

        lv.setAdapter(adapt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject item = group.get(position);
                Intent intent = new Intent(getApplicationContext(), GroupsListActivity.class)
                        .putExtra("GroupID", item.getString("GroupID"))
                        .putExtra("GroupName", item.getString("GroupName"));
                startActivity(intent);
                finish();

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
