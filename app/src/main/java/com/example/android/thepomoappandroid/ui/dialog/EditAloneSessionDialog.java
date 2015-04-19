package com.example.android.thepomoappandroid.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.AloneSession;

/**
 * Created by Enric on 19/04/2015.
 */
public class EditAloneSessionDialog extends AddAloneSessionDialog {

    private final static String EXTRA_NAME = "extra.name";

    private String currentName;

    public static EditAloneSessionDialog newInstance(String name) {
        EditAloneSessionDialog dialogFragment = new EditAloneSessionDialog();
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_NAME, name);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_add_alone_session, container,
                false);
        findViews(view);
        setListeners();
        getArguments(getArguments());
        fillViews();
        setupToolbar(R.string.toolbar_edit_session);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    private void getArguments(Bundle bundle) {
        currentName = bundle.getString(EXTRA_NAME);
    }

    private void fillViews() {
        AloneSession aloneSession = dbHandler.getAloneSession(currentName);
        name.setText(aloneSession.getName());
        num.setText(Integer.toString(aloneSession.getNum()));
    }

    @Override
    protected void performSaveAction() {
        //TODO update the alone session
    }
}
