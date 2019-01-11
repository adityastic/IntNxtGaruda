package com.nxtvision.capitalstar.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.adapters.TrackSheetListAdapter;
import com.nxtvision.capitalstar.application.ApplicationActivity;
import com.nxtvision.capitalstar.utils.Common;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nxtvision.capitalstar.utils.Common.LAYOUT_GRID;
import static com.nxtvision.capitalstar.utils.Common.LAYOUT_LINEAR;
import static com.nxtvision.capitalstar.utils.Common.TRACKSHEET_NAMES_LINK;

public class TrackSheetFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager recycleLayoutManager;
    TrackSheetListAdapter mAdapter;

    public TrackSheetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    public static Fragment newInstance() {
        return new TrackSheetFragment();
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

        if(!isStoragePermissionGranted())
        {
        }

        refreshLayout();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    public void refreshLayout() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter = null;
        mRecyclerView.setAdapter(null);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, TRACKSHEET_NAMES_LINK, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("JSONDATA", response.toString());
                Common.tracksheetList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        Common.tracksheetList.add(jsonObject.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mAdapter = new TrackSheetListAdapter(getContext(), Common.tracksheetList);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(getContext(), "Error in TrackSheet", Toast.LENGTH_SHORT).show();
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
                @Override
                public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", "NxtGARUDA");
                return headers;
            }
        };

        ApplicationActivity.getInstance().addToRequestQueue(request);
    }
}
