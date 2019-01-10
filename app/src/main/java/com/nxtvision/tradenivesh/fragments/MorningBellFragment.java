package com.nxtvision.tradenivesh.fragments;

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

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.adapters.RecomAdapter;
import com.nxtvision.tradenivesh.data.RecomInfo;
import com.nxtvision.tradenivesh.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MorningBellFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager recycleLayoutManager;
    RecomAdapter mAdapter;

    ArrayList<RecomInfo> morningBellList;

    public MorningBellFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    public static Fragment newInstance() {
        return new MorningBellFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleLayoutManager = new LinearLayoutManager(getContext());
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(recycleLayoutManager);

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

    private void refreshLayout() throws JSONException {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter = null;
        mRecyclerView.setAdapter(null);

        morningBellList = new ArrayList<>();

        Log.e("Morning",Common.sharedPreferences.getString("morning","[]"));
        JSONArray response = new JSONArray(Common.sharedPreferences.getString("morning","[]"));
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) response.get(i);
                morningBellList.add(new RecomInfo(
                        jsonObject.getString("title"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("date"))
                ));
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(morningBellList, new Comparator<RecomInfo>() {
            @Override
            public int compare(RecomInfo o1, RecomInfo o2) {
                return o2.date.compareTo(o1.date);
            }
        });

        if (morningBellList.size() > 10)
            morningBellList = new ArrayList<>(morningBellList.subList(0, 10));

        JSONArray arr = new JSONArray();

        for (RecomInfo re: morningBellList) {
            JSONObject obj = new JSONObject();
            obj.put("title",re.title);
            obj.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(re.date));
            arr.put(obj);
        }

        Common.sharedPreferences.edit().putString("morning",arr.toString()).apply();

        mAdapter = new RecomAdapter(getContext(), morningBellList);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
