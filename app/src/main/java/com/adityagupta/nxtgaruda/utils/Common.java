package com.adityagupta.nxtgaruda.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.activities.Dashboard;
import com.adityagupta.nxtgaruda.activities.LoginActivity;
import com.adityagupta.nxtgaruda.application.ApplicationActivity;
import com.adityagupta.nxtgaruda.data.LoginDetails;
import com.adityagupta.nxtgaruda.data.NewsInfo;
import com.adityagupta.nxtgaruda.data.Question;
import com.adityagupta.nxtgaruda.data.RecomInfo;
import com.adityagupta.nxtgaruda.data.ServicesInfo;
import com.adityagupta.nxtgaruda.views.OTPEditText;
import com.adityagupta.nxtgaruda.views.SheetDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Common {
    public static final String IP = "http://nxtgaruda.com/";

    public static final String THEME_LINK = IP + "Theme.json";
    public static final String LOGIN_LINK = IP + "login.php?uname=%s&pass=%s";
    public static final String VERIFY_LINK = IP + "verify.php?uname=%s";
    public static final String REGISTER_LINK = IP + "registration.php?name=%s&email=%s&number=%s&gender=%s&dob=%s&pass=%s";
    public static final String PAYCOMPLETE_LINK = IP + "payComplete.php?number=%s&name=%s&amount=%s&payid=%s&sname=%s&sterm=%s";
    public static final String KYC_LINK = "http://192.168.0.66/nxtvision/" + "kyc.php";
    public static final String TRACKSHEET_NAMES_LINK = IP + "filenames.php";
    public static final String TRACKSHEET_LINK = IP + "upload/";

    public static final String SERVICES_LINK = IP + "APIServices.php";
    public static final String NEWS_LINK = IP + "news.json";
    public static final String RECOM_LINK = IP + "recom.json";
    public static final String MORNINGBELL_LINK = IP + "morning.json";
    public static final String ABOUTUS_LINK = IP + "aboutus.json";
    public static final String RPMQUESTIONS_LINK = IP + "APIRpm.php?";

    public static final String SMS_LINK = "http://sms.suninfolabs.com/api/sendhttp.php?authkey=142371Aix4e8TS5ab89464&mobiles=%s&message=%s&sender=NXTVIS&route=4&country=0";

    public static final String PAYU_MERCHANTID = "4901198";
    public static final String PAYU_MERCHANTKEY = "2uICKv";
    public static final String PAYU_MERCHANTSALT = "BVShe7B4";

    public static final String CHANNEL_1_ID = "morningChannel";
    public static final String CHANNEL_2_ID = "recommendChannel";

    public static final int THEME_RED = 1;
    public static final int THEME_BLUE = 2;
    public static final int THEME_GREEN = 3;
    public static final int THEME_GREY = 4;
    public static final int THEME_SALMON = 5;
    public static final int THEME_SEAGREEN = 6;
    public static final int THEME_MULBERRY = 7;
    public static final int THEME_ROSYBROWN = 8;
    public static final int THEME_WINE = 9;

    public static final int LAYOUT_LINEAR = 11;
    public static final int LAYOUT_GRID = 12;

    public static int selectedTheme = THEME_BLUE;
    public static int selectedLayout = LAYOUT_LINEAR;

    public static ArrayList<ServicesInfo> servicesList;
    public static ArrayList<String> tracksheetList;
    public static LoginDetails loginDetails;
    public static SharedPreferences sharedPreferences;
    public static ArrayList<NewsInfo> newsList;
    public static ArrayList<RecomInfo> recomList;
    public static ArrayList<RecomInfo> morningBellList;

    public static NotificationManagerCompat notificationManager;
    public static ArrayList<Question> questionsList;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getHTMLString(String s) {
        return s.replace(" ", "%20");
    }

    static String OTP(int len) {

        // Using numeric values
        String numbers = "0123456789";

        String s = "";
        for (int i = 0; i < len; i++) {
            // use of charAt() method : to get character value
            // use of nextInt() as it is scanning the value as int
            s += numbers.charAt((int) (Math.random() * numbers.length()));
        }
        return s;
    }

    public static String OTP;
    public static AlertDialog alert;

    public static void generateOTP(final String number, final Context context, final OTPListener otpListener) {
        OTP = OTP(4);
        Log.e("OTP", OTP);
        if (Common.isNetworkAvailable(context)) {

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    getHTMLString(
                            String.format(
                                    SMS_LINK,
                                    number,
                                    "Your Login OTP For Garuda is " + OTP
                            )
                    ),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("LinkO", response);

                            AlertDialog.Builder sheetDialog = new AlertDialog.Builder(context);
                            View sheetView =
                                    View.inflate(context, R.layout.content_dialog_bottom_sheet, null);

                            final LinearLayout resendLayout = sheetView.findViewById(R.id.resendlayout);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    resendLayout.setVisibility(View.VISIBLE);
                                }
                            }, 15000);
                            final OTPEditText gototp = sheetView.findViewById(R.id.custom_unique_edittext);

                            Button check = sheetView.findViewById(R.id.check);
                            check.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (OTP.equals(gototp.getEnteredText())) {
                                        alert.dismiss();
                                        otpListener.onValid();
                                    } else {
                                        Toast.makeText(context, "Wrong OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            sheetDialog.setView(sheetView);
                            sheetDialog.setCancelable(false);
                            sheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    otpListener.onInvalid(new Exception("CancelledOTP"));
                                }
                            });

                            alert = sheetDialog.create();
                            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alert.show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ErrorLinkO", error.toString());
                            otpListener.onInvalid(new Exception("Error in ResponseOTP"));
                        }
                    });
            ApplicationActivity.getInstance().addToRequestQueue(request);
        } else {
            otpListener.onInvalid(new Exception("NO INTERNET"));
        }

    }

    public interface OTPListener {
        public void onValid();

        public void onInvalid(Exception e);
    }
}
