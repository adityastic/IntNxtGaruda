package com.nxtvision.capitalstar.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.application.ApplicationActivity;
import com.nxtvision.capitalstar.data.TabInfo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.nxtvision.capitalstar.utils.Common.SERVICES_TAB_LIST;

public class ServicesMainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter viewPagerAdapter;

    public ServicesMainFragment() {
    }

    public static Fragment newInstance() {
        return new ServicesMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_services, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SERVICES_TAB_LIST, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<TabInfo> list = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        list.add(new TabInfo(jsonObject.getString("name"), jsonObject.getString("jsonlink")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                tabLayout.setVisibility(View.VISIBLE);
                viewPagerAdapter = new PagerAdapter(getChildFragmentManager(),list);
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(getContext(), "Error in Services", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }

                return volleyError;
            }
        };
        ApplicationActivity.getInstance().addToRequestQueue(request);

        return view;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<TabInfo> list;

        public PagerAdapter(FragmentManager fm, ArrayList<TabInfo> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return new ServicesFragment(list.get(position).getJson());
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTitle();
        }
    }
}