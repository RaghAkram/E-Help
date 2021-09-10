package com.e_help.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.OpportunitiesModel;
import com.e_help.Opportunities.DetailesOpportunitiesActivity;
import com.e_help.R;
import com.e_help.Team.OpportunitiesParticipantsActivity;

import java.util.ArrayList;
import java.util.List;


public class OpportunitiesOfferdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<OpportunitiesModel> list;
    Context context;

    public OpportunitiesOfferdAdapter(Context context, List<OpportunitiesModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_opportunities_offerd, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final OpportunitiesModel orderModel = list.get(position);

        holder1.name.setText(orderModel.getName()+"");
        holder1.date.setText("من "+orderModel.getTime_start() + " الى "+orderModel.getTime_end());
        holder1.num_valunteer.setText(""+orderModel.getNum_valunteer()+"");
        holder1.city.setText(orderModel.getCity()+"");

        holder1.logo.setText((position+1)+"");
        holder1.participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpportunitiesParticipantsActivity.class);
                intent.putExtra("ID", orderModel.getId());
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
        TextView name, city, date, num_valunteer;
        TextView logo;
        Button participants;

        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            participants = (Button) itemView.findViewById(R.id.participants);
            date = (TextView) itemView.findViewById(R.id.date);

            city = (TextView) itemView.findViewById(R.id.city);
            num_valunteer = (TextView) itemView.findViewById(R.id.num_valunteer);
            logo= (TextView) itemView.findViewById(R.id.logo);
        }


    }

}






