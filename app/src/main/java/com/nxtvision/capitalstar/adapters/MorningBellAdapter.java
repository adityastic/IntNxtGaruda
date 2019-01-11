package com.nxtvision.capitalstar.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.data.RecomInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MorningBellAdapter extends RecyclerView.Adapter<MorningBellAdapter.RecomHolder> {

    Context context;
    ArrayList<RecomInfo> list;

    public MorningBellAdapter(Context context, ArrayList<RecomInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecomHolder(LayoutInflater.from(context).inflate(R.layout.recycler_recom_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecomHolder holder, final int position) {
        final RecomInfo news = list.get(position);

        holder.name.setText(news.title);
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(news.date));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecomHolder extends RecyclerView.ViewHolder {

        TextView name, date;

        public RecomHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
}
