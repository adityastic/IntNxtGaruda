package com.nxtvision.tradenivesh.fragments;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.application.ApplicationActivity;
import com.nxtvision.tradenivesh.data.Question;
import com.nxtvision.tradenivesh.utils.Common;
import com.nxtvision.tradenivesh.views.CustomSetScrollViewPager;
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

import static com.nxtvision.tradenivesh.utils.Common.questionsList;

public class RPMFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomSetScrollViewPager viewPager;
    Button button;

    private ArrayList<RPMAnswersFragment> allfragments;
    private ArrayList<String> allans;

    private TextView rpmdone;
    private RelativeLayout rpmlayout;

    public RPMFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rpm, container, false);

    }

    boolean done = false;

    public static Fragment newInstance() {
        return new RPMFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        viewPager = view.findViewById(R.id.viewpager);
        button = view.findViewById(R.id.submit);

        button.setText("NEXT");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ans = allfragments.get(viewPager.getCurrentItem()).getSelectedRadio();
                allans.add(ans);
                if (viewPager.getCurrentItem() == allfragments.size() - 2) {
                    button.setText("SUBMIT");
                } else if (viewPager.getCurrentItem() == allfragments.size() - 1) {
                    done = true;

                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < Common.questionsList.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("question", Common.questionsList.get(i));
                            jsonObject.put("answer", allans.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }

                    String uri = Uri.parse(Common.RPMQUESTIONS_LINK)
                            .buildUpon()
                            .appendQueryParameter("phone", Common.loginDetails.phone)
                            .appendQueryParameter("apply", "true")
                            .appendQueryParameter("value", jsonArray.toString())
                            .build().toString();

                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                            uri,
                            null
                            , new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject code = (JSONObject) response.get(0);
                                if (code.getInt("code") == 202) {
                                    Toast.makeText(getContext(), "Submitted", Toast.LENGTH_SHORT).show();

                                    rpmdone.setVisibility(View.VISIBLE);
                                    rpmlayout.setVisibility(View.GONE);

                                }else
                                    Toast.makeText(getContext(), "Error in Submitting", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VolleyError", error.toString());
                            Toast.makeText(getContext(), "Error in Submitting Result", Toast.LENGTH_SHORT).show();
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
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        rpmdone = view.findViewById(R.id.rpmdone);
        rpmlayout = view.findViewById(R.id.questionslayout);

        refreshLayout();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

        {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });
    }

    private void setupViewPager() {
        //Calling the Adapter and adding fragments to add the Viewpager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        for (int i = 0; i < Common.questionsList.size(); i++) {
            adapter.addFragment(RPMAnswersFragment.newInstance(questionsList.get(i)));
        }

        //Setting the Adapter
        viewPager.setAdapter(adapter);
    }

    public void refreshLayout() {
        mSwipeRefreshLayout.setRefreshing(true);

        rpmdone.setVisibility(View.GONE);
        rpmlayout.setVisibility(View.GONE);

        allfragments = new ArrayList<>();
        allans = new ArrayList<>();

        String uri = Uri.parse(Common.RPMQUESTIONS_LINK)
                .buildUpon()
                .appendQueryParameter("phone", Common.loginDetails.phone)
                .appendQueryParameter("apply", "false")
                .appendQueryParameter("value", "")
                .build().toString();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                uri,
                null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("JSONDATA", response.toString());
                JSONObject json = null;
                try {
                    json = response.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (json.has("code")) {
                    rpmdone.setVisibility(View.VISIBLE);
                    rpmlayout.setVisibility(View.GONE);

                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                } else {
                    rpmdone.setVisibility(View.GONE);
                    rpmlayout.setVisibility(View.VISIBLE);
                }

                Common.questionsList = new ArrayList<>();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject question = response.getJSONObject(i);
                        JSONArray answers = question.getJSONArray("answers");

                        ArrayList<String> ans = new ArrayList<>();
                        for (int j = 0; j < answers.length(); j++) {
                            ans.add((String) answers.get(j));
                        }

                        Common.questionsList.add(new Question(question.getString("question"), ans));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                setupViewPager();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(getContext(), "Error in RPM", Toast.LENGTH_SHORT).show();
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

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return allfragments.get(position);
        }

        @Override
        public int getCount() {
            return allfragments.size();
        }

        public void addFragment(RPMAnswersFragment fragment) {
            //Adding Fragments and there Titles to the Adapter
            allfragments.add(fragment);
        }
    }
}
