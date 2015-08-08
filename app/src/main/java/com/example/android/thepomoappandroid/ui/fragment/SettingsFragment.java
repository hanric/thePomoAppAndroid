package com.example.android.thepomoappandroid.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.db.Setting;
import com.example.android.thepomoappandroid.ui.adapter.SettingAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddSettingDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hanric on 6/7/15.
 */
public class SettingsFragment extends Fragment
        implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        AddSettingDialog.OnActionFromSettingDialog {

    private DBHandler dbHandler;
    private SettingAdapter adapter;

    private ListView listView;
    private FloatingActionButton fab;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        dbHandler = DBHandler.newInstance(getActivity());
        findViews(view);
        setListeners();
        init();
        return view;
    }

    private void findViews(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void setListeners() {
        fab.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void init() {

        fab.attachToListView(listView);

        if (Utils.isNetworkAvailable(getActivity())) {
            refreshFragment();
        } else {
            updateViewsFromLocalDB();
        }
    }

    private void refreshFragment() {
        PeopleService.getInstance().getSettings(getActivity(), new Callback<List<SettingDTO>>() {
            @Override
            public void success(List<SettingDTO> settingDTOs, Response response) {
                updateLocalSettings(settingDTOs);
                updateViewsFromLocalDB();
            }

            @Override
            public void failure(RetrofitError error) {
                updateViewsFromLocalDB();
            }
        });
    }

    private void updateLocalSettings(List<SettingDTO> settingDTOs) {
        for (SettingDTO settingDTO : settingDTOs) {
            if (dbHandler.getSetting(settingDTO.getId()) == null) {
                dbHandler.createSetting(settingDTO);
            }
        }
    }

    private void updateViewsFromLocalDB() {
        RealmResults<Setting> results = DBHandler.newInstance(getActivity()).getSettings();
        adapter = new SettingAdapter(getActivity(), R.id.listView, results, true);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == fab.getId()) {
            AddSettingDialog addSettingDialog = AddSettingDialog.newInstance();
            addSettingDialog.setOnActionFromSettingDialog(this);
            addSettingDialog.show(getFragmentManager(), "dialog");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO show edit screen
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
        // TODO show delete dialog
    }

    @Override
    public void onActionFromSessionDialog() {
        init();
    }
}
