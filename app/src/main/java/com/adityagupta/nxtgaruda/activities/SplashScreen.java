package com.adityagupta.nxtgaruda.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.application.ApplicationActivity;
import com.adityagupta.nxtgaruda.data.LoginDetails;
import com.adityagupta.nxtgaruda.utils.Common;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import static com.adityagupta.nxtgaruda.utils.Common.CHANNEL_1_ID;
import static com.adityagupta.nxtgaruda.utils.Common.CHANNEL_2_ID;
import static com.adityagupta.nxtgaruda.utils.Common.LAYOUT_GRID;
import static com.adityagupta.nxtgaruda.utils.Common.LAYOUT_LINEAR;
import static com.adityagupta.nxtgaruda.utils.Common.LOGIN_LINK;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_BLUE;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_GREEN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_GREY;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_LINK;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_MULBERRY;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_RED;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_ROSYBROWN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_SALMON;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_SEAGREEN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_WINE;

public class SplashScreen extends AppCompatActivity {

    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Common.IP = getResources().getString(R.string.ip_address);

        linear = findViewById(R.id.no_internet);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                startActivity(new Intent(SplashScreen.this,SplashScreen.class));
            }
        });

        if(!Common.isNetworkAvailable(this))
        {
            linear.setVisibility(View.VISIBLE);
            return;
        }else {
            linear.setVisibility(View.GONE);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, THEME_LINK, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    switch (response.getString("color")) {
                        case "red":
                            Common.selectedTheme = THEME_RED;
                            break;
                        case "blue":
                            Common.selectedTheme = THEME_BLUE;
                            break;
                        case "green":
                            Common.selectedTheme = THEME_GREEN;
                            break;
                        case "grey":
                            Common.selectedTheme = THEME_GREY;
                            break;
                        case "salmon":
                            Common.selectedTheme = THEME_SALMON;
                            break;
                        case "seagreen":
                            Common.selectedTheme = THEME_SEAGREEN;
                            break;
                        case "rosybrown":
                            Common.selectedTheme = THEME_ROSYBROWN;
                            break;
                        case "mulberry":
                            Common.selectedTheme = THEME_MULBERRY;
                            break;
                        case "wine":
                            Common.selectedTheme = THEME_WINE;
                            break;
                        default:
                            Common.selectedLayout = THEME_BLUE;
                            break;
                    }
                    switch (response.getString("layout")) {
                        case "grid":
                            Common.selectedLayout = LAYOUT_GRID;
                            break;
                        case "linear":
                            Common.selectedLayout = LAYOUT_LINEAR;
                            break;
                        default:
                            Common.selectedLayout = LAYOUT_LINEAR;
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callNext();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                callNext();
            }
        });

        ApplicationActivity.getInstance().addToRequestQueue(request);

        Common.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    private void callNext() {
        if (!Common.sharedPreferences.getString("Username", "NULL").equals("NULL")) {
            if (Common.isNetworkAvailable(this)) {

                JsonObjectRequest request2 = new JsonObjectRequest(
                        Request.Method.GET,
                        String.format(
                                Common.LOGIN_LINK,
                                Common.sharedPreferences.getString("Username", ""),
                                Common.sharedPreferences.getString("Password", "")
                        ),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.has("name")) {
                                        Common.loginDetails = new LoginDetails(
                                                response.getString("name"),
                                                response.getString("password"),
                                                response.getString("phone"),
                                                response.getString("id"),
                                                response.getString("gender"),
                                                response.getString("dob")
                                        );

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent i = new Intent(SplashScreen.this, Dashboard.class);
                                                if(getIntent().hasExtra("type"))
                                                    i.putExtra("type",getIntent().getExtras().getString("type"));
                                                startActivity(i);
                                            }
                                        },400);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                ApplicationActivity.getInstance().addToRequestQueue(request2);

            } else {
                Toast.makeText(this, "NO INTERNET CONNECTION...\nRestart App When Connected To The Internet", Toast.LENGTH_SHORT).show();
            }
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }
            },400);
        }
    }
}
