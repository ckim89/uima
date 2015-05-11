package com.example.group9.choresplitter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GroupList extends ActionBarActivity {

    List<String> groups;
    List<ParseObject> group;
    List<String> pgroups;
    List<String> groups1;
    List<ParseObject> gg;
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
        groups1 = new ArrayList<String>();
        group = new ArrayList<ParseObject>();
        gg = new ArrayList<ParseObject>();
        pgroups = new ArrayList<String>();
        pgroup = new ArrayList<ParseObject>();
        Bundle extra = getIntent().getExtras();
        final String user = extra.getString("username");

        final ParseUser u = ParseUser.getCurrentUser();
        groups1 = (ArrayList<String>) u.get("Groups");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        gg.add(a);
                        if (groups1.contains(a.get("GroupID").toString())) {
                            String add = (String) a.get("GroupName");
                            groups.add(add);
                            group.add(a);
                        }
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
                    final ArrayList<ParseObject> invite = (ArrayList<ParseObject>) parseObjects;
                    for (ParseObject a : parseObjects) {
                        String add = (String) a.get("GroupName");
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
                            final ParseObject item = pgroup.get(position);
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            ParseUser user = ParseUser.getCurrentUser();
                                            user.add("Groups", (String) item.get("groupID"));
                                            for (ParseObject a: gg) {
                                                if (a.get("GroupID").toString().equals(item.get("groupID").toString())) {
                                                    a.add("Users", user.getUsername().toString());
                                                    a.saveInBackground();
                                                    break;
                                                }
                                            }
                                            user.saveInBackground();
                                            item.deleteInBackground();
                                            Toast.makeText(GroupList.this, "You have been added to the group!", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent createAccount = new Intent(getApplicationContext(), GroupList.class)
                                                    .putExtra("username", ParseUser.getCurrentUser().getUsername().toString());
                                            startActivity(createAccount);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(GroupList.this);
                            builder.setMessage("Would you like to join this group?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.credits:
                Intent intent = new Intent(this, Credits.class);
                startActivity(intent);
                return true;
            case R.id.add_group:
                Intent intent2 = new Intent(this, AddGroup.class);
                startActivity(intent2);
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
            try {
                saveImage(bp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
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

        /*
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseQuery<ParseUser> userqueries = query.whereEqualTo("username", username);
        ParseUser user = userqueries.getFirst();
        */
        ParseUser user = ParseUser.getCurrentUser();
        user.put("picture", file);
        user.saveInBackground();
    }
}
