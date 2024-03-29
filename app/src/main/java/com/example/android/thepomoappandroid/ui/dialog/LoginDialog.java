package com.example.android.thepomoappandroid.ui.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.thepomoappandroid.Constants;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.response.LoginResponse;
import com.example.android.thepomoappandroid.api.services.PeopleService;
import com.example.android.thepomoappandroid.api.services.SettingsService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.db.Setting;
import com.example.android.thepomoappandroid.ui.activity.MainActivity;
import com.example.android.thepomoappandroid.ui.activity.RegisterActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 13/04/2015.
 */
public class LoginDialog extends DialogFragment implements
        View.OnClickListener,
        Validator.ValidationListener {

    public static final String CLASS_TAG = LoginDialog.class.getSimpleName();

    private DBHandler dbHandler;

    private boolean shown = false;

    ProgressDialog progressDialog;

    private String emailText;

    private Validator validator;

    @NotEmpty (sequence = 1, messageResId = R.string.emptyFieldError)
    @Email (sequence = 2, messageResId = R.string.emailError)
    private EditText email;

    @NotEmpty (sequence = 1, messageResId = R.string.emptyFieldError)
    @Password(sequence = 2, messageResId = R.string.passwordError, min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    private EditText password;

    private Button login;
    private Button register;
    private int numberOfOfflineSettings;

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
        findViews(view);
        setListeners();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dbHandler = DBHandler.newInstance(getActivity());
        return view;
    }

    private void findViews(View view) {
        email = (EditText) view.findViewById(R.id.popup_login_email);
        password = (EditText) view.findViewById(R.id.popup_login_password);
        login = (Button) view.findViewById(R.id.popup_login_button);
        register = (Button) view.findViewById(R.id.popup_login_register_button);
    }

    private void setListeners() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == login.getId()) {
            validator.validate();
        } else if (id == register.getId()) {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivityForResult(intent, RegisterActivity.REGISTER_OK);// Activity is started with requestCode 2
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

    @Override
    public void onValidationSucceeded() {
        login();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ((TextInputLayout) email.getParent()).setError(null);
        ((TextInputLayout) email.getParent()).setError(null);
        ((TextInputLayout) password.getParent()).setErrorEnabled(false);
        ((TextInputLayout) password.getParent()).setErrorEnabled(false);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getFailedRules().get(0).getMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((TextInputLayout) view.getParent()).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isShown() {
        return shown;
    }

    /**
     * login -> handleLoginSuccess -> postLocalSettingsWithoutUser -> handleSettingsPosted -> handleGetPrefsSuccess
     */

    private void login() {
        progressDialog = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading));
        String emailField = email.getText().toString();
        emailText = emailField;
        String passwordField = password.getText().toString();
        PeopleService.getInstance().login(emailField, passwordField, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse loginResponse, Response response) {
                handleLoginSuccess(loginResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getActivity(), "Incorrect user or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleLoginSuccess(LoginResponse loginResponse) {
        SharedPreferences prefs = Utils.getPrefs(getActivity());
        prefs.edit().putString(Constants.PREFS_EMAIL, emailText).apply();
        ((MainActivity) getActivity()).onLoginFromDialog(loginResponse);
        if (Utils.checkPlayServices(getActivity())) {
            Utils.updateRegistration(getActivity());
        } else {
            Log.i(CLASS_TAG, "No valid Google Play Services APK found.");
        }
        postLocalSettingsWithoutUser();
    }

    private void postLocalSettingsWithoutUser() {
        // TODO for every setting in the local db with userId = -1, post
        RealmResults<Setting> localSettings = dbHandler.getSettings();
        List<Setting> settingsToPost = new ArrayList<>();
        for (Setting setting : localSettings) {
            if (setting.getId() == -1) {
                settingsToPost.add(setting);
            }
        }
        numberOfOfflineSettings = settingsToPost.size();
        for (Setting setting : settingsToPost) {
            SettingDTO settingDTO = new SettingDTO(setting.getName(), setting.getWorkTime(), setting.getRestTime(), setting.getLargeRestTime(), Utils.getUserId(getActivity()));
            SettingsService.getInstance().create(Utils.getToken(getActivity()), settingDTO, new Callback<SettingDTO>() {
                @Override
                public void success(SettingDTO settingDTO, Response response) {
                    --numberOfOfflineSettings;
                    if (numberOfOfflineSettings == 0) {
                        handleSettingsPosted();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.showError(getActivity());
                    dismiss();
                }
            });
        }
        if (settingsToPost.size() == 0) {
            handleSettingsPosted();
        }
    }

    private void handleSettingsPosted() {
        // delete all the local settings
        dbHandler.deleteSettings();

        // get all the settings from the server
        PeopleService.getInstance().getSettings(getActivity(), new Callback<List<SettingDTO>>() {
            @Override
            public void success(List<SettingDTO> settingDTOs, Response response) {
                handleGetPrefsSuccess(settingDTOs);
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.showError(getActivity());
                dismiss();
            }
        });
    }

    private void handleGetPrefsSuccess(List<SettingDTO> settingDTOs) {

        for (SettingDTO settingDTO : settingDTOs) {
            if (dbHandler.getSetting(settingDTO.getId()) == null) {
                dbHandler.createSetting(settingDTO);
            }
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            //TODO LOGIN CORRECT
        }
    }
}
