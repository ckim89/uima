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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielkim on 4/9/15.
 */
public class fragment1 extends Fragment {
    View rootView;
    Context context;

    private List<MemberListItem> memberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment1, container, false);
        context = rootView.getContext();

        populateListView();
        registerClickCallback();

        return rootView;
    }

    private void populateListView() {
        //Create list of items
        //TODO: get member list
        memberList = new ArrayList<MemberListItem>();
        memberList.add(new MemberListItem("Andrew Ding", 20, R.drawable.avatar1));
        memberList.add(new MemberListItem("Kevin Zhang", 1, R.drawable.avatar1));
        memberList.add(new MemberListItem("Daniel Kim", 9999, R.drawable.avatar1));
        memberList.add(new MemberListItem("That Guy", 0, R.drawable.avatar1));
        memberList.add(new MemberListItem("Joanne Selinski", 3151151, R.drawable.avatar1));

        //Build adapter
        ArrayAdapter<MemberListItem> adapter = new MemberListAdapter();

        //Configure list view
        ListView list = (ListView) rootView.findViewById(R.id.members_list_view);
        list.setAdapter(adapter);
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


}
