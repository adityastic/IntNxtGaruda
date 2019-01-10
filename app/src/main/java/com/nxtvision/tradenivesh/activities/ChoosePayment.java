package com.nxtvision.tradenivesh.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.activities.pay.PayUMoneyWeb;
import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static com.nxtvision.tradenivesh.utils.Common.PAYCOMPLETE_LINK;
import static com.nxtvision.tradenivesh.utils.Common.THEME_BLUE;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_MULBERRY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_RED;
import static com.nxtvision.tradenivesh.utils.Common.THEME_ROSYBROWN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SALMON;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SEAGREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_WINE;
import static com.nxtvision.tradenivesh.utils.Common.getHTMLString;

public class ChoosePayment extends AppCompatActivity implements PaymentResultListener {

    Toolbar mToolbar;

    CardView razorpay, payumoney;
    LinearLayout customLayout;
    EditText customEntry;
//    TextView datepicker;

    double amount;
    String date;
    String item;
    int index;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLayout();
        setContentView(R.layout.activity_choosepayment);

        razorpay = findViewById(R.id.razorPay);
        payumoney = findViewById(R.id.PayUMoney);


        // INITIALIZE PAYMENT OPTION
        razorpay.setVisibility(View.GONE);
        payumoney.setVisibility(View.VISIBLE);

        customLayout = findViewById(R.id.customlayout);
        customEntry = findViewById(R.id.customentrytext);

//        datepicker = findViewById(R.id.datepicker);

        mToolbar = findViewById(R.id.toolbar);
        setUpToolbar();

        item = (getIntent().getStringExtra("item").equals("Custo")) ? "Custom" : getIntent().getStringExtra("item");
        index = getIntent().getIntExtra("index", -1);

        if (!item.equals("Custom")) {
            customLayout.setVisibility(GONE);
            amount = getIntent().getDoubleExtra("price", -1);
        }

        razorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_razorpay();
            }
        });

        payumoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_payumoney();
            }
        });
//
//        datepicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("Inidate", Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");
//                new SpinnerDatePickerDialogBuilder()
//                        .context(ChoosePayment.this)
//                        .callback(new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                datepicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                            }
//                        })
//                        .showTitle(true)
//                        .showDaySpinner(true)
//                        .defaultDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
//                        .minDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
//                        .build()
//                        .show();
//            }
//        });
    }

    private void pay_razorpay() {
        if (!item.equals("Custom")) {
                startPaymentRazorMoney();
        } else {
            if (checkamount()) {
                    startPaymentRazorMoney();
            } else
                Toast.makeText(ChoosePayment.this, "Enter Amount to Proceed", Toast.LENGTH_SHORT).show();
        }
    }

    private void pay_payumoney() {
        if (!item.equals("Custom")) {
            Intent i = new Intent(ChoosePayment.this, PayUMoneyWeb.class);
            i.putExtra("price", amount);
            i.putExtra("date", date);
            i.putExtra("item", item);
            i.putExtra("name", getIntent().getStringExtra("name"));
            startActivity(i);

            finish();
        } else {
            if (checkamount()) {
                Intent i = new Intent(ChoosePayment.this, PayUMoneyWeb.class);
                i.putExtra("price", amount);
                i.putExtra("date", date);
                i.putExtra("item", item);
                i.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(i);

                finish();
            } else
                Toast.makeText(ChoosePayment.this, "Enter Amount to Proceed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkamount() {
        if (customEntry.getText().toString().equals("")) {
            return false;
        } else {
            amount = Double.parseDouble(customEntry.getText().toString());
            return true;
        }
    }

    private void setUpToolbar() {
        setTitle("Select Payment Sites");
        setSupportActionBar(mToolbar);
    }

    /*
        STARTING OF RAZORMONEY CODE
     */

    public void startPaymentRazorMoney() {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "NxtVision");
            options.put("description", "Garuda");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", String.valueOf(amount * 100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", Common.loginDetails.email);
            preFill.put("contact", Common.loginDetails.phone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(final String s) {
        Log.e("PAyMent", "Payment Successful: " + s);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getHTMLString(String.format(
                        PAYCOMPLETE_LINK,
                        Common.loginDetails.phone,
                        String.valueOf(amount),
                        s,
                        getIntent().getStringExtra("name"),
                        item,
                        "RAZORPAY"
                ))
                ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ChoosePayment.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChoosePayment.this, "Payment Successful but not registered ", Toast.LENGTH_SHORT).show();
                        finish();
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

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("PAyMent", "Payment failed: " + i + " " + s);
        Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
        finish();
    }

    /*
        ENDING OF RAZORMONEY CODES
     */
}
