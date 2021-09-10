package com.e_help.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.User;
import com.e_help.R;
import com.e_help.Volunteer.Activity.DetailesImmediateHelpActivity;
import com.e_help.Volunteer.Activity.TeamJoinActivity;

import java.util.List;


public class TeamVolunteerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> list;
    Context context;

    public TeamVolunteerAdapter(Context context, List<User> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;
        User requestModel = list.get(position);
        holder1.name.setText(requestModel.getName_team() + "");

        holder1.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeamJoinActivity.class);
                intent.putExtra("ID", requestModel.getId());

                context.startActivity(intent);

            }
        });


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
        TextView name, show;
        ImageView logo;
        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            show = (TextView) itemView.findViewById(R.id.show);
            logo = (ImageView) itemView.findViewById(R.id.logo);
        }
    }
}