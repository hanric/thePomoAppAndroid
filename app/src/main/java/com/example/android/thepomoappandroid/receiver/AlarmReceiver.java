package com.example.android.thepomoappandroid.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.NotificationCompat;

import com.example.android.thepomoappandroid.App;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.SessionStartBusEvent;

import java.util.Collections;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CLASS_TAG = AlarmReceiver.class.getSimpleName();

    public static final String TITLE_ID = "id.title";
    public static final String CONTENT_ID = "id.content";

    private String title = "TODOTitle";
    private String content = "TODOContent";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent.getExtras()) {
            int idTitle = intent.getExtras().getInt(TITLE_ID, -1);
            if (idTitle == -1) {
                int idContent = intent.getExtras().getInt(CONTENT_ID, -1); // workaround, not a well named variable
                triggerBus(context, idContent);
            } else {
                if (idTitle >= 0) {
                    title = context.getResources().getString(idTitle);
                }
                int idContent = intent.getExtras().getInt(CONTENT_ID, -1);
                if (idContent >= 0) {
                    content = context.getResources().getString(idContent);
                }
                sendNotification(context);
            }
        }
    }

    private void sendNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        Intent intent = getMainIntent(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, builder.build());
    }

    private void triggerBus(Context context, int groupId) {
        App.bus.post(new SessionStartBusEvent(groupId));
    }

    /**
     * Get the launcher activity of the context. It will be different for smartphone and tablet, so it is in the commons module
     *
     * @param context
     * @return
     */
    private Intent getMainIntent(Context context) {
        Intent intent = null;
        final PackageManager pm = context.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo temp : appList) {
            if (context.getPackageName().equals(temp.activityInfo.packageName)) {
                intent = pm.getLaunchIntentForPackage(temp.activityInfo.packageName);
                break;
            }
        }

        return intent;
    }
}