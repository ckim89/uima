package com.example.group9.choresplitter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import java.util.List;

/**
 * Created by danielkim on 4/9/15.
 */
public class fragment2 extends Fragment {
    View rootView, unclaimedChoresListView;
    Context context;
    int numApproval;
    int numMembers;


    //private static List<Task> unclaimedTasks, pendingTasks, completedTasks;
    private List<Task> unclaimedTasks, pendingTasks, completedTasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ProgressDialog dlg = new ProgressDialog(getActivity());
        dlg.setTitle("Please Wait");
        dlg.setMessage("Signing up. Please wait.");
        dlg.show();
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        //unclaimedChoresListView = inflater.inflate(R.layout.unclaimed_chores_list_view, container, false);
        context = rootView.getContext();

        unclaimedTasks = new ArrayList<Task>();
        pendingTasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("groupID", GroupsListActivity.GID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        if (a.get("status").equals("pending")) {
                            Task newTask = new Task((String) a.get("Name"), -1);
                            newTask.setId((String) a.get("taskID"));
                            ArrayList<String> approvedList = (ArrayList<String>) a.get("approved");
                            if (approvedList != null) {
                                for (String b: approvedList) {
                                    newTask.approvedBy(b);
                                    newTask.setUpVotes(newTask.getUpVotes() + 1);
                                }
                            }
                            newTask.setAllUsers((int) a.get("nummems"));
                            pendingTasks.add(newTask);
                        } else if (a.get("status").equals("unclaimed")) {
                            Task newTask = new Task((String) a.get("Name"), -1);
                            newTask.setId((String) a.get("taskID"));
                            ArrayList<String> approvedList = (ArrayList<String>) a.get("approved");
                            if (approvedList != null) {
                                for (String b : (ArrayList<String>) a.get("approved")) {
                                    newTask.approvedBy(b);
                                    newTask.setUpVotes(newTask.getUpVotes() + 1);
                                }
                            }
                            newTask.setAllUsers((int) a.get("nummems"));
                            if (newTask.getUpVotes() != newTask.getAllUsers()) {
                                unclaimedTasks.add(newTask);
                            }
                            //TODO: currently doesn't add unclaimed task if has 4 votes, but also needs to
                            //TODO: remove from parse
                        } else if (a.get("status").equals("completed")) {
                            //TODO: get num points to set task to from auction shitballs
                            Task newTask = new Task((String) a.get("Name"), -1);
                            newTask.setId((String) a.get("taskID"));
                            ArrayList<String> approvedList = (ArrayList<String>) a.get("approved");
                            if (approvedList != null) {
                                for (String b : (ArrayList<String>) a.get("approved")) {
                                    newTask.approvedBy(b);
                                    newTask.setUpVotes(newTask.getUpVotes() + 1);
                                }
                            }
                            newTask.setAllUsers((int) a.get("nummems"));
                            completedTasks.add(newTask);
                        }
                    }
                    populateListView();
                    registerClickCallback();
                }
            }

        });

        dlg.dismiss();

        return rootView;
    }

    private void populateListView() {

        System.out.println(unclaimedTasks.size());
        //Build adapter
        ArrayAdapter<Task> adapter = new UnclaimedTaskListAdapter();
        //Configure list view
        ListView list = (ListView) rootView.findViewById(R.id.unclaimed_chores_list_view);
        list.setAdapter(adapter);

        ArrayAdapter<Task> pendingAdapter = new PendingTaskListAdapter();
        ListView list2 = (ListView) rootView.findViewById(R.id.pending_chores_list_view);
        list2.setAdapter(pendingAdapter);

        ArrayAdapter<Task> completedAdapter = new CompletedTaskListAdapter();
        ListView list3 = (ListView) rootView.findViewById(R.id.completed_chores_list_view);
        list3.setAdapter(completedAdapter);
    }

    private class UnclaimedTaskListAdapter extends ArrayAdapter<Task> {
        public UnclaimedTaskListAdapter() {
            super(context, R.layout.unclaimed_chores_list_view, unclaimedTasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.unclaimed_chores_list_view, parent, false);
            }

            //Find member list item to work with
            Task currentItem = unclaimedTasks.get(position);
            //Fill View
            TextView unclaimedTaskNameText = (TextView) itemView.findViewById(R.id.unclaimed_task_name);
            unclaimedTaskNameText.setText(currentItem.getName());
            TextView unclaimedNumberPointsText = (TextView) itemView.findViewById(R.id.unclaimed_number_points);
            String a = "";
            for (String s : currentItem.getApprovedBy()) {
                a += s + ", ";
            }
            if (currentItem.getApprovedBy().size() >= 1) {
                a = a.substring(0, a.length() - 2);
            }
            unclaimedNumberPointsText.setText(a);
            TextView UnclaimedCancelText = (TextView) itemView.findViewById(R.id.unclaimed_cancel);
            UnclaimedCancelText.setText(currentItem.getUpVotes() + "/" + currentItem.getAllUsers());

            return itemView;
        }
    }

    private class PendingTaskListAdapter extends ArrayAdapter<Task> {
        public PendingTaskListAdapter() {
            super(context, R.layout.pending_chores_list_view, pendingTasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.pending_chores_list_view, parent, false);
            }
            //Find member list item to work with
            Task currentItem = pendingTasks.get(position);
            //Fill View
            TextView pendingTaskNameText = (TextView) itemView.findViewById(R.id.pending_task_name);
            pendingTaskNameText.setText(currentItem.getName());
            TextView pendingNumberPointsText = (TextView) itemView.findViewById(R.id.pending_number_points);
            String a = "";
            for (String s : currentItem.getApprovedBy()) {
                a += s + ", ";
            }
            if (currentItem.getApprovedBy().size() >= 1) {
                a = a.substring(0, a.length() - 2);
            }
            pendingNumberPointsText.setText(a);
            TextView approvedCountText = (TextView) itemView.findViewById(R.id.approved_count);
            approvedCountText.setText(currentItem.getUpVotes() + "/" + currentItem.getAllUsers());

            return itemView;
        }
    }

    private class CompletedTaskListAdapter extends ArrayAdapter<Task> {
        public CompletedTaskListAdapter() {
            super(context, R.layout.completed_chores_list_view, completedTasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.completed_chores_list_view, parent, false);
            }
            //Find member list item to work with
            Task currentItem = completedTasks.get(position);
            //Fill View
            TextView completedTaskNameText = (TextView) itemView.findViewById(R.id.completed_task_name);
            completedTaskNameText.setText(currentItem.getName());
            TextView completedNumberPointsText = (TextView) itemView.findViewById(R.id.completed_number_points);
            String a = "";
            for (String s : currentItem.getApprovedBy()) {
                a += s + ", ";
            }
            if (currentItem.getApprovedBy().size() >= 1) {
                a = a.substring(0, a.length() - 2);
            }
            completedNumberPointsText.setText(a);
            TextView disputeCountText = (TextView) itemView.findViewById(R.id.dispute_count);
            disputeCountText.setText(currentItem.getUpVotes() + "/" + currentItem.getAllUsers());

            return itemView;
        }
    }



    private void registerClickCallback() {
        ListView list = (ListView) rootView.findViewById(R.id.unclaimed_chores_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = unclaimedTasks.get(position);
                /*
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                */
                Intent intent = new Intent(getActivity(), TaskAuction.class);
                intent.putExtra("thisTask", clickedItem);
                startActivity(intent);
            }
        });

        ListView list2 = (ListView) rootView.findViewById(R.id.pending_chores_list_view);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = pendingTasks.get(position);
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

        ListView list3 = (ListView) rootView.findViewById(R.id.completed_chores_list_view);
        list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = completedTasks.get(position);
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelUnclaimedChore(View view) {
        //Button click callback
        LinearLayout clickedRow = (LinearLayout) view.getParent();
        LinearLayout tempRow = (LinearLayout) clickedRow.getChildAt(0);

        //These two lines get the position that the button is in
        ListView temp = (ListView) clickedRow.getParent();
        int position = temp.getPositionForView(view);
        final Task currentTask = unclaimedTasks.get(position);

        TextView taskName = (TextView) tempRow.getChildAt(0);
        TextView numPoints = (TextView) tempRow.getChildAt(1);
        TextView cancelVote = (TextView) clickedRow.getChildAt(1);

        /*
        String message = "Pressed " + taskName.getText().toString() +
                " , which has " + numPoints.getText().toString() +
                " points and " + cancelVote.getText().toString() + " cancel votes.";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        */

        ArrayList<String>cancelUsers = currentTask.getApprovedBy();
        String curid = currentTask.getId();
        ParseUser u = ParseUser.getCurrentUser();
        final String username = u.getUsername();
        if (cancelUsers.contains(username)) {
            currentTask.removeUser(username);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("taskID", curid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject a : parseObjects) {
                            List<String> b = new ArrayList<String>();
                            b = (ArrayList<String>) a.get("approved");
                            ArrayList<String> temp = new ArrayList<String>(b);
                            a.remove("approved");
                            for (String c : b) {
                                if (c.equals(username)) { //undoing a vote
                                    temp.remove(c);
                                }
                            }
                            b = temp;
                            for (String c : b) {
                                a.add("approved", c);
                            }
                            a.saveInBackground();
                        }
                    }
                }

            });
        } else { //adding a vote
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("taskID", curid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject a : parseObjects) {
                            a.add("approved", username);
                            a.saveInBackground();
                        }
                    }
                }

            });
        }

        /*
        if (currentTask.getUpVotes() == 0) {
            unclaimedTasks.remove(currentTask);
        }
        */
        populateListView();
    }

    public void approvePendingChore(View view) {
        //Button click callback
        LinearLayout clickedRow = (LinearLayout) view.getParent();
        LinearLayout tempRow = (LinearLayout) clickedRow.getChildAt(0);

        //These two lines get the position that the button is in
        ListView temp = (ListView) clickedRow.getParent();
        int position = temp.getPositionForView(view);
        Task currentTask = pendingTasks.get(position);

        TextView taskName = (TextView) tempRow.getChildAt(0);
        TextView numPoints = (TextView) tempRow.getChildAt(1);
        TextView cancelVote = (TextView) clickedRow.getChildAt(1);

        /*
        String message = "Pressed " + taskName.getText().toString() +
                " , which has " + numPoints.getText().toString() +
                " points and " + cancelVote.getText().toString() + " approval votes.";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        */

        ArrayList<String> approvedUsers = currentTask.getApprovedBy();
        String curid = currentTask.getId();
        ParseUser u = ParseUser.getCurrentUser();
        final String username = u.getUsername();
        if (approvedUsers.contains(username)) {
            currentTask.removeUser(username);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("taskID", curid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject a : parseObjects) {
                            List<String> b = new ArrayList<String>();
                            b = (ArrayList<String>) a.get("approved");
                            ArrayList<String> temp = new ArrayList<String>(b);
                            a.remove("approved");
                            for (String c : b) {
                                if (c.equals(username)) {
                                    temp.remove(c);
                                }
                            }
                            b = temp;
                            for (String c : b) {
                                a.add("approved", c);
                            }
                            a.saveInBackground();
                        }
                    }
                }

            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("taskID", curid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject a : parseObjects) {
                            a.add("approved", username);
                            a.saveInBackground();
                        }
                    }
                }

            });
        }

        //pendingTasks.remove(currentTask);
        populateListView();
    }

    public void disputeCompletedChore(View view) {
        //Button click callback
        LinearLayout clickedRow = (LinearLayout) view.getParent();
        LinearLayout tempRow = (LinearLayout) clickedRow.getChildAt(0);

        //These two lines get the position that the button is in
        ListView temp = (ListView) clickedRow.getParent();
        int position = temp.getPositionForView(view);
        Task currentTask = completedTasks.get(position);


        TextView taskName = (TextView) tempRow.getChildAt(0);
        TextView numPoints = (TextView) tempRow.getChildAt(1);
        TextView cancelVote = (TextView) clickedRow.getChildAt(1);
        /*
        String message = "Pressed " + taskName.getText().toString() +
                " , which has " + numPoints.getText().toString() +
                " points and " + cancelVote.getText().toString() + " cancel votes.";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        */

        ArrayList<String> disputeUsers = currentTask.getApprovedBy();
        String curid = currentTask.getId();
        ParseUser u = ParseUser.getCurrentUser();
        final String username = u.getUsername();
        if (disputeUsers.contains(username)) {
            currentTask.removeUser(username);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("taskID", curid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject a : parseObjects) {
                            List<String> b = new ArrayList<String>();
                            b = (ArrayList<String>) a.get("approved");
                            ArrayList<String> temp = new ArrayList<String>(b);
                            a.remove("approved");
                            for (String c : b) {
                                if (c.equals(username)) { //undoing a vote
                                    temp.remove(c);
                                }
                            }
                            b = temp;
                            for (String c : b) {
                                a.add("approved", c);
                            }
                            a.saveInBackground();
                        }
                    }
                }

            });
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("taskID", curid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject a : parseObjects) {
                            a.add("approved", username);
                            a.saveInBackground();
                        }
                    }
                }

            });

        }
        //completedTasks.remove(currentTask);
        populateListView();
    }
}