package com.example.android.thepomoappandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.ui.adapter.GroupAdapter;
import com.example.android.thepomoappandroid.ui.dialog.AddGroupDialog;
import com.example.android.thepomoappandroid.ui.dialog.LoginDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by Enric on 13/04/2015.
 */
public class GroupFragment extends Fragment implements
        View.OnClickListener,
        BaseService.OnRetrofitError,
        PeopleService.OnGetGroups,
        AddGroupDialog.OnActionGroupFromDialog,
        GroupsService.OnDeleteGroup {

    private LoginDialog loginDialog;

    private GroupAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

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
        setUpRecyclerView();
        refreshFragment();
        return view;
    }

    private void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.groupCardList);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void setListeners() {
        fab.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        fab.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Utils.getInstance().isLoggedIn(getActivity()) && getUserVisibleHint()) {
            loadDialog();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null && !Utils.getInstance().isLoggedIn(getActivity())) {
            loadDialog();
        }
    }

    public void loadDialog() {
        if (loginDialog == null) {
            loginDialog = LoginDialog.newInstance();
        }
        if (!loginDialog.isShown()) loginDialog.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == fab.getId()) {
            AddGroupDialog.newInstance(this).show(getFragmentManager(), "dialog");
        }
    }

    public void refreshFragment() {
        Utils utils = Utils.getInstance();
        PeopleService.getInstance().getGroups(utils.getUserId(getActivity()), utils.getToken(getActivity()), this);
    }

    /**
     * ----------------------------------------------
     * OnGetGroups
     * ----------------------------------------------
     */

    @Override
    public void onGetGroups(List<GroupDTO> groupDTOs) {
        adapter = new GroupAdapter(getActivity(), groupDTOs);
        adapter.setOnDeleteGroup(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * ----------------------------------------------
     * OnDeleteGroup
     * ----------------------------------------------
     */

    @Override
    public void onDeleteGroup() {
        refreshFragment();
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
        refreshFragment();
    }
}
