package com.example.android.thepomoappandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.alarm.AlarmUtils;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.db.Session;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Enric on 09/05/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String CLASS_TAG = BootReceiver.class.getSimpleName();

    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON";

    public BootReceiver() {
    }

    private void setAlarm(Context context) {
        List<Session> sessions =  DBHandler.newInstance(context).getSessions();
        for (Session session : sessions) {
            GregorianCalendar startDate = Utils.getInstance().formatStringDate(session.getStartTime());
            AlarmUtils.initAlarm(context, startDate.getTimeInMillis(), R.string.notification_start_title, R.string.notification_start_content);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BOOT_COMPLETED) || action.equals(QUICKBOOT_POWERON)) {
            setAlarm(context);
        }
    }
}