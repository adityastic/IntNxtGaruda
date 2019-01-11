package com.nxtvision.capitalstar.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.data.NewsInfo;
import com.thefinestartist.finestwebview.FinestWebView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    Context context;
    ArrayList<NewsInfo> list;

    public NewsAdapter(Context context, ArrayList<NewsInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsHolder(LayoutInflater.from(context).inflate(R.layout.recycler_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, final int position) {
        final NewsInfo news = list.get(position);

        holder.name.setText(news.title);
        holder.desc.setText(news.desc);
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(news.date));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FinestWebView.Builder(context).show(news.link);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView name, desc, date;

        public NewsHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cv_news_title);
            desc = itemView.findViewById(R.id.cv_news_content);
            date = itemView.findViewById(R.id.date_time);
        }
    }
}
