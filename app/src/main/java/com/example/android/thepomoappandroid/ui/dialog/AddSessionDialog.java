package com.example.android.thepomoappandroid.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.services.SessionsService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.db.Session;
import com.example.android.thepomoappandroid.db.Setting;
import com.example.android.thepomoappandroid.ui.adapter.SettingAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 19/04/2015.
 */
public class AddSessionDialog extends DialogFragment
        implements Toolbar.OnMenuItemClickListener,
        View.OnClickListener {

    public static final String EXTRA_GROUP_ID = "extra.groupId";

    protected SessionDTO sessionDTO;

    protected DBHandler dbHandler;
    protected SettingAdapter settingAdapter;

    protected int groupId;

    protected GregorianCalendar minimumDate;

    protected Toolbar toolbar;
    protected EditText name;
    protected DiscreteSeekBar num;
    protected Button timeButton;
    protected TextView timeText;
    protected Spinner settingsSpinner;

    private GregorianCalendar timePicked;

    protected OnActionFromSessionDialog onActionFromSessionDialog;

    public interface OnActionFromSessionDialog {
        void onActionFromSessionDialog();
    }

    public static AddSessionDialog newInstance(int groupId) {
        AddSessionDialog dialogFragment = new AddSessionDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_GROUP_ID, groupId);
        dialogFragment.setArguments(bundle);
        dialogFragment.setStyle(0, R.style.DialogThemeFullScreen);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = DBHandler.newInstance(getActivity());
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
        initSettingsAdapter();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getMinimumDate();
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
        num = (DiscreteSeekBar) view.findViewById(R.id.popupAddSession_num);
        timeButton = (Button) view.findViewById(R.id.popupAddSession_timeButton);
        timeText = (TextView) view.findViewById(R.id.popupAddSession_timeText);
        settingsSpinner = (Spinner) view.findViewById(R.id.popupAddSession_settingSpinner);
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

    protected void initSettingsAdapter() {
        RealmResults<Setting> results = DBHandler.newInstance(getActivity()).getOnlyOnlineSettings();
        settingAdapter = new SettingAdapter(getActivity(), R.id.listView, results, true);
        settingsSpinner.setAdapter(settingAdapter);
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
            selectStartTime();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.save) {
            performSaveAction();
        }
        return false;
    }

    private void getMinimumDate() {
        List<Session> sessions = dbHandler.getSessionsByGroup(groupId);
        minimumDate = new GregorianCalendar(); // minimumDate is now
        for (Session session : sessions) {
            GregorianCalendar sessionDate = Utils.formatStringDate(session.getEndTime());
            if (sessionDate.after(minimumDate)) {
                minimumDate = sessionDate;
            }
        }
    }

    private void selectStartTime() {
        handleDatePicking();
    }

    private void handleDatePicking() {
        DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                timePicked = new GregorianCalendar(i, i1, i2);
                GregorianCalendar compareTime = new GregorianCalendar(minimumDate.get(Calendar.YEAR), minimumDate.get(Calendar.MONTH), minimumDate.get(Calendar.DAY_OF_MONTH));
                if (timePicked.before(compareTime)) {
                    String toastText = getString(R.string.popup_add_session_err_after_date, minimumDate.get(Calendar.YEAR), minimumDate.get(Calendar.MONTH), minimumDate.get(Calendar.DATE));
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    selectStartTime();
                } else {
                    handleTimePicking();
                }
            }
        }, minimumDate.get(Calendar.YEAR), minimumDate.get(Calendar.MONTH), minimumDate.get(Calendar.DAY_OF_MONTH)).show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void handleTimePicking() {
        TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
                timePicked.set(Calendar.HOUR, i);
                timePicked.set(Calendar.MINUTE, i1);
                if (timePicked.before(minimumDate)) {
                    String toastText = getString(R.string.popup_add_session_err_after_time, minimumDate.get(Calendar.HOUR), minimumDate.get(Calendar.MINUTE));
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                    handleTimePicking();
                } else {
                    timeText.setText(Utils.formatDate(timePicked));
                }
            }
        }, minimumDate.get(Calendar.HOUR), minimumDate.get(Calendar.MINUTE), true).show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    protected void performSaveAction() {
        String nameString = name.getText().toString();
        int nPomos = num.getProgress();
        Setting setting = ((Setting) settingsSpinner.getSelectedItem());
        if (!nameString.isEmpty() && nPomos != 0 && timeText.getText() != getActivity().getResources().getString(R.string.popup_add_session_timeNotSelected)) {
            GregorianCalendar timePickedGmt = (GregorianCalendar) timePicked.clone();
            timePickedGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            String startTime = Utils.formatDate(timePickedGmt);
            String endTime = Utils.getEndTime(timePickedGmt, nPomos, setting.getWorkTime(), setting.getRestTime(), setting.getLargeRestTime());
            sessionDTO = new SessionDTO(nameString, nPomos, startTime, endTime, groupId, setting.getId());
            SessionsService.getInstance().create(Utils.getToken(getActivity()), sessionDTO, new Callback<SessionDTO>() {
                @Override
                public void success(SessionDTO sessionDTO, Response response) {
                    onActionFromSessionDialog.onActionFromSessionDialog();
                    dismiss();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }
}
