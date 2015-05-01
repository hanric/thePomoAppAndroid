package com.example.android.thepomoappandroid.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.GroupsService;

import retrofit.RetrofitError;

/**
 * Created by Enric on 19/04/2015.
 */
public class AddGroupDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener, View.OnClickListener, BaseService.OnRetrofitError, GroupsService.OnCreateGroup {

    protected Toolbar toolbar;
    protected EditText name;
    protected EditText description;
    protected Button buttonAddMembers;

    private OnCreateGroupFromDialog onCreateGroupFromDialog;

    public interface OnCreateGroupFromDialog {
        void onCreateGroupFromDialog();
    }

    public static AddGroupDialog newInstance(OnCreateGroupFromDialog onCreateGroupFromDialog) {
        AddGroupDialog dialogFragment = new AddGroupDialog();
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        dialogFragment.onCreateGroupFromDialog = onCreateGroupFromDialog;
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_add_group, container,
                false);
        findViews(view);
        setListeners();
        setupToolbar(R.string.toolbar_add_group);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    protected void findViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        name = (EditText) view.findViewById(R.id.popupAddGroup_name);
        description = (EditText) view.findViewById(R.id.popupAddGroup_description);
        buttonAddMembers = (Button) view.findViewById(R.id.popupAddGroup_addMembers);
    }

    protected void setListeners() {
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        buttonAddMembers.setOnClickListener(this);
    }

    public void setupToolbar(int titleResId) {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle(titleResId);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.save_menu);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == toolbar.getChildAt(0).getId()) {
            dismiss();
        } else if (id == buttonAddMembers.getId()) {
            // TODO launch add members
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.save) {
            // TODO handle save group
            String nameString = name.getText().toString();
            String descriptionString = description.getText().toString();
            if (!nameString.isEmpty() && !descriptionString.isEmpty()) {
                String token = Utils.getInstance().getToken(getActivity());
                GroupsService.getInstance().create(token, nameString, descriptionString, this);
                dismiss();
            }
        }
        return false;
    }

    @Override
    public void onCreate(GroupDTO groupDTO) {
        onCreateGroupFromDialog.onCreateGroupFromDialog();
        dismiss();
    }

    @Override
    public void onError(RetrofitError error) {

    }
}
