package com.example.android.thepomoappandroid.ui.dialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.android.thepomoappandroid.Constants;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.response.LoginResponse;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.ui.activity.MainActivity;

import retrofit.RetrofitError;

/**
 * Created by Enric on 13/04/2015.
 */
public class LoginDialog extends DialogFragment implements
        View.OnClickListener, BaseService.OnRetrofitError, PeopleService.OnLogin {

    private boolean shown = false;

    private String emailText;

    private EditText email;
    private EditText password;
    private CheckBox remember;
    private Button login;
    private Button register;

    public interface OnLoginFromDialog {
        void onLoginFromDialog(LoginResponse loginResponse);
    }

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
            login();
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

    private void login() {
        String emailField = email.getText().toString();
        emailText = emailField;
        String passwordField = password.getText().toString();
        PeopleService.getInstance().login(emailField, passwordField, this);
    }


    /**
     * ----------------------------------------------
     * OnLogin
     * ----------------------------------------------
     */

    @Override
    public void onLogin(LoginResponse loginResponse) {
        SharedPreferences prefs = Utils.getInstance().getPrefs(getActivity());
        prefs.edit().putString(Constants.PREFS_EMAIL, emailText).apply();
        ((MainActivity) getActivity()).onLoginFromDialog(loginResponse);
        dismiss();
    }

    @Override
    public void onError(RetrofitError error) {
        Toast.makeText(getActivity(), "Incorrect user or password", Toast.LENGTH_SHORT).show();
    }

    /**
     * ----------------------------------------------
     * ----------------------------------------------
     */
}
