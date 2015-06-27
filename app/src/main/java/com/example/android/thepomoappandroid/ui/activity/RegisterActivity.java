package com.example.android.thepomoappandroid.ui.activity;

import android.app.ProgressDialog;
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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 04/05/2015.
 */
public class RegisterActivity extends AppCompatActivity
    implements View.OnClickListener {

    public static final int REGISTER_OK = 1;
    public static final int REGISTER_ERR = 0;
    public static final int NO_REGISTER = 0;

    ProgressDialog progressDialog;

    private Toolbar toolbar;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        getSupportActionBar().setTitle(R.string.popup_login_signup);
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
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading));
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        PeopleService.getInstance().register(emailString, nameString, passwordString, new Callback<PersonDTO>() {
            @Override
            public void success(PersonDTO personDTO, Response response) {
                setResult(REGISTER_OK);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                setResult(REGISTER_ERR);
                finish();
            }
        });
    }
}
