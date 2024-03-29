package com.example.android.thepomoappandroid.ui.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.thepomoappandroid.Pomodoro;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.db.Setting;
import com.example.android.thepomoappandroid.ui.adapter.SettingAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.List;

import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

/**
 * Created by Enric on 19/04/2015.
 */
public class AddAloneSessionDialog extends DialogFragment
        implements Toolbar.OnMenuItemClickListener,
        View.OnClickListener,
        Validator.ValidationListener {

    protected DBHandler dbHandler;
    protected SettingAdapter settingAdapter;

    protected Toolbar toolbar;

    private Validator validator;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Length(sequence = 2, min = 3, max = 20, messageResId = R.string.sessionNameError)
    protected EditText name;
    protected DiscreteSeekBar num;
    protected Spinner settingsSpinner;

    public static AddAloneSessionDialog newInstance() {
        AddAloneSessionDialog dialogFragment = new AddAloneSessionDialog();
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initAdapter();
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
        num = (DiscreteSeekBar) view.findViewById(R.id.popupAddAloneSession_num);
        settingsSpinner = (Spinner) view.findViewById(R.id.popupAddAloneSession_settingSpinner);
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

    protected void initAdapter() {
        RealmResults<Setting> results = DBHandler.newInstance(getActivity()).getSettings();
        settingAdapter = new SettingAdapter(getActivity(), R.id.listView, results, true);
        settingsSpinner.setAdapter(settingAdapter);
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
            validator.validate();
        }
        return false;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        ((TextInputLayout) name.getParent()).setError(null);
        ((TextInputLayout) name.getParent()).setErrorEnabled(false);
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

    @Override
    public void onValidationSucceeded() {
        performSaveAction();
    }

    protected void performSaveAction() {
        try {
            String sessionName = name.getText().toString();
            String settingUuid = ((Setting) settingsSpinner.getSelectedItem()).getUuid();
            dbHandler.createAloneSession(sessionName, num.getProgress(), Pomodoro.TO_START, settingUuid);
            dismiss();
        } catch (RealmException e) {
            Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
        }
    }
}
