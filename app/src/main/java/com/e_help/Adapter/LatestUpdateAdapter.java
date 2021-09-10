package com.e_help.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.HelpModel;
import com.e_help.R;
import com.e_help.Volunteer.Activity.DetailesImmediateHelpActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class LatestUpdateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HelpModel.LatestUpdatesModel> list;
    Context context;

    public LatestUpdateAdapter(Context context, List<HelpModel.LatestUpdatesModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_latest_help, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final HelpModel.LatestUpdatesModel updatesModel = list.get(position);
        holder1.time.setText(updatesModel.getFormattedTime());
        holder1.details.setText(updatesModel.getName());

        if (!updatesModel.getImg_url().equals("")) {
            holder1.img.setVisibility(View.VISIBLE);
            Picasso.get().load(updatesModel.getImg_url()).into(holder1.img);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView time, details;

        ImageView img;

        public Holder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            details = (TextView) itemView.findViewById(R.id.details);
            img = (ImageView) itemView.findViewById(R.id.img);

        }


    }

}






