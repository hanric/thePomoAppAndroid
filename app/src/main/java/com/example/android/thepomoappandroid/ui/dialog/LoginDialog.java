package com.example.android.thepomoappandroid.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.android.thepomoappandroid.R;

/**
 * Created by Enric on 13/04/2015.
 */
public class LoginDialog extends DialogFragment implements View.OnClickListener {

    private boolean shown = false;

    private EditText email;
    private EditText password;
    private CheckBox remember;
    private Button login;
    private Button register;

    public static LoginDialog newInstance() {
        LoginDialog dialogFragment = new LoginDialog();
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_login, container,
                false);
        findViews(view);
        setListeners();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    private void findViews(View view) {
        email = (EditText) view.findViewById(R.id.popup_login_email);
        password = (EditText) view.findViewById(R.id.popup_login_password);
        remember = (CheckBox) view.findViewById(R.id.popup_login_remember);
        login = (Button) view.findViewById(R.id.popup_login_button);
        register = (Button) view.findViewById(R.id.popup_login_register_button);
    }

    private void setListeners() {
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == login.getId()) {
            dismiss();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) return;

        super.show(manager, tag);
        shown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        shown = false;
        super.onDismiss(dialog);
    }

    public boolean isShown() {
        return shown;
    }
}
