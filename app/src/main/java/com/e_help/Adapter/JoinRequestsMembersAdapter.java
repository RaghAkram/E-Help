package com.e_help.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.MemberModel;
import com.e_help.Model.RequestModel;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class JoinRequestsMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RequestModel> list;
    Context context;

    public JoinRequestsMembersAdapter(Context context, List<RequestModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request_team, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;
        SharedPreferences preferences = context.getSharedPreferences("login", MODE_PRIVATE);
        String UID = preferences.getString("Uid", "");
        RequestModel requestModel = list.get(position);
        holder1.name.setText(requestModel.getFname() + " " + requestModel.getLname());
        holder1.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1.dialog1.show();
                DatabaseReference reference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                        getReference().child("TeamMembers").child(UID);


                String key = reference.push().getKey();

                MemberModel model = new MemberModel(key, UID, requestModel.getId_userJoin() + "", requestModel.getEmail()
                        , requestModel.getFname(), requestModel.getLname(), true);

                //لاضافة العضو
                reference.child(requestModel.getId_userJoin()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder1.dialog1.dismiss();
                        Toast.makeText(context, "تمت القبول بنجاح", Toast.LENGTH_SHORT).show();
                        //لازالة الطلب بعد الموافقة عليه
                        holder1.mdatabase.child(requestModel.getId_userJoin())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder1.dialog1.dismiss();
                                notifyDataSetChanged();


                            }
                        });
                    }
                });


            }
        });


        holder1.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //لرفض الطلب
                holder1.dialog1.show();
                holder1.mdatabase.child(requestModel.getId_userJoin())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder1.dialog1.dismiss();
                        Toast.makeText(context, "تم الرفض", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    }
                });

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
        TextView name, yes, no;
        DatabaseReference mdatabase;
        ProgressDialog dialog1;

        public Holder(View itemView) {
            super(itemView);
            SharedPreferences preferences = context.getSharedPreferences("login", MODE_PRIVATE);

            name = (TextView) itemView.findViewById(R.id.name);
            yes = (TextView) itemView.findViewById(R.id.yes);
            no = (TextView) itemView.findViewById(R.id.no);

            mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                    getReference().child("Join_requests").child(preferences.getString("Uid", ""));

            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("الرجاء الانتظار ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);
        }
    }
}