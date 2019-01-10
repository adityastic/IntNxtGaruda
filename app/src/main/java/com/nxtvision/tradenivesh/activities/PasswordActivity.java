package com.nxtvision.tradenivesh.activities;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.data.LoginDetails;
import com.nxtvision.tradenivesh.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.nxtvision.tradenivesh.utils.Common.THEME_BLUE;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_MULBERRY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_RED;
import static com.nxtvision.tradenivesh.utils.Common.THEME_ROSYBROWN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SALMON;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SEAGREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_WINE;

public class PasswordActivity extends AppCompatActivity {

    private EditText mPasswordView;
    static String number;

    boolean clicked = false;

    public void changeLayout() {
        switch (Common.selectedTheme) {
            case THEME_RED:
                setTheme(R.style.RedTheme);
                break;
            case THEME_BLUE:
                setTheme(R.style.BlueTheme);
                break;
            case THEME_GREEN:
                setTheme(R.style.GreenTheme);
                break;
            case THEME_GREY:
                setTheme(R.style.GreyTheme);
                break;
            case THEME_SALMON:
                setTheme(R.style.SalmonTheme);
                break;
            case THEME_SEAGREEN:
                setTheme(R.style.SeaGreenTheme);
                break;
            case THEME_ROSYBROWN:
                setTheme(R.style.RosyBrownTheme);
                break;
            case THEME_MULBERRY:
                setTheme(R.style.MulberryTheme);
                break;
            case THEME_WINE:
                setTheme(R.style.WineTheme);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLayout();
        setContentView(R.layout.activity_password);

        number = getIntent().getStringExtra("number");

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        AppCompatButton mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (Common.isNetworkAvailable(this)) {
                if (!clicked) {
                    clicked = true;
                    Log.e("LOGINLINK", String.format(Common.LOGIN_LINK, number, mPasswordView.getText()));
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.GET,
                            String.format(Common.LOGIN_LINK, number, mPasswordView.getText()),
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

                                            Common.sharedPreferences.edit().putString("Username", number).apply();
                                            Common.sharedPreferences.edit().putString("Password", mPasswordView.getText().toString()).apply();

                                            Common.sharedPreferences.edit().putString("subs", response.getString("services")).apply();

                                            JSONArray json = new JSONArray(Common.sharedPreferences.getString("subs", "[]"));
                                            for (int i = 0; i < json.length(); i++) {
                                                String topic = (String) json.get(i);
                                                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                                            }

                                            Intent i = new Intent(PasswordActivity.this, Dashboard.class);
                                            startActivity(i);
                                        } else {
                                            clicked = false;
                                            Toast.makeText(PasswordActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VolleyError", error.toString());
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders(){
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("User-agent", "NxtGARUDA");
                            return headers;
                        }
                    };
                    ApplicationActivity.getInstance().addToRequestQueue(request);

                }
            } else {
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
