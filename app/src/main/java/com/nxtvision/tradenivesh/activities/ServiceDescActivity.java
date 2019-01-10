package com.nxtvision.tradenivesh.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.adapters.ServicesPriceAdapter;
import com.nxtvision.tradenivesh.data.ServicePrice;
import com.nxtvision.tradenivesh.data.ServicesInfo;
import com.nxtvision.tradenivesh.utils.Common;
import com.razorpay.Checkout;

import java.util.ArrayList;

import static com.nxtvision.tradenivesh.utils.Common.THEME_BLUE;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_GREY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_MULBERRY;
import static com.nxtvision.tradenivesh.utils.Common.THEME_RED;
import static com.nxtvision.tradenivesh.utils.Common.THEME_ROSYBROWN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SALMON;
import static com.nxtvision.tradenivesh.utils.Common.THEME_SEAGREEN;
import static com.nxtvision.tradenivesh.utils.Common.THEME_WINE;

public class ServiceDescActivity extends AppCompatActivity {

    int index;

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    ServicesPriceAdapter mAdapter;

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
    ServicesInfo servicesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeLayout();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_desc);

        index = getIntent().getIntExtra("index", -1);
        servicesInfo = (ServicesInfo) getIntent().getSerializableExtra("service");
        
        mToolbar = findViewById(R.id.toolbar);
        setUpToolbar();

        Checkout.preload(getApplicationContext());

        ((TextView) findViewById(R.id.description)).setText(servicesInfo.getDesc());
        ((TextView) findViewById(R.id.details)).setText(servicesInfo.getDet());

        mRecyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridlayout = new GridLayoutManager(this, 2);
        gridlayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return ((position + 1) % 5 == 0) ? 2 : 1;
            }
        });
        mRecyclerView.setLayoutManager(gridlayout);

        ArrayList<ServicePrice> list = new ArrayList<>();
        list.add(new ServicePrice("Monthly:", servicesInfo.getMonthly()));
        list.add(new ServicePrice("Quarterly:", servicesInfo.getQuaterly()));
        list.add(new ServicePrice("Half-Yearly:", servicesInfo.getHyearly()));
        list.add(new ServicePrice("Yearly:", servicesInfo.getYearly()));
        list.add(new ServicePrice("Custom", -1));

        mAdapter = new ServicesPriceAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);

        (findViewById(R.id.pay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getSelectedValue() != null)
                    choosePayment();
                else
                    Toast.makeText(ServiceDescActivity.this, "Select a Plan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void choosePayment() {
        Intent i = new Intent(this, ChoosePayment.class);
        i.putExtra("item", mAdapter.getSelectedValue().name.substring(0, mAdapter.getSelectedValue().name.length() - 1));
        i.putExtra("price", mAdapter.getSelectedValue().price);
        i.putExtra("index", index);
        i.putExtra("name", servicesInfo.getName());
        startActivity(i);
        finish();
    }

    private void setUpToolbar() {
        setTitle(servicesInfo.getName());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
