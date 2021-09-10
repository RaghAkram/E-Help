package com.e_help.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.User;
import com.e_help.Model.dataModel;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsersAcceptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> list;
    Context context;

    public UsersAcceptAdapter(Context context, List<User> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_accept, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final User user = list.get(position);
        if (user.getUser_type() == 2) {
            holder1.name.setText("فريق:"+user.getName_team() + "");

            holder1.phone.setText(user.getPhone_num_team() + "");
            holder1.id_num.setText(user.getId_num_leader() + "");
            holder1.city.setText(user.getCityName() + "");
            holder1.the_field.setText(holder1.listModels.get(user.getThe_field_id()).getName() + "");
            holder1.description.setText(user.getDescription() + "");

        } else if (user.getUser_type() == 3) {
            holder1.name.setText("منظمة:"+user.getName_entity() + "");

            holder1.phone.setText(user.getPhone_num_entity() + "");
            holder1.id_num.setText(user.getId_num_entity() + "");
            holder1.city.setText(user.getCity_entityName() + "");
            holder1.the_field.setText(holder1.listModels.get(user.getThe_field_entity_id()).getName() + "");
            holder1.description.setVisibility(View.GONE);
        }




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
                                holder1.mdatabase.child(user.getId())
                                        .child("Active").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        TextView name, accept,phone,id_num,city,the_field,description;
        DatabaseReference mdatabase;
        ProgressDialog dialog1;

        List<dataModel> listModels;
        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            accept = (TextView) itemView.findViewById(R.id.accept);
            mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().child("Users");
            phone = (TextView) itemView.findViewById(R.id.phone);
            id_num = (TextView) itemView.findViewById(R.id.id_num);
            city = (TextView) itemView.findViewById(R.id.city);
            the_field = (TextView) itemView.findViewById(R.id.the_field);
            description = (TextView) itemView.findViewById(R.id.description);

            listModels=new ArrayList<>();
            listModels.add(new dataModel(0, "اختر المجال"));
            listModels.add(new dataModel(1, "التعليم"));
            listModels.add(new dataModel(2, "الصحة"));
            listModels.add(new dataModel(3, "الاماكن المقدسة"));
            listModels.add(new dataModel(4, "الجمعيات الخيرية"));
            listModels.add(new dataModel(5, "البيئة"));
            listModels.add(new dataModel(6, "الترفيه"));
            listModels.add(new dataModel(7, "افتراضي"));
            listModels.add(new dataModel(8, "التدريب والتطوير"));
            listModels.add(new dataModel(9, "الرعاية بالمسنين"));
            listModels.add(new dataModel(10, "تنمية المجتمع"));
            listModels.add(new dataModel(11, "رعاية الاطفال"));
            listModels.add(new dataModel(12, "خدمات سكنية"));
            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("الرجاء الانتظار ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);
        }
    }
}