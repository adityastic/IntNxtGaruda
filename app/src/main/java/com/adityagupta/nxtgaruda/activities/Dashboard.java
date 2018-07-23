package com.adityagupta.nxtgaruda.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.fragments.AboutUsFragment;
import com.adityagupta.nxtgaruda.fragments.MorningBellFragment;
import com.adityagupta.nxtgaruda.fragments.NewsFragment;
import com.adityagupta.nxtgaruda.fragments.RPMFragment;
import com.adityagupta.nxtgaruda.fragments.RecomFragment;
import com.adityagupta.nxtgaruda.fragments.ServicesFragment;
import com.adityagupta.nxtgaruda.fragments.TrackSheetFragment;
import com.adityagupta.nxtgaruda.smartview.WebViewActivity;
import com.adityagupta.nxtgaruda.utils.Common;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thefinestartist.finestwebview.FinestWebView;

import static com.adityagupta.nxtgaruda.utils.Common.CHANNEL_1_ID;
import static com.adityagupta.nxtgaruda.utils.Common.CHANNEL_2_ID;
import static com.adityagupta.nxtgaruda.utils.Common.KYC_LINK;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_BLUE;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_GREEN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_GREY;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_MULBERRY;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_RED;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_ROSYBROWN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_SALMON;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_SEAGREEN;
import static com.adityagupta.nxtgaruda.utils.Common.THEME_WINE;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, MorningBellFragment.newInstance()).addToBackStack(null).commit();
                getSupportActionBar().setTitle("Opening Bell");
            }
        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ServicesFragment.newInstance()).addToBackStack(null).commit();

        fmenu = findViewById(R.id.menu_red);
        com.github.clans.fab.FloatingActionButton fab1 = findViewById(R.id.fab1);
        com.github.clans.fab.FloatingActionButton fab2 = findViewById(R.id.fab2);
        com.github.clans.fab.FloatingActionButton fab3 = findViewById(R.id.fab3);
        com.github.clans.fab.FloatingActionButton fab4 = findViewById(R.id.fab4);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ServicesFragment.newInstance()).addToBackStack(null).commit();
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
        changeFABMENU();
    }

    private void createFirebaseMessagingInit() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token", "Token: " + token);

        FirebaseMessaging.getInstance().subscribeToTopic("garudaNotifications");
        Common.notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Morning Bell",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Morning Bell Channel");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Recommendation",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Recommendation Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, ServicesFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Services");
            fmenu.setVisibility(View.VISIBLE);
        } else if (id == R.id.TrackSheet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, TrackSheetFragment.newInstance()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("TrackSheet");
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
                    drawer.closeDrawer(GravityCompat.START,true);
                }
            },500);
            return false;
        } else if (id == R.id.logout) {
            Common.sharedPreferences.edit().remove("Username").apply();
            Common.sharedPreferences.edit().remove("Password").apply();
            this.finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
