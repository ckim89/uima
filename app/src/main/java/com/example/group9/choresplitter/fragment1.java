package com.example.group9.choresplitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danielkim on 4/9/15.
 */
public class fragment1 extends Fragment {
    View rootView;
    Context context;
    private List<String> userList;
    private List<Integer> userpoints;
    private List<MemberListItem> memberList;
    List<Pair> memPair;
    Button invite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GroupsListActivity mac = (GroupsListActivity) getActivity();
        final String id = mac.getGroupID();
        rootView = inflater.inflate(R.layout.fragment1, container, false);
        context = rootView.getContext();
        userList = new ArrayList<String>();
        userpoints = new ArrayList<Integer>();
        memPair = new ArrayList<Pair>();
        invite = (Button)rootView.findViewById(R.id.inviteMember);
        invite.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Addmember.class);
                intent.putExtra("GID", id);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("GroupID", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : parseObjects) {
                        userList.add((String) a.get("UserID"));
                        userpoints.add((Integer) a.get("Points"));
                    }
                    populateListView();
                }
            }

        });

        return rootView;
    }

    private void populateListView() {
        //Create list of items
        //TODO: get member list
        memberList = new ArrayList<MemberListItem>();
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereContainedIn("username", userList);
        query1.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject a : objects) {
                        for (int i=0;i<userList.size();i++) {
                            if (userList.get(i).equals(a.getString("username"))) {
                                memberList.add(new MemberListItem(a.getString("username"), userpoints.get(i),
                                        R.drawable.avatar1));
                                memPair.add(new Pair(a.getString("username"), userpoints.get(i)));
                            }
                        }
                    }

                    //Build adapter
                    ArrayAdapter<MemberListItem> adapter = new MemberListAdapter();

                    //Configure list view
                    ListView list = (ListView) rootView.findViewById(R.id.members_list_view);
                    list.setAdapter(adapter);
                    registerClickCallback();
                }
            }
        });
    }

    //Array Adapter used to create the member list
    private class MemberListAdapter extends ArrayAdapter<MemberListItem> {
        public MemberListAdapter() {
            super(context, R.layout.members_list_view, memberList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.members_list_view, parent, false);
            }

            //Find member list item to work with
            MemberListItem currentItem = memberList.get(position);

            //Fill view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.member_image_view);
            imageView.setImageResource(currentItem.getImageId());

            TextView nameText = (TextView) itemView.findViewById(R.id.member_name_field);
            nameText.setText(currentItem.getName());

            TextView pointsText = (TextView) itemView.findViewById(R.id.member_points_field);
            pointsText.setText(currentItem.getPoints() + "");

            return itemView;
        }
    }

    private void registerClickCallback() {
        ListView list = (ListView) rootView.findViewById(R.id.members_list_view);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                MemberListItem clickedItem = memberList.get(position);
                //String message = "You clicked position " + position + ", which is " + clickedItem.getName();
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MemberDetail.class));
            }
        });
    }

    public class Pair {

        String username;
        int points;

        public Pair(String user, int point) {
            this.username = user;
            this.points = point;
        }

        public int getPoint() {
            return this.points;
        }

        public String getName() {
            return this.username;
        }
    }


}
