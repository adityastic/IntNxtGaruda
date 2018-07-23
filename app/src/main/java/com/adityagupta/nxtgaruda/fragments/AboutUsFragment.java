package com.adityagupta.nxtgaruda.fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.application.ApplicationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.adityagupta.nxtgaruda.utils.Common.ABOUTUS_LINK;

public class AboutUsFragment extends Fragment {

    TextView name,desc;
    ImageView img;
    LinearLayout about;

    public AboutUsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aboutus, container, false);

    }

    public static Fragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.company);
        desc = view.findViewById(R.id.description);
        img = view.findViewById(R.id.img);
        about = view.findViewById(R.id.LinearAbout);

        about.setVisibility(View.GONE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                ABOUTUS_LINK,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("AboutUS",response.toString());
                        about.setVisibility(View.VISIBLE);

                        try {
                            name.setText(response.getString("name"));
                            desc.setText(response.getString("des"));
                            Picasso.with(getContext())
                                    .load(response.getString("logo"))
                                    .into(img);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                        about.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error in About Us", Toast.LENGTH_SHORT).show();
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
