package com.example.android.thepomoappandroid.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.thepomoappandroid.Constants;
import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.Utils;
import com.example.android.thepomoappandroid.alarm.AlarmUtils;
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.api.services.SessionsService;
import com.example.android.thepomoappandroid.db.DBHandler;
import com.example.android.thepomoappandroid.receiver.GcmReceiver;
import com.example.android.thepomoappandroid.ui.activity.GroupActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Enric on 18/05/2015.
 */
public class GcmIntentService extends IntentService {

    public static final String LOGTAG = GcmIntentService.class.getSimpleName();

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    private String gcmMessage;
    private String groupId;
    private String groupName;
    private String sessionId;
    private String sessionName;

    private CharSequence notificationTitle;
    private CharSequence notificationText;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCM service";

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(LOGTAG, "onHandleIntent");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.w(LOGTAG, "error");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.w(LOGTAG, "deleted");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Do work here
                // Post notification of received message.
                handle(extras);
                Log.i(TAG, "Received: " + extras.toString());
            } else {
                Log.i(TAG, "Else: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void handle(Bundle extras) {
        parseContent(extras);
        formatNotification();
        doAction();
    }

    private void parseContent(Bundle extras) {
        try {
            JSONObject data = new JSONObject(extras.getString(Constants.GCM_DATA));
            gcmMessage = extras.getString(Constants.GCM_MESSAGE);
            if (data.has("groupId")) {
                groupId = data.getString("groupId");
            }
            if (data.has("groupName")) {
                groupName = data.getString("groupName");
            }
            if (data.has("sessionId")) {
                sessionId = data.getString("sessionId");
            }
            if (data.has("sessionName")) {
                sessionName = data.getString("sessionName");
            }
        } catch (JSONException e) {
            Log.e(LOGTAG, e.getMessage());
        }
    }

    private void formatNotification() {
        switch (gcmMessage) {
            case Constants.GCM_SESSION_NEW:
                notificationTitle = Utils.formatNotification(getApplicationContext(), R.string.session_new_title, groupName);
                notificationText = Utils.formatNotification(getApplicationContext(), R.string.session_new_content, sessionName);
                break;
            case Constants.GCM_SESSION_UPDATED:
                notificationTitle = Utils.formatNotification(getApplicationContext(), R.string.session_updated_title, groupName);
                notificationText = Utils.formatNotification(getApplicationContext(), R.string.session_updated_content, sessionName);
                break;
            case Constants.GCM_GROUP_ADDED:
                notificationTitle = Utils.formatNotification(getApplicationContext(), R.string.group_added_title, groupName);
                notificationText = getApplicationContext().getString(R.string.group_added_content);
                break;
        }
    }

    private void doAction() {
        if (gcmMessage.equals(Constants.GCM_SESSION_NEW) || gcmMessage.equals(Constants.GCM_SESSION_UPDATED) || gcmMessage.equals(Constants.GCM_GROUP_ADDED)) {
            notifyAndUpdateLocalDB();
        }
    }

    private void notifyAndUpdateLocalDB() {
        //Call to the get the full Group info and send it to the GroupActivity
        int intGroupId = Integer.parseInt(groupId);
        GroupsService.getInstance().findById(Utils.getToken(getApplicationContext()), intGroupId, new Callback<GroupDTO>() {
            @Override
            public void success(GroupDTO groupDTO, Response response) {
                sendNotification(groupDTO);
                makeLocalChanges();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, error.getMessage());
            }
        });
    }

    private void sendNotification(GroupDTO groupDTO) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, GroupActivity.class);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonGroup = gson.toJson(groupDTO);
        intent.putExtra(GroupActivity.EXTRA_GROUP, jsonGroup);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(Constants.NOTIFICATION_SERVER_SESSION_ID, mBuilder.build());
    }

    private void makeLocalChanges() {
        switch (gcmMessage) {
            case Constants.GCM_SESSION_NEW:
                int intSessionId = Integer.parseInt(sessionId);
                SessionsService.getInstance().findById(Utils.getToken(getApplicationContext()), intSessionId, new Callback<SessionDTO>() {
                    @Override
                    public void success(SessionDTO sessionDTO, Response response) {
                        refreshAlarm(sessionDTO);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                break;
            case Constants.GCM_SESSION_UPDATED:
                // TODO
                break;
        }
    }

    private void refreshAlarm(SessionDTO sessionDTO) {
        DBHandler dbHandler = DBHandler.newInstance(getApplicationContext());
        if (dbHandler.getSession(sessionDTO.getId()) == null) {
            dbHandler.createSession(sessionDTO);
            GregorianCalendar startDate = Utils.formatStringDate(sessionDTO.getStartTime());
            AlarmUtils.initAlarm(getApplicationContext(), startDate, R.string.notification_start_title, R.string.notification_start_content, true);
        }
    }
}
