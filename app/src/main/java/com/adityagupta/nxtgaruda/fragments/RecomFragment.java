package com.adityagupta.nxtgaruda.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.adapters.NewsAdapter;
import com.adityagupta.nxtgaruda.adapters.RecomAdapter;
import com.adityagupta.nxtgaruda.application.ApplicationActivity;
import com.adityagupta.nxtgaruda.data.NewsInfo;
import com.adityagupta.nxtgaruda.data.RecomInfo;
import com.adityagupta.nxtgaruda.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.adityagupta.nxtgaruda.utils.Common.NEWS_LINK;
import static com.adityagupta.nxtgaruda.utils.Common.RECOM_LINK;

public class RecomFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager recycleLayoutManager;
    RecomAdapter mAdapter;

    public RecomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    public static Fragment newInstance() {
        return new RecomFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleLayoutManager = new LinearLayoutManager(getContext());
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(recycleLayoutManager);

        refreshLayout();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });

    }

    public void refreshLayout() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter = null;
        mRecyclerView.setAdapter(null);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RECOM_LINK, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("JSONDATA", response.toString());
                Common.recomList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        Common.recomList.add(new RecomInfo(
                                jsonObject.getString("des"),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("date"))
                        ));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }

                Collections.sort(Common.recomList, new Comparator<RecomInfo>() {
                    @Override
                    public int compare(RecomInfo o1, RecomInfo o2) {
                        return o2.date.compareTo(o1.date);
                    }
                });
                mAdapter = new RecomAdapter(getContext(), Common.recomList);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(getContext(), "Error in Recommendation", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
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
    }
}
