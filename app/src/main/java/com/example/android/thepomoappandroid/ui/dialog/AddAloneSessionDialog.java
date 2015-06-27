package com.example.android.thepomoappandroid.ui.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.thepomoappandroid.Pomodoro;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.DBHandler;

import io.realm.exceptions.RealmException;

/**
 * Created by Enric on 19/04/2015.
 */
public class  AddAloneSessionDialog extends DialogFragment implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    protected DBHandler dbHandler;

    protected Toolbar toolbar;
    protected EditText name;
    protected EditText num;

    public static AddAloneSessionDialog newInstance() {
        AddAloneSessionDialog dialogFragment = new AddAloneSessionDialog();
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = DBHandler.newInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_add_alone_session, container,
                false);
        findViews(view);
        setListeners();
        setupToolbar(R.string.toolbar_add_session);
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
        name = (EditText) view.findViewById(R.id.popupAddAloneSession_name);
        num = (EditText) view.findViewById(R.id.popupAddAloneSession_num);
    }

    protected void setListeners() {
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
    }

    public void setupToolbar(int titleResId) {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle(titleResId);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_save);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == toolbar.getChildAt(0).getId()) {
            dismiss();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.save) {
            performSaveAction();
        }
        return false;
    }

    protected void performSaveAction() {
        try {
            dbHandler.createAloneSession(name.getText().toString(), Integer.parseInt(num.getText().toString()), Pomodoro.TO_START);
            dismiss();
        } catch (RealmException e) {
            Toast.makeText(getActivity(), "TODO this name already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
