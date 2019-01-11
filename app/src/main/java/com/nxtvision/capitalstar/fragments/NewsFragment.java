package com.nxtvision.capitalstar.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.adapters.NewsAdapter;
import com.nxtvision.capitalstar.application.ApplicationActivity;
import com.nxtvision.capitalstar.data.NewsInfo;
import com.nxtvision.capitalstar.utils.Common;
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

import static com.nxtvision.capitalstar.utils.Common.NEWS_LINK;

public class NewsFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager recycleLayoutManager;
    NewsAdapter mAdapter;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    public static Fragment newInstance() {
        return new NewsFragment();
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

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, NEWS_LINK, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("JSONDATA", response.toString());
                Common.newsList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        Common.newsList.add(new NewsInfo(
                                jsonObject.getString("info"),
                                jsonObject.getString("des"),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("date")),
                                jsonObject.getString("li")
                        ));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }

                Collections.sort(Common.newsList, new Comparator<NewsInfo>() {
                    @Override
                    public int compare(NewsInfo o1, NewsInfo o2) {
                        return o2.date.compareTo(o1.date);
                    }
                });

                mAdapter = new NewsAdapter(getContext(), Common.newsList);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(getContext(), "Error in News", Toast.LENGTH_SHORT).show();
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
