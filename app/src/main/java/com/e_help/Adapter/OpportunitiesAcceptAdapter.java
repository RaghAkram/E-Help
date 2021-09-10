package com.e_help.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.OpportunitiesModel;
import com.e_help.Opportunities.DetailesOpportunitiesActivity;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OpportunitiesAcceptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OpportunitiesModel> list;
    Context context;

    public OpportunitiesAcceptAdapter(Context context, List<OpportunitiesModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_opportunities_accept, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final OpportunitiesModel model = list.get(position);

        holder1.name.setText(model.getName()+"");
        holder1.date.setText("من "+model.getTime_start() + " الى "+model.getTime_end());
        holder1.num_valunteer.setText(""+model.getNum_valunteer()+"");
        holder1.city.setText(model.getCity()+"");

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailesOpportunitiesActivity.class);
                intent.putExtra("ID", model.getId());
                context.startActivity(intent);
            }
        });

        holder1.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                        // set message, title, and icon
                        .setTitle("قبول")
                        .setMessage("هل انت متأكد من القبول؟")
                        .setPositiveButton("قبول", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                dialog.dismiss();
                                holder1.dialog1.show();
                                holder1.mdatabase.child(model.getId())
                                        .child("active").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder1.dialog1.dismiss();
                                        Toast.makeText(context, "تم قبول الطلب", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });
                            }

                        })
                        .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
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
        DatabaseReference mdatabase;
        ProgressDialog dialog1;
        TextView name, city, date, num_valunteer;
        TextView logo;
        Button accept;


        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            accept = (Button) itemView.findViewById(R.id.accept);
            date = (TextView) itemView.findViewById(R.id.date);

            city = (TextView) itemView.findViewById(R.id.city);
            num_valunteer = (TextView) itemView.findViewById(R.id.num_valunteer);
            logo= (TextView) itemView.findViewById(R.id.logo);

            mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().child("Opportunities");

            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("الرجاء الانتظار ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);
        }
    }
}