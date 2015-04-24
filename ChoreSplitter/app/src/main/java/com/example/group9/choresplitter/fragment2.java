package com.example.group9.choresplitter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielkim on 4/9/15.
 */
public class fragment2 extends Fragment {
    View rootView;
    Context context;

    private List<Task> unclaimedTasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        context = rootView.getContext();

        populateListView();
        registerClickCallback();

        return rootView;
    }

    private void populateListView() {
        unclaimedTasks = new ArrayList<Task>();
        unclaimedTasks.add(new Task("Task 1", 1));
        unclaimedTasks.add(new Task("Task 2", 2));
        unclaimedTasks.add(new Task("Task 3", 3));
        unclaimedTasks.add(new Task("Task 4", 4));

        //Build adapter
        ArrayAdapter<Task> adapter = new UnclaimedTaskListAdapter();

        //Configure list view
        ListView list = (ListView) rootView.findViewById(R.id.unclaimed_chores_list_view);

        list.setAdapter(adapter);
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
            TextView nameText = (TextView) itemView.findViewById(R.id.unclaimed_task_name);
            nameText.setText(currentItem.getName());


            TextView pointsText = (TextView) itemView.findViewById(R.id.unclaimed_number_points);
            pointsText.setText(currentItem.getPoints() + "");

            TextView cancelText = (TextView) itemView.findViewById(R.id.unclaimed_cancel);
            cancelText.setText("0/4");


            return itemView;
        }
    }

    private void registerClickCallback() {
        ListView list = (ListView) rootView.findViewById(R.id.unclaimed_chores_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Task clickedItem = unclaimedTasks.get(position);
                String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}