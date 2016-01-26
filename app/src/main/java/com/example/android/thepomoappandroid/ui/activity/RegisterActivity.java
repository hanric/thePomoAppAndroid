package com.example.android.thepomoappandroid.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.api.dto.PersonDTO;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 04/05/2015.
 */
public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener,
        Validator.ValidationListener {

    public static final int REGISTER_OK = 1;
    public static final int REGISTER_ERR = 0;
    public static final int NO_REGISTER = 0;

    ProgressDialog progressDialog;

    private Toolbar toolbar;

    private Validator validator;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Length(sequence = 2, min = 4, max = 12, messageResId = R.string.usernameError)
    private EditText name;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Email(sequence = 2, messageResId = R.string.emailError)
    private EditText email;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Password(sequence = 2, messageResId = R.string.passwordError, min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    private EditText password;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @ConfirmPassword(sequence = 2, messageResId = R.string.confirmPasswordError)
    private EditText confirmPassword;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(TextInputLayout.class,
                new ViewDataAdapter<TextInputLayout, String>() {
                    @Override
                    public String getData(TextInputLayout flet) throws ConversionException {
                        return flet.getEditText().getText().toString();
                    }
                }
        );
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
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        register();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ((TextInputLayout) name.getParent()).setError(null);
        ((TextInputLayout) name.getParent()).setErrorEnabled(false);
        ((TextInputLayout) email.getParent()).setError(null);
        ((TextInputLayout) email.getParent()).setErrorEnabled(false);
        ((TextInputLayout) password.getParent()).setError(null);
        ((TextInputLayout) password.getParent()).setErrorEnabled(false);
        ((TextInputLayout) confirmPassword.getParent()).setError(null);
        ((TextInputLayout) confirmPassword.getParent()).setErrorEnabled(false);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getFailedRules().get(0).getMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((TextInputLayout) view.getParent()).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
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
