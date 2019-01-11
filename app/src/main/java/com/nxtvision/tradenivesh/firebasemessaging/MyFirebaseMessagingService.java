package com.nxtvision.tradenivesh.firebasemessaging;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import android.preference.PreferenceManager;
import android.util.Log;

import com.nxtvision.tradenivesh.R;

import com.nxtvision.tradenivesh.activities.SplashScreen;

import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.nxtvision.tradenivesh.utils.Common.CHANNEL_ID;
import static com.nxtvision.tradenivesh.utils.Common.REGISTER_DEVID;
import static com.nxtvision.tradenivesh.utils.Common.getHTMLString;
import static com.nxtvision.tradenivesh.utils.Common.notificationManager;

/**
 * Created by aditya.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String s) {

        if (Common.loginDetails != null) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    getHTMLString(String.format(
                            REGISTER_DEVID,
                            Common.loginDetails.phone,
                            s
                    ))
                    ,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Registration Device ID", "Success: " + response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Registration Device ID", "No: " + error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("User-agent", "NxtGARUDA");
                    return headers;
                }
            };
            ApplicationActivity.getInstance().addToRequestQueue(request);
        }
        Log.e(TAG, "New Token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "FROM:" + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data: " + remoteMessage.getData());

            String type = "";
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData().get("message"));
                type = Common.saveNotification(getApplicationContext(), obj.getString("body"));
                Log.e("type", type);
                sendNotification(obj.getString("body"), obj.getString("title"), type);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }

        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());


        }
    }

    private void sendNotification(String body, String title, String type) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int notificationNumber = prefs.getInt("notificationNumber", 0);

        PendingIntent pendingIntent;

        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("type", type);

        pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationNumber, notification);
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt("notificationNumber", notificationNumber);
        editor.apply();
    }
}
