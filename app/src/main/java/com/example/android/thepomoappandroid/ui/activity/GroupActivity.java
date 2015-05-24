package com.example.android.thepomoappandroid.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.thepomoappandroid.Pomodoro;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.alarm.AlarmUtils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.api.services.SessionsService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.ui.adapter.SessionAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddGroupDialog;
import com.example.android.thepomoappandroid.ui.dialog.AddSessionDialog;
import com.github.pavlospt.CircleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.melnykov.fab.FloatingActionButton;

import java.util.GregorianCalendar;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by Enric on 01/05/2015.
 */
public class GroupActivity extends AppCompatActivity implements
        BaseService.OnRetrofitError,
        GroupsService.OnGetSessions,
        GroupsService.OnDeleteGroup,
        SessionsService.OnDeleteSession,
        View.OnClickListener,
        AddGroupDialog.OnActionGroupFromDialog,
        AddSessionDialog.OnActionFromSessionDialog {

    public static final String EXTRA_GROUP = "extra.group";

    private GroupDTO groupDTO;

    private Pomodoro pomodoro;

    private SessionAdapter sessionAdapter;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        findViews();
        Intent intent = getIntent();
        getArguments(intent);
        setListeners();
        setUpRecyclerView();
        setUpToolbar();
        pomodoro = new Pomodoro(circleView);

        refreshActivity();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.listSession);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        circleView = (CircleView) findViewById(R.id.timer);
    }

    private void getArguments(Intent intent) {
        String jsonGroup = intent.getStringExtra(EXTRA_GROUP);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        groupDTO = gson.fromJson(jsonGroup, GroupDTO.class);
    }

    private void setListeners() {
        fab.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        fab.attachToRecyclerView(recyclerView);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupDTO.getName());
        toolbar.setSubtitle(formatSubtitle(groupDTO));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void refreshActivity() {
        GroupsService.getInstance().getSessions(Utils.getInstance().getToken(this), groupDTO.getId(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int userId = Utils.getInstance().getUserId(this);
        if (userId == groupDTO.getAdminId()) {
            getMenuInflater().inflate(R.menu.menu_group_admin, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_group, menu);
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
        if (id == R.id.edit) {
            AddGroupDialog.newInstance(this).show(getSupportFragmentManager(), "dialog");
        } else if (id == R.id.delete) {
            final GroupActivity groupActivity = this;
            new AlertDialog.Builder(this)
                    .setMessage(this.getString(R.string.delete_group))
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            GroupsService.getInstance().delete(Utils.getInstance().getToken(groupActivity), groupDTO.getId(), groupActivity);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

        } else if (id == R.id.exit) {
            // TODO unlink request
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ----------------------------------------------
     * onGetSessions
     * ----------------------------------------------
     */

    @Override
    public void onGetSessions(List<SessionDTO> sessionDTOs) {
        sessionAdapter = new SessionAdapter(this, sessionDTOs);
        sessionAdapter.setOnDeleteSession(this);
        recyclerView.setAdapter(sessionAdapter);
        fab.attachToRecyclerView(recyclerView);
        refreshAlarms(sessionDTOs);
    }

    /**
     * ----------------------------------------------
     * onGetSessions
     * ----------------------------------------------
     */

    @Override
    public void onDeleteGroup() {
        finish();
    }

    @Override
    public void onDeleteSession() {
        refreshActivity();
    }

    @Override
    public void onError(RetrofitError error) {

    }
    /**
     * ----------------------------------------------
     * ----------------------------------------------
     */

    @Override
    public void onActionGroupFromDialog() {
        // TODO call get group to see new info
    }

    @Override
    public void onActionFromSessionDialog() {
        refreshActivity();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == fab.getId()) {
            AddSessionDialog addSessionDialog = AddSessionDialog.newInstance(groupDTO.getId());
            addSessionDialog.setOnActionFromSessionDialog(this);
            addSessionDialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    private void refreshAlarms(List<SessionDTO> sessionDTOs) {
        DBHandler dbHandler = DBHandler.newInstance(this);
        for (SessionDTO sessionDTO : sessionDTOs) {
            if (dbHandler.getSession(sessionDTO.getId()) == null) {
                dbHandler.createSession(sessionDTO);
                // Code for quick testing
//                Date date = new Date();
//                AlarmUtils.initAlarm(getApplicationContext(), date.getTime(), R.string.notification_base_title, R.string.notification_base_content);


                GregorianCalendar startDate = Utils.getInstance().formatStringDate(sessionDTO.getStartTime());
                AlarmUtils.initAlarm(getApplicationContext(), startDate.getTimeInMillis(), R.string.notification_start_title, R.string.notification_start_content);
            }
        }
    }

    private String formatSubtitle(GroupDTO groupDTO) {
        String subtitle = "";
        int i = 0;
        for (PersonDTO personDTO : groupDTO.getPeople()) {
            subtitle += personDTO.getUsername();
            if (i < groupDTO.getPeople().size() - 1) {
                subtitle += ", ";
            }
            ++i;
        }
        return subtitle;
    }
}
