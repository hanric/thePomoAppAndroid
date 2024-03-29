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
import android.widget.Toast;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.SettingDTO;
import com.example.android.thepomoappandroid.api.services.SettingsService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 19/04/2015.
 */
public class AddSettingDialog extends DialogFragment
        implements Toolbar.OnMenuItemClickListener,
        View.OnClickListener,
        Validator.ValidationListener {

//    public static final String EXTRA_GROUP_ID = "extra.groupId";

    protected SettingDTO settingDTO;

    protected DBHandler dbHandler;

    protected Toolbar toolbar;

    private Validator validator;

    @NotEmpty(sequence = 1, messageResId = R.string.emptyFieldError)
    @Length(sequence = 2, min = 3, max = 20, messageResId = R.string.sessionNameError)
    protected EditText name;

    protected DiscreteSeekBar work;
    protected DiscreteSeekBar rest;
    protected DiscreteSeekBar largeRest;

    protected OnActionFromSettingDialog onActionFromSettingDialog;

    public interface OnActionFromSettingDialog {
        void onActionFromSessionDialog();
    }

    public static AddSettingDialog newInstance() {
        AddSettingDialog dialogFragment = new AddSettingDialog();
        Bundle bundle = new Bundle();
//        bundle.putInt(EXTRA_GROUP_ID, groupId);
        dialogFragment.setArguments(bundle);
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
        View view = inflater.inflate(R.layout.popup_add_setting, container,
                false);
        findViews(view);
        setListeners();
        getArguments(getArguments());
        setupToolbar(R.string.toolbar_add_setting);
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
        name = (EditText) view.findViewById(R.id.popupAddSetting_name);
        work = (DiscreteSeekBar) view.findViewById(R.id.popupAddSetting_work);
        rest = (DiscreteSeekBar) view.findViewById(R.id.popupAddSetting_rest);
        largeRest = (DiscreteSeekBar) view.findViewById(R.id.popupAddSetting_largeRest);
    }

    protected void setListeners() {
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
    }

    public void getArguments(Bundle bundle) {
        //TODO
    }

    public void setupToolbar(int titleResId) {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle(titleResId);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_save);
    }

    public void setOnActionFromSettingDialog(OnActionFromSettingDialog onActionFromSettingDialog) {
        this.onActionFromSettingDialog = onActionFromSettingDialog;
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
    public void onValidationSucceeded() {
        performSaveAction();
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

    protected void performSaveAction() {
        if (!name.getText().toString().isEmpty()) {
            if (Utils.isLoggedIn(getActivity())) {
                saveWhenLoggedIn();
            } else {
                saveWhenNotAuthenticated();
            }
        }
    }

    private void saveWhenLoggedIn() {
        settingDTO = new SettingDTO(name.getText().toString(), work.getProgress(), rest.getProgress(), largeRest.getProgress(), Utils.getUserId(getActivity()));
        SettingsService.getInstance().create(Utils.getToken(getActivity()), settingDTO, new Callback<SettingDTO>() {
            @Override
            public void success(SettingDTO settingDTO, Response response) {
                dbHandler.createSetting(settingDTO);
                onActionFromSettingDialog.onActionFromSessionDialog();
                dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveWhenNotAuthenticated() {
        dbHandler.createSetting(name.getText().toString(), work.getProgress(), rest.getProgress(), largeRest.getProgress());
        onActionFromSettingDialog.onActionFromSessionDialog();
        dismiss();
    }
}
