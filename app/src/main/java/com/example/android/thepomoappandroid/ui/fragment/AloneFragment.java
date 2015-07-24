package com.example.android.thepomoappandroid.ui.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.thepomoappandroid.Pomodoro;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.AloneSession;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.ui.adapter.ListAloneSessionAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddAloneSessionDialog;
import com.example.android.thepomoappandroid.ui.dialog.EditAloneSessionDialog;
import com.github.pavlospt.CircleView;
import com.melnykov.fab.FloatingActionButton;

import java.util.GregorianCalendar;

/**
 * Created by Enric on 12/04/2015.
 */
public class AloneFragment extends Fragment implements
        View.OnClickListener,
        ListView.OnItemClickListener,
        ListView.OnItemLongClickListener,
        Pomodoro.OnPomodoroFinished {

    private DBHandler dbHandler;

    private Pomodoro pomodoro;

    private ListAloneSessionAdapter adapter;

    private ListView listViewLocalSessions;
    private FloatingActionButton fab;
    private CircleView circleView;

    public static AloneFragment newInstance() {
        AloneFragment fragment = new AloneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = DBHandler.newInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alone, container, false);

        findViews(view);
        pomodoro = new Pomodoro(circleView);
        setListeners();

        adapter = new ListAloneSessionAdapter(getActivity(), R.id.listSession, dbHandler.getAloneSessions(), true);
        listViewLocalSessions.setAdapter(adapter);
        listViewLocalSessions.setOnItemClickListener(this);
        listViewLocalSessions.setOnItemLongClickListener(this);
        fab.attachToListView(listViewLocalSessions);
        return view;
    }

    public void findViews(View view) {
        listViewLocalSessions = (ListView) view.findViewById(R.id.listSession);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        circleView = (CircleView) view.findViewById(R.id.timer);
    }

    public void setListeners() {
        fab.setOnClickListener(this);
        circleView.setOnClickListener(this);
        pomodoro.setOnPomodoroFinished(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == fab.getId()) {
            showAddAloneSessionDialog();
        } else if (id == circleView.getId()) {
            if (!pomodoro.isRunning()) {
                startSession();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = ((TextView) view.findViewById(R.id.header)).getText().toString();
        showEditAloneSessionDialog(name);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        new AlertDialog.Builder(getActivity())
                .setMessage(getActivity().getString(R.string.delete_session))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = ((TextView) view.findViewById(R.id.header)).getText().toString();
                        dbHandler.deleteAloneSession(name);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
        return true;
    }

    private void showAddAloneSessionDialog() {
        AddAloneSessionDialog addAloneSessionDialog = AddAloneSessionDialog.newInstance();
        addAloneSessionDialog.show(getFragmentManager(), "dialog");
    }

    private void showEditAloneSessionDialog(String name) {
        EditAloneSessionDialog editAloneSessionDialog = EditAloneSessionDialog.newInstance(name);
        editAloneSessionDialog.show(getFragmentManager(), "dialog");
    }

    private void startSession() {
        if (!adapter.isEmpty()) {
            AloneSession aloneSession = adapter.getItem(0);
            GregorianCalendar workTime = new GregorianCalendar(0, 0, 0, 0, 1, 0);
            GregorianCalendar breakTime = new GregorianCalendar(0, 0, 0, 0, 1, 0);
            GregorianCalendar largeBreakTime = new GregorianCalendar(0, 0, 0, 0, 1, 0);
            pomodoro.setSession(aloneSession.getName(), workTime, breakTime, largeBreakTime, aloneSession.getNum(), null);
            dbHandler.updateAloneSession(aloneSession.getName(), aloneSession.getName(), aloneSession.getNum(), Pomodoro.WORK);
            pomodoro.startSession();
        }
    }

    @Override
    public void phaseEnded(String key, int nextPhase) {
        Toast.makeText(getActivity(), "phaseEnded", Toast.LENGTH_SHORT).show();
        AloneSession aloneSession = dbHandler.getAloneSession(key);
        // If the nextPhase is not WORK, that means that the WORK phase just happened
        int newNum = aloneSession.getNum();
        if (nextPhase != Pomodoro.WORK) {
            newNum = newNum -1;
        }
        dbHandler.updateAloneSession(aloneSession.getName(), aloneSession.getName(), newNum, nextPhase);
    }

    @Override
    public void sessionEnded(String key) {
        dbHandler.deleteAloneSession(key);
    }
}
