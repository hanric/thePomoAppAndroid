package com.example.android.thepomoappandroid.ui.dialog;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.services.BaseService;
import com.example.android.thepomoappandroid.api.services.SessionsService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.RetrofitError;

/**
 * Created by Enric on 19/04/2015.
 */
public class EditSessionDialog extends AddSessionDialog
        implements Toolbar.OnMenuItemClickListener,
        View.OnClickListener,
        BaseService.OnRetrofitError,
        SessionsService.OnCreateSession {

    private GregorianCalendar timePicked;

    public static EditSessionDialog newInstance(int groupId) {
        EditSessionDialog dialogFragment = new EditSessionDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_GROUP_ID, groupId);
        dialogFragment.setArguments(bundle);
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_add_session, container,
                false);
        findViews(view);
        setListeners();
        getArguments(getArguments());
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
        name = (EditText) view.findViewById(R.id.popupAddSession_name);
        num = (EditText) view.findViewById(R.id.popupAddSession_num);
        timeButton = (Button) view.findViewById(R.id.popupAddSession_timeButton);
        timeText = (TextView) view.findViewById(R.id.popupAddSession_timeText);
    }

    protected void setListeners() {
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);
        timeButton.setOnClickListener(this);
    }

    public void getArguments(Bundle bundle) {
        groupId = bundle.getInt(EXTRA_GROUP_ID);
    }

    public void setupToolbar(int titleResId) {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle(titleResId);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_save);
    }

    public void setOnActionFromSessionDialog(OnActionFromSessionDialog onActionFromSessionDialog) {
        this.onActionFromSessionDialog = onActionFromSessionDialog;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == toolbar.getChildAt(0).getId()) {
            dismiss();
        } else if (id == timeButton.getId()) {
            handleTimePicking();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.save) {
            performSaveAction();
        }
        return false;
    }

    private void handleTimePicking() {
        final Calendar now = Calendar.getInstance();
        DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                timePicked = new GregorianCalendar(i, i1, i2);
                TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
                        timePicked.add(Calendar.HOUR, i);
                        timePicked.add(Calendar.HOUR, i1);
                        timeText.setText(Utils.getInstance().formatDate(timePicked));
                    }
                }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false).show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    protected void performSaveAction() {
        Utils utils = Utils.getInstance();
        String nameString = name.getText().toString();
        int nPomos = Integer.parseInt(num.getText().toString());
        String startTime = timeText.getText().toString();
        String endTime = utils.getEndTime(timePicked, nPomos, 25, 5, 15);
//        SessionsService.getInstance().create(utils.getToken(getActivity()), nameString, nPomos, startTime, endTime, groupId, null, this);
    }

    @Override
    public void onCreateSession() {
        onActionFromSessionDialog.onActionFromSessionDialog();
    }

    @Override
    public void onError(RetrofitError error) {

    }
}
