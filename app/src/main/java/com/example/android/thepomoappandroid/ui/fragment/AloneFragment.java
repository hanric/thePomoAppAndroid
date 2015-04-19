package com.example.android.thepomoappandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.AloneSession;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.ui.adapter.ListAloneSessionAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddAloneSessionDialog;
import com.example.android.thepomoappandroid.ui.dialog.EditAloneSessionDialog;
import com.github.pavlospt.CircleView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Enric on 12/04/2015.
 */
public class AloneFragment extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {

    private DBHandler dbHandler;

    ArrayList<AloneSession> arrayAloneSessions;

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
        setListeners();

        ListAloneSessionAdapter adapter = new ListAloneSessionAdapter(getActivity(), R.id.listLocalSession, dbHandler.getAloneSessions(), true);
        listViewLocalSessions.setAdapter(adapter);
        listViewLocalSessions.setOnItemClickListener(this);
        fab.attachToListView(listViewLocalSessions);
        return view;
    }

    public void findViews(View view) {
        listViewLocalSessions = (ListView) view.findViewById(R.id.listLocalSession);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        circleView = (CircleView) view.findViewById(R.id.aloneTimer);
    }

    public void setListeners() {
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == fab.getId()) {
            showAddAloneSessionDialog();
//            Pomodoro pomodoro = new Pomodoro(circleView);
//            GregorianCalendar workTime = new GregorianCalendar(0, 0, 0, 0, 25, 0);
//            GregorianCalendar breakTime = new GregorianCalendar(0, 0, 0, 0, 5, 0);
//            GregorianCalendar largeBreakTime = new GregorianCalendar(0, 0, 0, 0, 15, 0);
//            pomodoro.setSession(workTime, breakTime, largeBreakTime, 3, null);
//            pomodoro.startSession();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = ((TextView) view.findViewById(R.id.sb_header)).getText().toString();
        showEditAloneSessionDialog(name);
    }

    public void showAddAloneSessionDialog() {
        AddAloneSessionDialog addAloneSessionDialog = AddAloneSessionDialog.newInstance();
        addAloneSessionDialog.show(getFragmentManager(), "dialog");
    }

    public void showEditAloneSessionDialog(String name) {
        EditAloneSessionDialog editAloneSessionDialog = EditAloneSessionDialog.newInstance(name);
        editAloneSessionDialog.show(getFragmentManager(), "dialog");
    }
}
