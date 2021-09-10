package com.e_help.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.HelpModel;
import com.e_help.R;
import com.e_help.Volunteer.Activity.DetailesImmediateHelpActivity;

import java.util.List;


public class ImmediateHelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HelpModel> list;
    Context context;

    public ImmediateHelpAdapter(Context context, List<HelpModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_immediate_help, parent, false));
    }
    //لزر Join الانضمام
    //هنا ما عملنا اكشن على الداتا بيس
    //مجرد فتح لصفحة
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;
        //نعرف فيه المتغيرات تبعنا =  holder
        //عملنا اوبجكت عشان نربط onBindViewHolder مع Holder
        //ولمن استدعي اي عنصر في xml حاربطه بالمتغير holder
        final HelpModel orderModel = list.get(position);
        holder1.title_type.setText(orderModel.getType());
        holder1.city.setText(orderModel.getCity());
        holder1.hour.setText(orderModel.getTime_end()+"hr");

        holder1.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailesImmediateHelpActivity.class);
                intent.putExtra("ID", orderModel.getId());//نعرضله صفحة التفاصيل حسب id المرسل من join من جدول helpModel
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
        TextView title_type, city, hour;

        Button join;

        public Holder(View itemView) {
            super(itemView);
            title_type = (TextView) itemView.findViewById(R.id.title_type);
            city = (TextView) itemView.findViewById(R.id.city);
            hour = (TextView) itemView.findViewById(R.id.hour);
            join = (Button) itemView.findViewById(R.id.join);

        }


    }

}






