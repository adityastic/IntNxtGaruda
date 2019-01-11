package com.nxtvision.tradenivesh.activities;

import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.data.LoginDetails;
import com.nxtvision.tradenivesh.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.nxtvision.tradenivesh.utils.Common.LAYOUT_GRID;
import static com.nxtvision.tradenivesh.utils.Common.LAYOUT_LINEAR;
import static com.nxtvision.tradenivesh.utils.Common.THEME_BLUE;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_LINK;
import static com.nxtvision.tradenivesh.utils.Common.THEME_MULBERRY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_RED;
import static com.nxtvision.tradenivesh.utils.Common.THEME_ROSYBROWN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SALMON;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SEAGREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_WINE;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        linear = findViewById(R.id.no_internet);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                startActivity(new Intent(SplashScreen.this, SplashScreen.class));
            }
        });

        if (!Common.isNetworkAvailable(this)) {
            linear.setVisibility(View.VISIBLE);
            return;
        } else {
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
                Log.e("VolleyErrorTheme", error.toString());
                callNext();
            }
        });

        ApplicationActivity.getInstance().getRequestQueue().getCache().clear();
        ApplicationActivity.getInstance().addToRequestQueue(request);

        Common.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    private void callNext() {
        if (Common.isNetworkAvailable(SplashScreen.this)) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map map = (Map) dataSnapshot.getValue();
                    if ((Boolean) map.get("license")) {
                        if (!Common.sharedPreferences.getString("Username", "NULL").equals("NULL")) {
                            Log.e("LoginLink", String.format(
                                    Common.LOGIN_LINK,
                                    Common.sharedPreferences.getString("Username", ""),
                                    Common.sharedPreferences.getString("Password", "")
                            ));

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
                                                            if (getIntent().hasExtra("type")) {
                                                                if (getIntent().getStringExtra("type").equals("notif"))
                                                                    Log.e("LogOfNoti", getIntent().getExtras().toString());
//                                                        i.putExtra("type", Common.saveNotification(SplashScreen.this,));
                                                                else
                                                                    i.putExtra("type", getIntent().getExtras().getString("type"));
                                                            }
                                                            startActivity(i);
                                                        }
                                                    }, 400);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("VolleyError", error.getMessage());
                                    Common.sharedPreferences.edit().remove("Username").apply();
                                    Common.sharedPreferences.edit().remove("Password").apply();
                                    finish();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> headers = new HashMap<String, String>();
                                    headers.put("User-agent", "NxtGARUDA");
                                    return headers;
                                }
                            };
                            ApplicationActivity.getInstance().addToRequestQueue(request2);

                        } else {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                }
                            }, 400);
                        }
                    } else
                        Toast.makeText(SplashScreen.this, "Server Error 888", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("DatabaseReference", "Error: " + databaseError.toString());
                }
            });
        } else {
            Toast.makeText(SplashScreen.this, "NO INTERNET CONNECTION...\nRestart App When Connected To The Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
