package com.example.android.thepomoappandroid.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.ui.dialog.LoginDialog;

/**
 * Created by Enric on 13/04/2015.
 */
public class GroupFragment extends Fragment {

    private LoginDialog loginDialog;

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        loginDialog = LoginDialog.newInstance();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            loadDialog();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadDialog();
        }
    }

    public void loadDialog() {
        if (loginDialog == null) {
            loginDialog = LoginDialog.newInstance();
        }
        if (!loginDialog.isShown()) loginDialog.show(getFragmentManager(), "dialog");
    }
}
