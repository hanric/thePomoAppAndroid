package com.example.android.thepomoappandroid.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.android.thepomoappandroid.Constants;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.response.LoginResponse;
import com.example.android.thepomoappandroid.ui.adapter.MainFragmentPageAdapter;
import com.example.android.thepomoappandroid.ui.dialog.LoginDialog;
import com.example.android.thepomoappandroid.ui.fragment.AloneFragment;
import com.example.android.thepomoappandroid.ui.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
    implements LoginDialog.OnLoginFromDialog {

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

    @Override
    public void onLoginFromDialog(LoginResponse loginResponse) {
        Log.v("LOGIN_SUCCESS", loginResponse.toString());
        SharedPreferences prefs = Utils.getInstance().getPrefs(this);
        prefs.edit().putString(Constants.PREFS_TOKEN, loginResponse.getId()).apply();
        prefs.edit().putInt(Constants.PREFS_USERID, loginResponse.getUserId()).apply();

        Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();
        if (!(pageAdapter.getItem(1) == null)) {
            ((GroupFragment) pageAdapter.getItem(1)).refreshFragment();
        }
    }
}
