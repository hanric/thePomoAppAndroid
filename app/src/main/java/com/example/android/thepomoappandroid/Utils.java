package com.example.android.thepomoappandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Toast;

import com.example.android.thepomoappandroid.api.dto.SessionDTO;
import com.example.android.thepomoappandroid.db.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Enric on 22/04/2015.
 */
public class Utils {
    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public static void showError(Context context) {
        Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE);
    }

    public static void clearPreferences(Context context) {
        getPrefs(context).edit().clear().commit();
    }

    public static int getUserId(Context context) {
        return getPrefs(context).getInt(Constants.PREFS_USERID, 0);
    }

    public static void setUserId(Context context, int userId) {
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().putInt(Constants.PREFS_USERID, userId).apply();
    }

    public static String getEmail(Context context) {
        return getPrefs(context).getString(Constants.PREFS_EMAIL, "");
    }

    public static String getToken(Context context) {
        return getPrefs(context).getString(Constants.PREFS_TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().putString(Constants.PREFS_TOKEN, token).apply();
    }

    public static int getInstallationId(Context context) {
        return getPrefs(context).getInt(Constants.PREFS_INSTALLATION_ID, -1);
    }

    public static void setInstallationId(Context context, int id) {
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().putInt(Constants.PREFS_INSTALLATION_ID, id).apply();
    }

    public static boolean isLoggedIn(Context context) {
        String token = getToken(context);
        return (token != null && !token.isEmpty());
    }

    public static String getEndTime(GregorianCalendar startTime, int nPomos, int workMinutes, int restMinutes, int largeRestMinutes) {
        int totalRestPhases = nPomos - 1;
        int largeRestPhases = nPomos / 5;
        int restPhases = totalRestPhases - largeRestPhases;

        int minutes = (largeRestPhases * largeRestMinutes) + (restPhases * restMinutes) + (nPomos * workMinutes);

        GregorianCalendar endTime = (GregorianCalendar) startTime.clone();
        endTime.add(Calendar.MINUTE, minutes);

        return formatDate(endTime);
    }

    public static final String DATE_PATTERN_TO_SERVER = "MMM d, yyyy HH:mm:ss";
    public static final String DATE_PATTERN_FROM_SERVER = "yyyy-M-d'T'HH:mm:ss.SSSZ";


    public static String formatDate(GregorianCalendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_PATTERN_TO_SERVER);
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
    }

    public static GregorianCalendar formatStringDate(String date) {
        DateTimeFormatter formatter =
                DateTimeFormat.forPattern(DATE_PATTERN_FROM_SERVER).withOffsetParsed();
        DateTime dateTime = formatter.parseDateTime(date);
        return dateTime.toGregorianCalendar();
    }

    public static boolean isSessionOld(SessionDTO sessionDTO) {
        GregorianCalendar now = new GregorianCalendar();
        return (Utils.formatStringDate(sessionDTO.getEndTime()).before(now));
    }

    public static boolean isSessionOld(Session session) {
        GregorianCalendar now = new GregorianCalendar();
        return (Utils.formatStringDate(session.getEndTime()).before(now));
    }

    public static CharSequence formatNotification(Context context, int resId, String dynamic) {
        String lineFormat = context.getString(resId);
        int lineParamStartPos = lineFormat.indexOf("%1$s");
        if (lineParamStartPos < 0) {
            throw new InvalidParameterException("Something's wrong with your string! LINT could have caught that.");
        }
        String lineFormatted = context.getString(resId, dynamic);

        Spannable sb = new SpannableString(lineFormatted);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), lineParamStartPos, lineParamStartPos + dynamic.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * Updates the registration for push notifications.
     */
    public static void updateRegistration(Context context) {

        // 1. Grab the shared RestAdapter instance.
        final App app = (App) ((Activity) context).getApplication();
        final RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Create LocalInstallation instance
        final LocalInstallation installation = new LocalInstallation(context, adapter);

        // 3. Update Installation properties that were not pre-filled

        // Enter the id of the LoopBack Application
        installation.setAppId(Constants.LOOPBACK_APP_ID);

        // Substitute a real id of the user logged in this application
        String userId = String.valueOf(getUserId(context));
        installation.setUserId(userId);

        // 4. Check if we have a valid GCM registration id
        if (installation.getDeviceToken() != null) {
            // 5a. We have a valid GCM token, all we need to do now
            //     is to save the installation to the server
            saveInstallation(context, installation);
        } else {
            // 5b. We don't have a valid GCM token. Get one from GCM
            // and save the installation afterwards.
            registerInBackground(installation, context);
        }
    }

    /**
     * Checks the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Context context) {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("checkPS", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID in the provided LocalInstallation
     */
    private static void registerInBackground(final LocalInstallation installation, final Context context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                try {
                    GoogleCloudMessaging gcm;

                    gcm = GoogleCloudMessaging.getInstance(context);
                    final String regid = gcm.register(Constants.SENDER_ID);
                    installation.setDeviceToken(regid);
                    return "Device registered, registration ID=" + regid;
                } catch (final IOException ex) {
                    Log.e("registerInBackground", "GCM registration failed.", ex);
                    return "Cannot register with GCM:" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
            }

            @Override
            protected void onPostExecute(final String msg) {
                saveInstallation(context, installation);
            }
        }.execute(null, null, null);
    }

    /**
     * Saves the Installation to the LoopBack server and reports the result.
     *
     * @param installation
     */
    private static void saveInstallation(final Context context, final LocalInstallation installation) {
        installation.save(new VoidCallback() {

            @Override
            public void onSuccess() {
                final Object id = installation.getId();
                setInstallationId(context, (int) id);
                final String msg = "Installation saved with id " + id;
                Log.i("saveInstallation", msg);
            }

            @Override
            public void onError(final Throwable t) {
                Log.e("saveInstallation", "Cannot save Installation", t);
            }
        });
    }
}
