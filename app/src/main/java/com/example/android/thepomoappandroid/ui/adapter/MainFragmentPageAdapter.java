package com.example.android.thepomoappandroid.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Enric on 12/04/2015.
 */
public class MainFragmentPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public MainFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 : return "Alone";
            case 1 : return "Group";
            case 2 : return "Settings";
            default : return "default";
        }
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }


}
