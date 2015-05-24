package com.example.android.thepomoappandroid.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.android.thepomoappandroid.receiver.AlarmReceiver;

import java.util.GregorianCalendar;

public class AlarmUtils {

    private static final String CLASS_TAG = AlarmUtils.class.getSimpleName();

    private static void setAlarm(Context context, AlarmManager alarmManager, PendingIntent pendingIntent, long date) {
        int alarmType = AlarmManager.RTC_WAKEUP;
        long aMinute = 60 * 1000;
        long twoHours = 2 * 3600 * 1000; //TODO this is just a workaround for timezone
        long alarmTime = date - aMinute - twoHours;

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(alarmTime);
        alarmManager.set(alarmType, alarmTime, pendingIntent);
    }

    /**
     * Set an alarm
     *
     * @param context
     * @param date      Time to fire the alarm
     * @param idTitle   Resource id of the notification title
     * @param idContent Resource id of the notification content
     */
    public static void initAlarm(Context context, long date, int idTitle, int idContent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(AlarmReceiver.TITLE_ID, idTitle);
        alarmIntent.putExtra(AlarmReceiver.CONTENT_ID, idContent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        setAlarm(context, alarmManager, pendingIntent, date);
    }

}