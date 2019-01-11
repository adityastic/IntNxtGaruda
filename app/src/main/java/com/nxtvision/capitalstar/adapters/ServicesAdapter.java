package com.nxtvision.capitalstar.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.activities.ServiceDescActivity;
import com.nxtvision.capitalstar.data.ServicesInfo;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesHolder> {

    Context context;
    ArrayList<ServicesInfo> list;

    public ServicesAdapter(Context context, ArrayList<ServicesInfo> list) {
        this.context = context;
        this.list = list;
        this.list.add(new ServicesInfo("", "", "", 0.0, 0.0, 0.0, 0.0));
        this.list.add(new ServicesInfo("", "", "", 0.0, 0.0, 0.0, 0.0));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicesHolder(LayoutInflater.from(context).inflate(R.layout.recycler_services_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, final int position) {
        final ServicesInfo service = list.get(position);

        if (!service.getName().equals("")) {
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
            holder.itemView.setVisibility(View.VISIBLE);
        }else
            holder.itemView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServicesHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ServicesHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}
