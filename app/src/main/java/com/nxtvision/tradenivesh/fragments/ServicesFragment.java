package com.nxtvision.tradenivesh.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.adapters.ServicesAdapter;
import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.data.ServicesInfo;
import com.nxtvision.tradenivesh.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.nxtvision.tradenivesh.utils.Common.LAYOUT_GRID;
import static com.nxtvision.tradenivesh.utils.Common.LAYOUT_LINEAR;

@SuppressLint("ValidFragment")
public class ServicesFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager recycleLayoutManager;
    ServicesAdapter mAdapter;

    String json;

    ArrayList<ServicesInfo> list;

    @SuppressLint("ValidFragment")
    public ServicesFragment(String json) {
        this.json = json;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    private void changeLayout() {
        switch (Common.selectedLayout) {
            case LAYOUT_LINEAR:
                recycleLayoutManager = new LinearLayoutManager(getContext());
                break;
            case LAYOUT_GRID:
                recycleLayoutManager = new GridLayoutManager(getContext(),2);
                break;
            default:
                recycleLayoutManager = new LinearLayoutManager(getContext());
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeLayout();
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

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, json, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("JSONDATA", response.toString());
                list = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        list.add(new ServicesInfo(
                                jsonObject.getString("name"),
                                jsonObject.getString("des"),
                                jsonObject.getString("det"),
                                Double.parseDouble(jsonObject.getString("monthly")),
                                Double.parseDouble(jsonObject.getString("quaterly")),
                                Double.parseDouble(jsonObject.getString("hyearly")),
                                Double.parseDouble(jsonObject.getString("yearly"))
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mAdapter = new ServicesAdapter(getContext(), list);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(getContext(), "Error in Services", Toast.LENGTH_SHORT).show();
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
