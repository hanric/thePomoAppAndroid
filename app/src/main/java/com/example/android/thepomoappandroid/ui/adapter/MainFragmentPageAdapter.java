package com.example.android.thepomoappandroid.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
            default : return "default";
        }
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }


}
