package com.example.android.thepomoappandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.ui.adapter.ListLocalSessionAdapter;
import com.example.android.thepomoappandroid.vo.LocalSession;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Enric on 12/04/2015.
 */
public class AloneFragment extends Fragment {

    ArrayList<LocalSession> arrayLocalSessions;
    ListView listViewLocalSessions;

    public static AloneFragment newInstance() {
        AloneFragment fragment = new AloneFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alone, container, false);
        arrayLocalSessions = new ArrayList<LocalSession>();
        LocalSession plats = new LocalSession(0, 3, "Fregar els plats");
        LocalSession planxar = new LocalSession(1, 1, "Cal planxar");
        LocalSession fregar = new LocalSession(2, 2, "Fregar la casa");
        LocalSession estudiar = new LocalSession(3, 3, "Estudiar");

        arrayLocalSessions.add(plats);
        arrayLocalSessions.add(planxar);
        arrayLocalSessions.add(fregar);
        arrayLocalSessions.add(estudiar);

        listViewLocalSessions = (ListView) view.findViewById(R.id.listLocalSession);
        ListLocalSessionAdapter adapter = new ListLocalSessionAdapter(getActivity(), arrayLocalSessions);
        listViewLocalSessions.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(listViewLocalSessions);
        return view;
    }
}
