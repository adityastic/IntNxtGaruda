package com.adityagupta.nxtgaruda.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.application.ApplicationActivity;
import com.adityagupta.nxtgaruda.data.TabInfo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.adityagupta.nxtgaruda.utils.Common.SERVICES_TAB_LIST;

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