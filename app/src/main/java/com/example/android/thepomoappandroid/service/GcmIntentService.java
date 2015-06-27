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
import com.example.android.thepomoappandroid.api.dto.GroupDTO;
import com.example.android.thepomoappandroid.api.services.GroupsService;
import com.example.android.thepomoappandroid.receiver.GcmReceiver;
import com.example.android.thepomoappandroid.ui.activity.GroupActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                prepareNotification(extras);
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
    private void prepareNotification(Bundle extras) {

        //Getting the content to be used from the data gcm param
        final ArrayList<CharSequence> content = setNotificationContent(extras);

        //Call to the get the full Group info and send it to the GroupActivity
        int groupId = Integer.parseInt(content.get(2).toString());
        GroupsService.getInstance().findById(Utils.getToken(getApplicationContext()), groupId, new Callback<GroupDTO>() {
            @Override
            public void success(GroupDTO groupDTO, Response response) {
                sendNotification(content, groupDTO);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, error.getMessage());
            }
        });


    }

    private ArrayList<CharSequence> setNotificationContent(Bundle extras) {
        ArrayList<CharSequence> toReturn = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(extras.getString(Constants.GCM_DATA));
            String gcmMessage = extras.getString(Constants.GCM_MESSAGE);
            if (gcmMessage.equals(Constants.GCM_SESSION_NEW)) {
                String groupId = data.getString("groupId");
                String groupName = data.getString("groupName");
                String sessionId = data.getString("sessionId");
                String sessionName = data.getString("sessionName");

                toReturn.add(Utils.formatNotification(getApplicationContext(), R.string.session_new_title, groupName));
                toReturn.add(Utils.formatNotification(getApplicationContext(), R.string.session_new_content, sessionName));
                toReturn.add(groupId);
                return toReturn;

            } else if (gcmMessage.equals(Constants.GCM_SESSION_UPDATED)) {
                String groupId = data.getString("groupId");
                String groupName = data.getString("groupName");
                String sessionId = data.getString("sessionId");
                String sessionName = data.getString("sessionName");

                toReturn.add(Utils.formatNotification(getApplicationContext(), R.string.session_updated_title, groupName));
                toReturn.add(Utils.formatNotification(getApplicationContext(), R.string.session_updated_content, sessionName));
                toReturn.add(groupId);
                return toReturn;

            } else if (gcmMessage.equals(Constants.GCM_GROUP_ADDED)) {
                String groupId = data.getString("groupId");
                String groupName = data.getString("groupName");

                toReturn.add(Utils.formatNotification(getApplicationContext(), R.string.group_added_title, groupName));
                toReturn.add(getApplicationContext().getString(R.string.group_added_content));
                toReturn.add(groupId);
                return toReturn;
            }

        } catch (JSONException e) {
            Log.e(LOGTAG, e.getMessage());
        }
        return toReturn;
    }

    private void sendNotification(List<CharSequence> content, GroupDTO groupDTO) {
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
                        .setContentTitle(content.get(0))
                        .setContentText(content.get(1))
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(Constants.NOTIFICATION_SERVER_SESSION_ID, mBuilder.build());
    }
}
