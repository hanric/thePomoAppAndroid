package com.example.android.thepomoappandroid.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.thepomoappandroid.GroupPomodoro;
import com.example.android.thepomoappandroid.Pomodoro;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.alarm.AlarmUtils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.api.services.SessionsService;
import com.example.android.thepomoappandroid.api.services.SettingsService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.db.Session;
import com.example.android.thepomoappandroid.ui.adapter.SessionAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddGroupDialog;
import com.example.android.thepomoappandroid.ui.dialog.AddSessionDialog;
import com.github.pavlospt.CircleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.melnykov.fab.FloatingActionButton;

import java.util.GregorianCalendar;
import java.util.List;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 01/05/2015.
 */
public class GroupActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        AddGroupDialog.OnActionGroupFromDialog,
        AddSessionDialog.OnActionFromSessionDialog,
        Pomodoro.OnPomodoroFinished {

    public static final String EXTRA_GROUP = "extra.group";

    private GroupDTO groupDTO;

    private GroupPomodoro pomodoro;

    private SessionAdapter sessionAdapter;

    private Toolbar toolbar;
    private ListView listView;
    private FloatingActionButton fab;
    private CircleView circleView;

    private Callback<ResponseCallback> onDeleteSessionCallback = new Callback<ResponseCallback>() {
        @Override
        public void success(ResponseCallback callback, Response response) {
            refreshActivity();
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        findViews();
        pomodoro = new GroupPomodoro(circleView);
        Intent intent = getIntent();
        getArguments(intent);
        setListeners();
        setUpToolbar();

        if (Utils.isNetworkAvailable(this)) {
            refreshActivity();
        } else {
            updateViewsFromLocalDB();
        }
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listSession);
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
        listView.setOnItemClickListener(this);
        pomodoro.setOnPomodoroFinished(this);
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
        GroupsService.getInstance().getSessions(Utils.getToken(this), groupDTO.getId(), new Callback<List<SessionDTO>>() {
            @Override
            public void success(List<SessionDTO> sessionDTOs, Response response) {
                handleGetSessions(sessionDTOs);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void handleGetSessions(List<SessionDTO> sessionDTOs) {
        syncLocalData(sessionDTOs);
        updateViewsFromLocalDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int userId = Utils.getUserId(this);
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
            AddGroupDialog.newInstance(this).show(getFragmentManager(), "dialog");
        } else if (id == R.id.delete) {
            final GroupActivity groupActivity = this;
            new AlertDialog.Builder(this)
                    .setMessage(this.getString(R.string.delete_group))
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            GroupsService.getInstance().delete(Utils.getToken(groupActivity), groupDTO.getId(), new Callback<ResponseCallback>() {
                                @Override
                                public void success(ResponseCallback callback, Response response) {
                                    finish();
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Session session = sessionAdapter.getItem(i);
        new AlertDialog.Builder(this)
                .setMessage(this.getString(R.string.delete_session))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSession(session.getId());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void syncLocalData(List<SessionDTO> sessionDTOs) {
        DBHandler dbHandler = DBHandler.newInstance(this);
        for (SessionDTO sessionDTO : sessionDTOs) {
            if (dbHandler.getSession(sessionDTO.getId()) == null) {
                dbHandler.createSession(sessionDTO);
                GregorianCalendar startDate = Utils.formatStringDate(sessionDTO.getStartTime());
                AlarmUtils.initAlarm(getApplicationContext(), startDate, R.string.notification_start_title, R.string.notification_start_content);
            }
        }
    }

    private void updateViewsFromLocalDB() {
        RealmResults<Session> sessionList = DBHandler.newInstance(this).getSessionsByGroup(groupDTO.getId());
        sessionAdapter = new SessionAdapter(this, R.id.listSession, sessionList, true);
        listView.setAdapter(sessionAdapter);
        fab.attachToListView(listView);
        runFirstSession();
    }

    private void runFirstSession() {
        if (!sessionAdapter.isEmpty()) {
            boolean found = false;
            Session session = new Session();
            GregorianCalendar now = new GregorianCalendar();
            for (int i = 0; i < sessionAdapter.getCount() && !found; ++i) {
                if (Utils.formatStringDate(sessionAdapter.getItem(i).getStartTime()).before(now) && Utils.formatStringDate(sessionAdapter.getItem(i).getEndTime()).after(now)) {
                    session = sessionAdapter.getItem(i);
                    found = true;
                }
            }
            if (found) {
                final Session pomodoroSession = session;
                SettingsService.getInstance().findById(Utils.getToken(this), session.getSettingId(), new Callback<SettingDTO>() {
                    @Override
                    public void success(SettingDTO settingDTO, Response response) {
                        GregorianCalendar workTime = new GregorianCalendar(0, 0, 0, 0, settingDTO.getWorkTime(), 0);
                        GregorianCalendar breakTime = new GregorianCalendar(0, 0, 0, 0, settingDTO.getRestTime(), 0);
                        GregorianCalendar largeBreakTime = new GregorianCalendar(0, 0, 0, 0, settingDTO.getLargeRestTime(), 0);
                        GregorianCalendar startDate = Utils.formatStringDate(pomodoroSession.getStartTime());
                        pomodoro.setSession(pomodoroSession.getName(), workTime, breakTime, largeBreakTime, pomodoroSession.getNPomos(), startDate); // it already starts the session
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showError();
                    }
                });
            }
        }
    }

    @Override
    public void phaseEnded(String key, int nextPhase) {
        Toast.makeText(this, "phaseEnded", Toast.LENGTH_SHORT).show();
        // TODO update state to change colors of the session item
    }

    @Override
    public void sessionEnded(String key) {
        refreshActivity();
    }

    private void deleteSession(int sessionId) {
        SessionsService.getInstance().delete(Utils.getToken(this), sessionId, onDeleteSessionCallback);
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

    private void showError() {
        Utils.showError(this);
    }
}
