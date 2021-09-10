package com.e_help.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.OpportunitieRegisterModel;
import com.e_help.R;
import com.e_help.Team.OpportunitiesParticipantsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class OpportunitiesParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OpportunitieRegisterModel> list;
    Context context;

    public OpportunitiesParticipantsAdapter(Context context, List<OpportunitieRegisterModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_opportunity_member, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final OpportunitieRegisterModel model = list.get(position);

        holder1.name.setText(model.getNameMember() + "");

        if (model.isAccepted()) {
            holder1.accepted.setVisibility(View.VISIBLE);
            holder1.accepted.setText("تم الحضور");
            holder1.delete.setVisibility(View.GONE);
        } else {
            holder1.delete.setVisibility(View.VISIBLE);
            holder1.accepted.setVisibility(View.VISIBLE);
            holder1.accepted.setText("تأكيد الحضور");

            holder1.accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!model.isAccepted()) {
                        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                                // set message, title, and icon
                                .setTitle("تأكيد حضور")
                                .setMessage("هل انت متأكد من حضور العضو؟")
                                .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //your deleting code
                                        dialog.dismiss();
                                        holder1.dialog1.show();
                                        holder1.mdatabase.child(model.getId()).child(model.getId_member())
                                                .child("accepted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                holder1.dialog1.dismiss();
                                                Toast.makeText(context, "تم تأكيد الحضور", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();

                                                //لزيادة عدد النقاط على حسب عدد ساعات التطوع
                                                holder1.mdatabaseUser.child(model.getId_member())
                                                        .child("point").runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        Integer score = mutableData.getValue(Integer.class);
                                                        if (score == null) {
                                                            return Transaction.success(mutableData);
                                                        }
                                                        mutableData.setValue(score + model.getNum_h());

                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                                    }
                                                });
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
                }
            });
        }

        holder1.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                        // set message, title, and icon
                        .setTitle("حذف")
                        .setMessage("هل انت متأكد من حذف العضو؟")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("حذف", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                dialog.dismiss();
                                holder1.dialog1.show();
                                holder1.mdatabase.child(model.getId()).child(model.getId_member())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder1.dialog1.dismiss();
                                        Toast.makeText(context, "تم الحذف", Toast.LENGTH_SHORT).show();
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
        TextView name, delete, accepted;
        ProgressDialog dialog1;
        DatabaseReference mdatabase,mdatabaseUser;

        public Holder(View itemView) {
            super(itemView);
            SharedPreferences preferences = context.getSharedPreferences("login", MODE_PRIVATE);

            name = (TextView) itemView.findViewById(R.id.name);
            delete = (TextView) itemView.findViewById(R.id.delete);
            accepted = (TextView) itemView.findViewById(R.id.accepted);

            mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                    child("OpportunitiesRegister").child(preferences.getString("Uid", ""));
            mdatabaseUser = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                    child("Users");

            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("الرجاء الانتظار ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);
        }


    }

}






