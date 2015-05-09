package com.example.group9.choresplitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class SignIn extends ActionBarActivity {

    Button signup;
    Button signin;

    //TODO: must have code that creates the members list from list of members already in database
    //TODO: and also have code that updates this in each activity when needed
    public static List<Member> members;

    //TODO: delete the initialize array code
    //I store and create the arrays that hold people here because the onCreate is only called
    //once in the program.
    public static List<Task> unclaimedTasks, pendingTasks, completedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signup = (Button)findViewById(R.id.signupbutt);
        signin = (Button)findViewById(R.id.signinbutt);

        signup.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        signup();
                    }
                });

        signin.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        signin();
                    }
                });

        //TODO: remove this code
        unclaimedTasks = new ArrayList<Task>();
        pendingTasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        initializeListView();

    }

    private void initializeListView() {
        unclaimedTasks.add(new Task("Task 1", 3));
        unclaimedTasks.add(new Task("Task 2", 5));
        unclaimedTasks.add(new Task("Task 3", 9));
        unclaimedTasks.add(new Task("Task 4", 2));

        pendingTasks.add(new Task("Task 12", 4));
        pendingTasks.add(new Task("Task 13", 3));
        pendingTasks.add(new Task("Task 14", 10));
        pendingTasks.add(new Task("Task 15", 8));

        completedTasks.add(new Task("Task 35", 8));
        completedTasks.add(new Task("Task 36", 5));
        completedTasks.add(new Task("Task 37", 1));
        completedTasks.add(new Task("Task 38", 2));
    }

    public void signup() {
        final ProgressDialog dlg = new ProgressDialog(SignIn.this);
        dlg.setTitle("Please Wait");
        dlg.setMessage("Signing up. Please wait.");
        dlg.show();

        ParseUser user = new ParseUser();
        TextView usern = (TextView)findViewById(R.id.signupuser);
        String username = usern.getText().toString();
        TextView passw = (TextView)findViewById(R.id.signuppass);
        String password = passw.getText().toString();

        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                dlg.dismiss();
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Sign up Success!",
                            Toast.LENGTH_LONG).show();

                } else {
                    Log.e("Sign up failed", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Sign Up Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signin() {
        final ProgressDialog dlg = new ProgressDialog(SignIn.this);
        dlg.setTitle("Please Wait");
        dlg.setMessage("Signing In. Please wait.");
        dlg.show();

        TextView usern = (TextView)findViewById(R.id.signinuser);
        String username = usern.getText().toString();
        TextView passw = (TextView)findViewById(R.id.signinpass);
        String password = passw.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                dlg.dismiss();
                if (user != null) {
                    Intent createAccount = new Intent(getApplicationContext(), GroupList.class)
                            .putExtra("username", user.getString("username"));
                    startActivity(createAccount);
                    finish();
                } else {
                    // Clear fields since login failed

                    // Alert user of failure
                    Toast.makeText(getApplicationContext(), "Login Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateMembers() {
        //TODO: code should update static field members here
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
