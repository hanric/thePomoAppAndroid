package com.example.android.thepomoappandroid.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.ui.adapter.GroupAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddGroupDialog;
import com.example.android.thepomoappandroid.ui.dialog.LoginDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 13/04/2015.
 */
public class GroupFragment extends Fragment implements
        View.OnClickListener,
        AddGroupDialog.OnActionGroupFromDialog {

    private LoginDialog loginDialog;

    private GroupAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Timer popupLoginTimer;

    private Callback<ResponseCallback> onDeleteGroupCallback = new Callback<ResponseCallback>() {
        @Override
        public void success(ResponseCallback callback, Response response) {
            refreshFragment();
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        loginDialog = LoginDialog.newInstance();
        findViews(view);
        setListeners();
        init();
        return view;
    }

    private void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.groupCardList);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void setListeners() {
        fab.setOnClickListener(this);
    }

    private void init() {
        setUpRecyclerView();
        refreshFragment();
        setFabVisibility();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        fab.attachToRecyclerView(recyclerView);
    }

    private void setFabVisibility() {
        if (Utils.isLoggedIn(getActivity())) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Utils.isLoggedIn(getActivity()) && getUserVisibleHint()) {
            loadDialog();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null && !Utils.isLoggedIn(getActivity())) {
            loadDialog();
        } else if (!isVisibleToUser) {
            if (popupLoginTimer != null) {
                popupLoginTimer.cancel();
            }
        }
    }

    public void loadDialog() {
        if (loginDialog == null) {
            loginDialog = LoginDialog.newInstance();
        }
        if (!loginDialog.isShown()) {
            popupLoginTimer = new Timer();
            popupLoginTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    loginDialog.show(getFragmentManager(), "dialog");
                }
            }, 400);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == fab.getId()) {
            AddGroupDialog.newInstance(this).show(getFragmentManager(), "dialog");
        }
    }

    public void refreshFragment() {
        if (!Utils.isLoggedIn(getActivity())) {
            if (getUserVisibleHint()) loadDialog();
            adapter = new GroupAdapter(getActivity(), new ArrayList<GroupDTO>());
            recyclerView.setAdapter(adapter);
        } else {
            PeopleService.getInstance().getGroups(Utils.getUserId(getActivity()), Utils.getToken(getActivity()), new Callback<List<GroupDTO>>() {
                @Override
                public void success(List<GroupDTO> groupDTOs, Response response) {
                    handleOnGetGroupsSuccess(groupDTOs);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    public void handleOnGetGroupsSuccess(List<GroupDTO> groupDTOs) {
        adapter = new GroupAdapter(getActivity(), groupDTOs);
        adapter.setOnDeleteGroupCallback(onDeleteGroupCallback);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActionGroupFromDialog() {
        init();
    }
}
