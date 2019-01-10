package com.nxtvision.tradenivesh.activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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

public class RegisterActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    EditText dob, name, email, password;
    String gender = "", number;
    RadioGroup genderGroup;

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
        setContentView(R.layout.activity_register);

        dob = (EditText) findViewById(R.id.dob);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);

        number = getIntent().getStringExtra("number");

        genderGroup = findViewById(R.id.gender);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    gender = checkedRadioButton.getText().toString();
                }
            }
        });
        genderGroup.check(R.id.male);


        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(RegisterActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                }
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        AppCompatButton mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDOB()) {
                    if (checkPassword()) {
                        if (checkEmail()) {
                            Common.generateOTP(number, RegisterActivity.this, new Common.OTPListener() {
                                @Override
                                public void onValid() {

                                    JsonObjectRequest request2 = null;
                                    try {
                                        request2 = new JsonObjectRequest(
                                                Request.Method.GET,
                                                String.format(
                                                        Common.REGISTER_LINK,
                                                        URLEncoder.encode(name.getText().toString(), "UTF-8"),
                                                        email.getText().toString(),
                                                        number,
                                                        gender,
                                                        URLEncoder.encode(dob.getText().toString(), "UTF-8"),
                                                        URLEncoder.encode(password.getText().toString(), "UTF-8")
                                                ),
                                                null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            if (response.getString("code").equals("200")) {
                                                                Common.loginDetails = new LoginDetails(
                                                                        name.getText().toString(),
                                                                        password.getText().toString(),
                                                                        number,
                                                                        email.getText().toString(),
                                                                        gender,
                                                                        dob.getText().toString()
                                                                );
                                                                Common.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                                Common.sharedPreferences.edit().putString("Username", number).apply();
                                                                Common.sharedPreferences.edit().putString("Password", password.getText().toString()).apply();
                                                                startActivity(new Intent(RegisterActivity.this, Dashboard.class));
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("REGISTERATION ERROR", error.toString());
                                                    }
                                                }) {
                                            @Override
                                            public Map<String, String> getHeaders() {
                                                Map<String, String> headers = new HashMap<String, String>();
                                                headers.put("User-agent", "NxtGARUDA");
                                                return headers;
                                            }
                                        };
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    ApplicationActivity.getInstance().addToRequestQueue(request2);
                                }

                                @Override
                                public void onInvalid(Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            email.setError("Enter valid email address");
                            email.requestFocus();
                        }
                    } else {
                        password.setError("Enter Password Greater than 4 Digits");
                        password.requestFocus();
                    }
                } else {
                    dob.setError("Enter DOB");
                    dob.requestFocus();
                }

            }
        });
    }

    private boolean checkPassword() {
        return (!password.getText().toString().equals("") && password.getText().toString().length() > 4);
    }

    private boolean checkDOB() {
        return !dob.getText().toString().equals("");
    }

    private boolean checkEmail() {
        //TODO: Replace this with your own logic
        return email.getText().toString().contains("@");
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }
}
