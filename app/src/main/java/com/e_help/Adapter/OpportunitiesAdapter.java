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

import java.util.ArrayList;
import java.util.List;


public class OpportunitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    private List<OpportunitiesModel> list;
    Context context;
    private List<OpportunitiesModel> contactListFiltered;

    public OpportunitiesAdapter(Context context, List<OpportunitiesModel> List1) {
        this.context = context;
        this.list = List1;
        this.contactListFiltered = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_opportunities, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final OpportunitiesModel orderModel = contactListFiltered.get(position);

        holder1.name.setText(orderModel.getName());
        holder1.jeha.setText("الجهة : "+orderModel.getName_organization());
        holder1.date.setText("المدة : من"+orderModel.getTime_start() + " الى "+orderModel.getTime_end());
        holder1.num_valunteer.setText("عدد المتطوعين : "+orderModel.getNum_valunteer()+"");
        holder1.city.setText(orderModel.getCity()+"");

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailesOpportunitiesActivity.class);
                intent.putExtra("ID", orderModel.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                } else {
                    List<OpportunitiesModel> filteredList = new ArrayList<>();
                    for (OpportunitiesModel row : list) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<OpportunitiesModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView name, jeha, city, date, num_valunteer;
        ImageView logo;

        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            jeha = (TextView) itemView.findViewById(R.id.jeha);
            date = (TextView) itemView.findViewById(R.id.date);

            city = (TextView) itemView.findViewById(R.id.city);
            num_valunteer = (TextView) itemView.findViewById(R.id.num_valunteer);
            logo = (ImageView) itemView.findViewById(R.id.logo);
        }


    }

}






