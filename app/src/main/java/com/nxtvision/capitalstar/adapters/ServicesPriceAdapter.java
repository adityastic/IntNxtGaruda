package com.nxtvision.capitalstar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxtvision.capitalstar.R;
import com.nxtvision.capitalstar.data.ServicePrice;
import com.nxtvision.capitalstar.utils.Common;

import java.util.ArrayList;

import static com.nxtvision.capitalstar.utils.Common.THEME_BLUE;
import static com.nxtvision.capitalstar.utils.Common.THEME_GREEN;
import static com.nxtvision.capitalstar.utils.Common.THEME_GREY;
import static com.nxtvision.capitalstar.utils.Common.THEME_MULBERRY;
import static com.nxtvision.capitalstar.utils.Common.THEME_RED;
import static com.nxtvision.capitalstar.utils.Common.THEME_ROSYBROWN;
import static com.nxtvision.capitalstar.utils.Common.THEME_SALMON;
import static com.nxtvision.capitalstar.utils.Common.THEME_SEAGREEN;
import static com.nxtvision.capitalstar.utils.Common.THEME_WINE;

public class ServicesPriceAdapter extends RecyclerView.Adapter<ServicesPriceAdapter.ServicesPriceHolder> {

    Context context;
    ArrayList<ServicePrice> list;
    ServicePrice selected;
    Drawable bg;

    public ServicesPriceAdapter(Context context, ArrayList<ServicePrice> list) {
        this.context = context;
        this.list = list;

        switch (Common.selectedTheme) {
            case THEME_RED:
                bg = context.getResources().getDrawable(R.color.redPrimary);
                break;
            case THEME_BLUE:
                bg = context.getResources().getDrawable(R.color.bluePrimary);
                break;
            case THEME_GREEN:
                bg = context.getResources().getDrawable(R.color.greenPrimary);
                break;
            case THEME_GREY:
                bg = context.getResources().getDrawable(R.color.greyPrimary);
                break;
            case THEME_SALMON:
                bg = context.getResources().getDrawable(R.color.salmonPrimary);
                break;
            case THEME_SEAGREEN:
                bg = context.getResources().getDrawable(R.color.seagreenPrimary);
                break;
            case THEME_MULBERRY:
                bg = context.getResources().getDrawable(R.color.mulberryPrimary);
                break;
            case THEME_ROSYBROWN:
                bg = context.getResources().getDrawable(R.color.rosybrownPrimary);
                break;
            case THEME_WINE:
                bg = context.getResources().getDrawable(R.color.winePrimary);
                break;
        }
    }

    public ServicePrice getSelectedValue() {
        return selected;
    }

    @NonNull
    @Override
    public ServicesPriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicesPriceHolder(LayoutInflater.from(context).inflate(R.layout.recycler_servicesprice_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ServicesPriceHolder holder, int position) {
        final ServicePrice sp = list.get(position);

        if (sp.name.equals("Custom")) {
            holder.name.setText(sp.name);
            holder.price.setVisibility(View.GONE);
        } else {
            holder.name.setText(sp.name);
            if (sp.price != 0)
                holder.price.setText("₹ " + String.valueOf(sp.price));
            else
                holder.price.setText("NA");

            holder.price.setVisibility(View.VISIBLE);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.price != 0) {
                    if (selected != sp) {
                        selected = sp;
                    } else {
                        selected = null;
                    }
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (selected == sp) {
            holder.card.setBackground(bg);
            holder.name.setTextColor(Color.parseColor("#ffffff"));
            holder.price.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.card.setBackground(context.getResources().getDrawable(R.color.bg));
            holder.name.setTextColor(Color.parseColor("#000000"));
            holder.price.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ServicesPriceHolder extends RecyclerView.ViewHolder {
        TextView price, name;
        LinearLayout card;

        public ServicesPriceHolder(View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            card = itemView.findViewById(R.id.card);

        }
    }
}
