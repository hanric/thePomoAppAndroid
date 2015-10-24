package com.example.android.thepomoappandroid.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.android.thepomoappandroid.R;

import java.util.List;

/**
 * Created by Enric on 12/04/2015.
 */
public class MainFragmentPageAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment> fragments;

    public MainFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments, Context context) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 : return context.getString(R.string.tabAlone);
            case 1 : return context.getString(R.string.tabGroup);
            case 2 : return context.getString(R.string.tabSettings);
            default : return "default";
        }
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }


}
