package com.nxtvision.capitalstar.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.nxtvision.capitalstar.application.ApplicationActivity;
import com.nxtvision.capitalstar.fragments.PanelFragment;
import com.nxtvision.capitalstar.fragments.ReportsFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.fragments.AboutUsFragment;
import com.nxtvision.capitalstar.fragments.MorningBellFragment;
import com.nxtvision.capitalstar.fragments.NewsFragment;
import com.nxtvision.capitalstar.fragments.RPMFragment;
import com.nxtvision.capitalstar.fragments.RecomFragment;
import com.nxtvision.capitalstar.fragments.ServicesMainFragment;
import com.nxtvision.capitalstar.fragments.TrackSheetFragment;
import com.nxtvision.capitalstar.smartview.WebViewActivity;
import com.nxtvision.capitalstar.utils.Common;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.nxtvision.capitalstar.utils.Common.CHANNEL_ID;
import static com.nxtvision.capitalstar.utils.Common.REGISTER_DEVID;
import static com.nxtvision.capitalstar.utils.Common.THEME_BLUE;
import static com.nxtvision.capitalstar.utils.Common.THEME_GREEN;
import static com.nxtvision.capitalstar.utils.Common.THEME_GREY;
import static com.nxtvision.capitalstar.utils.Common.THEME_MULBERRY;
import static com.nxtvision.capitalstar.utils.Common.THEME_RED;
import static com.nxtvision.capitalstar.utils.Common.THEME_ROSYBROWN;
import static com.nxtvision.capitalstar.utils.Common.THEME_SALMON;
import static com.nxtvision.capitalstar.utils.Common.THEME_SEAGREEN;
import static com.nxtvision.capitalstar.utils.Common.THEME_WINE;
import static com.nxtvision.capitalstar.utils.Common.getHTMLString;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout drawer;

    TextView headerName, headerEmail;
    FloatingActionMenu fmenu;

    @Override
    public void onBackPressed() {
        if (fmenu.isOpened()) {
            fmenu.close(true);
        } else {
            finishAffinity();
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

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeLayout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createFirebaseMessagingInit();
        Common.enableAutoStart(this);

        mToolbar = findViewById(R.id.toolbar);
        setUpToolbar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_parent);

        headerEmail = headerLayout.findViewById(R.id.email_header);
        headerName = headerLayout.findViewById(R.id.user_name_header);

        headerName.setText(Common.loginDetails.name + " - " + Common.loginDetails.phone);
        headerEmail.setText(Common.loginDetails.email);

        navigationView.setCheckedItem(R.id.dashboard);

        if (getIntent().hasExtra("type")) {
            if (getIntent().getExtras().getString("type").equals("recommendation")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, RecomFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Recommendations");
            } else if(getIntent().getExtras().getString("type").equals("panel")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, PanelFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Panel");
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, MorningBellFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Opening Bell");
            }
        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ServicesMainFragment.newInstance()).addToBackStack(null).commit();

        fmenu = findViewById(R.id.menu_red);
        com.github.clans.fab.FloatingActionButton fab1 = findViewById(R.id.fab1);
        com.github.clans.fab.FloatingActionButton fab2 = findViewById(R.id.fab2);
        com.github.clans.fab.FloatingActionButton fab3 = findViewById(R.id.fab3);
        com.github.clans.fab.FloatingActionButton fab4 = findViewById(R.id.fab4);
        com.github.clans.fab.FloatingActionButton fab5 = findViewById(R.id.fab5);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ServicesMainFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Services");
                fmenu.close(true);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, MorningBellFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Opening Bell");
                fmenu.close(true);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, RecomFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Recommendations");
                fmenu.close(true);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, NewsFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("News");
                fmenu.close(true);
            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, PanelFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Panel");
                fmenu.close(true);
            }
        });
        changeFABMENU();
    }

    private void createFirebaseMessagingInit() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token", "Token: " + token);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getHTMLString(String.format(
                        REGISTER_DEVID,
                        Common.loginDetails.phone,
                        token
                ))
                ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Registration Device ID", "Success");
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

        Common.notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID,
                    getResources().getString(R.string.app_name) + " Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Morning Bell Channel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

    public void changeFABMENU() {
        final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menu_red);
        fam.getMenuIconView().setImageResource(R.drawable.ic_menu);
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fam.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fam.getMenuIconView().setImageResource(R.drawable.ic_menu);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        fam.setIconToggleAnimatorSet(set);
    }

    private void setUpToolbar() {
        setTitle("Services");
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ServicesMainFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Services");
            fmenu.setVisibility(View.VISIBLE);
        } else if (id == R.id.TrackSheet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, TrackSheetFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("TrackSheet");
            fmenu.setVisibility(View.GONE);
        } else if (id == R.id.Reports) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ReportsFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Reports");
            fmenu.setVisibility(View.GONE);
        } else if (id == R.id.aboutus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, AboutUsFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("About US");
            fmenu.setVisibility(View.GONE);
        } else if (id == R.id.rpm) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, RPMFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("RPM");
            fmenu.setVisibility(View.GONE);
        } else if (id == R.id.kyc) {
            Intent i = new Intent(Dashboard.this, WebViewActivity.class);
            startActivity(i);
//            new FinestWebView.Builder(this)
//                    .updateTitleFromHtml(false)
//                    .titleDefault("Fill KYC Form")
//                    .showUrl(false)
//                    .showMenuOpenWith(false)
//                    .showMenuCopyLink(false)
//                    .showMenuShareVia(false)
//                    .show(KYC_LINK);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawer.closeDrawer(GravityCompat.START, true);
                }
            }, 500);
            return false;
        } else if (id == R.id.logout) {
            Common.sharedPreferences.edit().remove("Username").apply();
            Common.sharedPreferences.edit().remove("Password").apply();
            this.finishAffinity();
        } else if (id == R.id.help) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.company_email)});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT, "");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if (id == R.id.callus) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "07316661369"));//change the number
            if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.CALL_PHONE}, 30);
            } else {
                startActivity(callIntent);
            }
            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 30: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "07316661369"));//change the number
                    startActivity(callIntent);
                } else {

                }
                return;
            }
        }
    }
}
