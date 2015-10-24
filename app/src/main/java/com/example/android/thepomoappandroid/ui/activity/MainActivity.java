package com.example.android.thepomoappandroid.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.response.LoginResponse;
import com.example.android.thepomoappandroid.api.services.InstallationsService;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.ui.adapter.MainFragmentPageAdapter;
import com.example.android.thepomoappandroid.ui.dialog.LoginDialog;
import com.example.android.thepomoappandroid.ui.fragment.AloneFragment;
import com.example.android.thepomoappandroid.ui.fragment.GroupFragment;
import com.example.android.thepomoappandroid.ui.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity
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
        getSupportActionBar().setTitle(getString(R.string.app_name));

        pageAdapter = new MainFragmentPageAdapter(getFragmentManager(), getFragments(), this);
        pager.setAdapter(pageAdapter);
        tabs.setViewPager(pager);
        tabs.getWidth();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(AloneFragment.newInstance());
        fragmentList.add(GroupFragment.newInstance());
        fragmentList.add(SettingsFragment.newInstance());

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem email = menu.findItem(R.id.email);
        MenuItem logout = menu.findItem(R.id.logout);
        if (Utils.isLoggedIn(this)) {
            email.setVisible(true);
            email.setTitle(Utils.getEmail(this));
            logout.setVisible(true);
        } else {
            email.setVisible(false);
            logout.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            PeopleService.getInstance().logout(this, new Callback<ResponseCallback>() {
                @Override
                public void success(ResponseCallback callback, Response response) {
                    handleLogout();
                }

                @Override
                public void failure(RetrofitError error) {
                    handleFailure();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleLogout() {
        InstallationsService.getInstance().delete(Utils.getInstallationId(this), new Callback<ResponseCallback>() {
            @Override
            public void success(ResponseCallback responseCallback, Response response) {
                restart();
            }

            @Override
            public void failure(RetrofitError error) {
                handleFailure();
            }
        });
    }

    private void restart() {
        Utils.clearPreferences(this);
        if (!(pageAdapter.getItem(1) == null)) {
            ((GroupFragment) pageAdapter.getItem(1)).init();
            ((SettingsFragment) pageAdapter.getItem(2)).init();
        }
        invalidateOptionsMenu();
    }

    private void handleFailure() {
        Utils.showError(this);
    }

    @Override
    public void onLoginFromDialog(LoginResponse loginResponse) {
        Log.v("LOGIN_SUCCESS", loginResponse.toString());
        Utils.setToken(this, loginResponse.getId());
        Utils.setUserId(this, loginResponse.getUserId());

        Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();
        if (!(pageAdapter.getItem(1) == null)) {
            ((GroupFragment) pageAdapter.getItem(1)).init();
        }
        invalidateOptionsMenu();
    }
}
