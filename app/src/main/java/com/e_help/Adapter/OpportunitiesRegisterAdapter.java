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

import com.e_help.Model.OpportunitieRegisterModel;
import com.e_help.Opportunities.DetailesOpportunitiesActivity;
import com.e_help.R;
import com.e_help.Volunteer.Activity.DetailesCheckSiteActivity;

import java.util.ArrayList;
import java.util.List;


public class OpportunitiesRegisterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<OpportunitieRegisterModel> list;
    Context context;

    public OpportunitiesRegisterAdapter(Context context, List<OpportunitieRegisterModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_opportunities_register, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final OpportunitieRegisterModel model = list.get(position);

        holder1.name.setText(model.getNameOpportunitie()+" "+model.getNameTeam());
        holder1.city.setText(model.getCity()+"");

        holder1.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, DetailesCheckSiteActivity.class);
                intent.putExtra("ID", model.getId());
                intent.putExtra("IdTeam", model.getIdTeam());

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
        TextView name, city;

        Button register;

        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            city = (TextView) itemView.findViewById(R.id.city);
            register = (Button) itemView.findViewById(R.id.register);
        }


    }

}






