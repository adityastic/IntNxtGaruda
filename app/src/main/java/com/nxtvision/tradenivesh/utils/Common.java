package com.nxtvision.tradenivesh.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.NotificationManagerCompat;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.data.LoginDetails;
import com.nxtvision.tradenivesh.data.NewsInfo;
import com.nxtvision.tradenivesh.data.Question;
import com.nxtvision.tradenivesh.views.OTPEditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Common {
    //    private static String IP = "http://nxtgaruda.com/";
    private static String IP = "http://tradenivesh.com/android/";

    public static final String THEME_LINK = IP + "Theme.json";
    public static final String LOGIN_LINK = IP + "login.php?uname=%s&pass=%s";
    public static final String VERIFY_LINK = IP + "verify.php?uname=%s";
    public static final String REGISTER_LINK = IP + "registration.php?name=%s&email=%s&number=%s&gender=%s&dob=%s&pass=%s";
    public static final String PAYCOMPLETE_LINK = IP + "payComplete.php?number=%s&amount=%s&payid=%s&sname=%s&sterm=%s&gateway=%s";
    public static final String KYC_LINK = IP + "kyc.php";
    public static final String TRACKSHEET_NAMES_LINK = IP + "Tracksheet_names.php";
    public static final String TRACKSHEET_LINK = IP + "sheetfiles/";
    public static final String REPORTS_NAMES_LINK = IP + "Reports_names.php";
    public static final String REPORTS_LINK = IP + "reportfiles/";

    public static final String SERVICES_TAB_LIST = IP + "tabs.json";
    public static final String NEWS_LINK = IP + "news.json";
    //    public static final String RECOM_LINK = IP + "recom.json";
//    public static final String MORNINGBELL_LINK = IP + "morning.json";
    public static final String ABOUTUS_LINK = IP + "aboutus.json";
    public static final String RPMQUESTIONS_LINK = IP + "APIRpm.php?";

    public static final String REGISTER_DEVID = IP + "register_device.php?number=%s&devid=%s";


    public static final String SMS_LINK = "http://sms.suninfolabs.com/api/sendhttp.php?authkey=254636AJZZd8jXrD5c2c9de2&mobiles=%s&message=%s&sender=TRDNIV&route=4&country=0";
//    public static final String SMS_LINK = "http://sms.suninfolabs.com/api/sendhttp.php?authkey=A6fc96504e34eed980c8151cf3771b9fc&mobiles=%s&message=%s&sender=TRDNIV&route=4&country=0";

    //PAUMONEY MERCHANT ACCOUNT INFO
    public static final String PAYU_HASHLINK = IP + "getHash.php";
    //    public static final String PAYU_MERCHANTID = "4901198";
    public static final String PAYU_MERCHANTKEY = "T75sZw";
    public static final String PAYU_MERCHANTSALT = "vAoWMOY0";

    public static final String CHANNEL_ID = "NotifChannel";
//    public static final String CHANNEL_2_ID = "recommendChannel";

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

    public static ArrayList<String> tracksheetList;
    public static LoginDetails loginDetails;
    public static SharedPreferences sharedPreferences;
    public static ArrayList<NewsInfo> newsList;


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
                                    "Your Login OTP For " + context.getResources().getString(R.string.app_name) + "-App is " + OTP
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

    public static String saveNotification(Context context, String title) throws JSONException, ParseException {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Log.e("Notification", "<<<<<<<<<<<>>>>>>>>>>>");
        Log.e("Notification", "Recieved");
        Log.e("Last Update", sharedPreferences.getString("last_update", ""));
        Log.e("Morning", sharedPreferences.getString("morning", ""));
        Log.e("Recom", sharedPreferences.getString("recom", ""));
        if (sharedPreferences.getString("last_update", "").equals("")) {
            Log.e("Notification", "First Time");
            Log.e("Last Update", sharedPreferences.getString("last_update", ""));
            Log.e("Morning", sharedPreferences.getString("morning", ""));
            Log.e("Recom", sharedPreferences.getString("recom", ""));
            sharedPreferences.edit().putString("last_update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())).apply();

            JSONArray array = new JSONArray(sharedPreferences.getString("morning", "[]"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);

            Date d = Calendar.getInstance().getTime();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

            jsonObject.put("date", date);

            array.put(jsonObject);

            sharedPreferences.edit().putString("morning", array.toString()).apply();
            Log.e("Notification", "First Time After");
            Log.e("Last Update", sharedPreferences.getString("last_update", ""));
            Log.e("Morning", sharedPreferences.getString("morning", ""));
            Log.e("Recom", sharedPreferences.getString("recom", ""));
            return "morning";
        } else {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sharedPreferences.getString("last_update", "")));
            Calendar cal2 = Calendar.getInstance();

            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            String res;
            if (sameDay) {
                Log.e("Notification", "NFT SameDay");
                Log.e("Last Update", sharedPreferences.getString("last_update", ""));
                Log.e("Morning", sharedPreferences.getString("morning", ""));
                Log.e("Recom", sharedPreferences.getString("recom", ""));
                JSONArray array = new JSONArray(sharedPreferences.getString("recom", "[]"));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);

                Date d = Calendar.getInstance().getTime();
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

                jsonObject.put("date", date);

                array.put(jsonObject);

                sharedPreferences.edit().putString("recom", array.toString()).apply();
                res = "recommendation";
            } else {
                Log.e("Notification", "NFT NotSameDay");
                Log.e("Last Update", sharedPreferences.getString("last_update", ""));
                Log.e("Morning", sharedPreferences.getString("morning", ""));
                Log.e("Recom", sharedPreferences.getString("recom", ""));
                JSONArray array = new JSONArray(sharedPreferences.getString("morning", "[]"));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);

                Date d = Calendar.getInstance().getTime();
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);

                jsonObject.put("date", date);

                array.put(jsonObject);

                sharedPreferences.edit().putString("morning", array.toString()).apply();
                res = "morning";
            }
            sharedPreferences.edit().putString("last_update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())).apply();
            Log.e("Notification", "Not Time After");
            Log.e("Last Update", sharedPreferences.getString("last_update", ""));
            Log.e("Morning", sharedPreferences.getString("morning", ""));
            Log.e("Recom", sharedPreferences.getString("recom", ""));
            return res;
        }
    }

    public static void enableAutoStart(final Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.getBoolean("dontallow", true)) {
            AlertDialog dialog;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Enable AutoStart");
            alertDialog.setMessage("Please allow us to always run in the background,else our services can't be accessed when you are in distress");
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.miui.securitycenter",
                                "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                        context.startActivity(intent);
                    } else if (Build.BRAND.equalsIgnoreCase("Letv")) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.letv.android.letvsafe",
                                "com.letv.android.letvsafe.AutobootManageActivity"));
                        context.startActivity(intent);

                    } else if (Build.BRAND.equalsIgnoreCase("Honor")) {

                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.huawei.systemmanager",
                                "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                        context.startActivity(intent);
                    } else if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
                        try {
                            Intent intent = new Intent();
                            intent.setClassName("com.coloros.safecenter",
                                    "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                Intent intent = new Intent();
                                intent.setClassName("com.oppo.safe",
                                        "com.oppo.safe.permission.startup.StartupAppListActivity");
                                context.startActivity(intent);
                            } catch (Exception ex) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setClassName("com.coloros.safecenter",
                                            "com.coloros.safecenter.startupapp.StartupAppListActivity");
                                    context.startActivity(intent);
                                } catch (Exception exx) {

                                }
                            }
                        }
                    } else if (Build.MANUFACTURER.contains("vivo")) {
                        try {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.iqoo.secure",
                                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            try {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                                context.startActivity(intent);
                            } catch (Exception ex) {
                                try {
                                    Intent intent = new Intent();
                                    intent.setClassName("com.iqoo.secure",
                                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                                    context.startActivity(intent);
                                } catch (Exception exx) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                    dialog.dismiss();
                }
            });

            alertDialog.setNegativeButton("Dont show again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedPreferences.edit().putBoolean("dontallow",false);
                    dialog.dismiss();
                }
            });

            dialog = alertDialog.create();
            dialog.show();
        }
    }

    public interface OTPListener {
        public void onValid();

        public void onInvalid(Exception e);
    }
}
