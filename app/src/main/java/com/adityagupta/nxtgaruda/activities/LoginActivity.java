package com.adityagupta.nxtgaruda.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.application.ApplicationActivity;
import com.adityagupta.nxtgaruda.data.LoginDetails;
import com.adityagupta.nxtgaruda.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.adityagupta.nxtgaruda.utils.Common.LAYOUT_GRID;
import static com.adityagupta.nxtgaruda.utils.Common.LAYOUT_LINEAR;
import static com.adityagupta.nxtgaruda.utils.Common.LOGIN_LINK;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_BLUE;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_GREEN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_GREY;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_MULBERRY;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_RED;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_ROSYBROWN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_SALMON;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_SEAGREEN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_WINE;

public class LoginActivity extends AppCompatActivity {

    boolean back = false;

    boolean clicked = false;
    // UI references.
    private EditText mMobileNumber;


    @Override
    public void onBackPressed() {
        if (back)
            this.finishAffinity();
        else {
            back = true;
            Toast.makeText(this, "Press Back Again to Close the App", Toast.LENGTH_SHORT).show();
        }
    }

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
        changeLayout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mMobileNumber = findViewById(R.id.number);

        mMobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

    @SuppressLint("HandlerLeak")
    private void attemptLogin() {

        // Reset errors.
        mMobileNumber.setError(null);

        // Store values at the time of the login attempt.
        String email = mMobileNumber.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mMobileNumber.setError(getString(R.string.error_field_required));
            focusView = mMobileNumber;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mMobileNumber.setError(getString(R.string.error_invalid_email));
            focusView = mMobileNumber;
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
                    Log.e("VERIFYLINK",String.format(Common.VERIFY_LINK, mMobileNumber.getText()));

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.GET,
                            String.format(Common.VERIFY_LINK, mMobileNumber.getText()),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("code").equals("200")) {
                                            Intent i = new Intent(LoginActivity.this, PasswordActivity.class);
                                            i.putExtra("number",mMobileNumber.getText().toString());
                                            startActivity(i);
                                            clicked = false;
                                        } else {
                                            clicked = false;
                                            Toast.makeText(LoginActivity.this, "No User Found, Lets Register", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                                            i.putExtra("number",mMobileNumber.getText().toString());
                                            startActivity(i);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VERIFYError",error.toString());
                        }
                    });
                    ApplicationActivity.getInstance().

                            addToRequestQueue(request);

                }
            } else {
                Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (TextUtils.isDigitsOnly(email) && email.length() == 10);
    }
}

