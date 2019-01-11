package com.nxtvision.capitalstar.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.adapters.PanelAdapter;
import com.nxtvision.capitalstar.data.PanelInfo;
import com.nxtvision.capitalstar.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PanelFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public PanelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    public static Fragment newInstance() {
        return new PanelFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (!isStoragePermissionGranted()) {
        }

        try {
            refreshLayout();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void refreshLayout() throws JSONException {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        PanelAdapter mAdapter = null;
        mRecyclerView.setAdapter(null);


        ArrayList<PanelInfo> panelList = new ArrayList<>();

        Log.e("Panel", Common.sharedPreferences.getString("panel", "[]"));
        JSONArray response = new JSONArray(Common.sharedPreferences.getString("panel", "[]"));
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) response.get(i);
                panelList.add(new PanelInfo(
                        jsonObject.getString("file"),
                        jsonObject.getString("info"),
                        jsonObject.getString("link"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("date"))
                ));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(panelList, new Comparator<PanelInfo>() {
            @Override
            public int compare(PanelInfo o1, PanelInfo o2) {
                return o2.date.compareTo(o1.date);
            }
        });

        if (panelList.size() > 150)
            panelList = new ArrayList<>(panelList.subList(0, 150));

        JSONArray arr = new JSONArray();

        for (PanelInfo re : panelList) {
            JSONObject obj = new JSONObject();
            obj.put("file", re.file);
            obj.put("info", re.info);
            obj.put("link", re.link);
            obj.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(re.date));
            arr.put(obj);
        }

        Common.sharedPreferences.edit().putString("panel", arr.toString()).apply();

        mAdapter = new PanelAdapter(getContext(), panelList);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
