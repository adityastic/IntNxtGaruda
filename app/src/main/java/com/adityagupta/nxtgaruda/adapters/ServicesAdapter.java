package com.adityagupta.nxtgaruda.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.activities.ServiceDescActivity;
import com.adityagupta.nxtgaruda.data.ServicesInfo;
import com.adityagupta.nxtgaruda.utils.Common;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesHolder> {

    Context context;
    ArrayList<ServicesInfo> list;

    public ServicesAdapter(Context context, ArrayList<ServicesInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicesHolder(LayoutInflater.from(context).inflate(R.layout.recycler_services_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, final int position) {
        final ServicesInfo service = list.get(position);

        holder.name.setText(service.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ServiceDescActivity.class);
                i.putExtra("index", position);
                i.putExtra("service", service);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServicesHolder extends RecyclerView.ViewHolder {

        TextView  name;

        public ServicesHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}
