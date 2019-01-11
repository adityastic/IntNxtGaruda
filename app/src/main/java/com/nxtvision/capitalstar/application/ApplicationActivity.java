package com.nxtvision.capitalstar.application;

import android.app.Application;
import android.text.TextUtils;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.enums.PayUEnv;
import com.nxtvision.capitalstar.utils.TypefaceUtil;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationActivity extends Application {

    public static final String TAG = ApplicationActivity.class.getSimpleName();
    private static RequestQueue mRequestQueue;
    private static ApplicationActivity mInstance;


    static PayUEnv environment;

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/circular_std_black.otf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/circular_std_book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mInstance = this;

        environment = PayUEnv.PRODUCTION;
    }

    public static PayUEnv getEnvironment() {
        return environment;
    }

    public void setEnvironment(PayUEnv environment) {
        ApplicationActivity.environment = environment;
    }

    public static synchronized ApplicationActivity getInstance() {
        return mInstance;
    }

    public static RequestQueue getQueue(){
        return mRequestQueue;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
