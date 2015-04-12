package com.example.android.thepomoappandroid.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.ui.adapter.MainPageAdapter;
import com.example.android.thepomoappandroid.ui.fragment.AloneFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

//    ArrayList<LocalSession> arrayLocalSessions;
//    ListView listViewLocalSessions;

    MainPageAdapter pageAdapter;

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = getFragments();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pomodoro for Groups");

        pageAdapter = new MainPageAdapter(getSupportFragmentManager(), getFragments());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);
//        arrayLocalSessions = new ArrayList<LocalSession>();
//        LocalSession plats = new LocalSession(0,3, "Fregar els plats");
//        LocalSession planxar = new LocalSession(1,1, "Cal planxar");
//        LocalSession fregar = new LocalSession(2,2, "Fregar la casa");
//        LocalSession estudiar = new LocalSession(3,3, "Estudiar");
//
//        arrayLocalSessions.add(plats);
//        arrayLocalSessions.add(planxar);
//        arrayLocalSessions.add(fregar);
//        arrayLocalSessions.add(estudiar);
//
//        listViewLocalSessions = (ListView) findViewById(R.id.listLocalSession);
//        ListLocalSessionAdapter adapter = new ListLocalSessionAdapter(this, arrayLocalSessions);
//        listViewLocalSessions.setAdapter(adapter);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(AloneFragment.newInstance());
        fragmentList.add(AloneFragment.newInstance());
        fragmentList.add(AloneFragment.newInstance());

        return fragmentList;
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
