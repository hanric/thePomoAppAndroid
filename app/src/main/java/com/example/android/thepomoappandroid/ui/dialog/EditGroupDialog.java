package com.example.android.thepomoappandroid.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.internal.Util;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 02/05/2015.
 */
public class EditGroupDialog extends AddGroupDialog {

    public static final String EXTRA_GROUP = "extra.group";

    private GroupDTO groupDTO;

    public static EditGroupDialog newInstance(OnActionGroupFromDialog onActionGroupFromDialog, GroupDTO groupDTO) {
        EditGroupDialog dialogFragment = new EditGroupDialog();
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        dialogFragment.onActionGroupFromDialog = onActionGroupFromDialog;
        Bundle bundle = new Bundle();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        bundle.putString(EXTRA_GROUP, gson.toJson(groupDTO));
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_add_group, container,
                false);
        findViews(view);
        setListeners();
        getArguments(getArguments());
        fillViews();
        setupToolbar(R.string.toolbar_edit_group);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    private void getArguments(Bundle bundle) {
        String jsonGroup = bundle.getString(EXTRA_GROUP);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        groupDTO = gson.fromJson(jsonGroup, GroupDTO.class);
    }

    private void fillViews() {
        name.setText(groupDTO.getName());
        description.setText(groupDTO.getDescription());
    }

    @Override
    protected void actionGroup(String name, String description) {
        String token = Utils.getToken(getActivity());
        GroupsService.getInstance().update(token, groupDTO.getId(), name, description, new Callback<GroupDTO>() {
            @Override
            public void success(GroupDTO groupDTO, Response response) {
                onActionGroupFromDialog.onActionGroupFromDialog();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        dismiss();
    }
}
