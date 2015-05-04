package com.example.android.thepomoappandroid.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.PeopleService;

import retrofit.RetrofitError;

/**
 * Created by Enric on 04/05/2015.
 */
public class RegisterActivity extends AppCompatActivity
    implements View.OnClickListener,
        BaseService.OnRetrofitError,
        PeopleService.OnRegister {

    public static final int REGISTER_OK = 1;
    public static final int REGISTER_ERR = 0;
    public static final int NO_REGISTER = 0;

    private Toolbar toolbar;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        findViews();
        setListeners();
        setUpToolbar();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (EditText) findViewById(R.id.register_name);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirmPassword);
        registerButton = (Button) findViewById(R.id.register_button);
    }

    private void setListeners() {
        registerButton.setOnClickListener(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.popup_login_signup);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(NO_REGISTER);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == registerButton.getId()) {
            // TODO handle validation
            register();
        }
    }

    private void register() {
        String nameString = name.getText().toString();
        String emailString = name.getText().toString();
        String passwordString = name.getText().toString();

        PeopleService.getInstance().register(emailString, nameString, passwordString, this);
    }

    @Override
    public void onRegister(PersonDTO personDTO) {
        setResult(REGISTER_OK);
        finish();
    }

    @Override
    public void onError(RetrofitError error) {
        setResult(REGISTER_ERR);
        finish();
    }
}
