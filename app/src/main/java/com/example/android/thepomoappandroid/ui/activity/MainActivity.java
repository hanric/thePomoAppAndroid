package com.example.android.thepomoappandroid.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.ui.adapter.MainFragmentPageAdapter;
import com.example.android.thepomoappandroid.ui.fragment.AloneFragment;
import com.example.android.thepomoappandroid.ui.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

//    ArrayList<LocalSession> arrayLocalSessions;
//    ListView listViewLocalSessions;

    Toolbar toolbar;
    private ViewPager pager;
    PagerSlidingTabStrip tabs;

    MainFragmentPageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pomodoro for Groups");

        pageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager(), getFragments());
        pager.setAdapter(pageAdapter);
        tabs.setViewPager(pager);
        tabs.getWidth();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(AloneFragment.newInstance());
        fragmentList.add(GroupFragment.newInstance());

        return fragmentList;
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
