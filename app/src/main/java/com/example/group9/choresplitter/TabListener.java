package com.example.group9.choresplitter;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by the.B on 3/5/2015.
 */
public class TabListener implements ActionBar.TabListener {

    private Fragment fragment;
    private GroupsListActivity main;

    // Constructor
    public TabListener(Fragment fragment) {
        this.fragment = fragment;
    }

    // Highlight tab in activity when it is selected
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.main, fragment);
    }

    // Mute unselected tab
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Do nothing
    }

}
