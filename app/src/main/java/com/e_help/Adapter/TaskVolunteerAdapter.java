package com.e_help.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Model.TaskModel;
import com.e_help.Notification.APIService;
import com.e_help.Notification.Notifications.Client;
import com.e_help.Notification.Notifications.Data;
import com.e_help.Notification.Notifications.MyResponse;
import com.e_help.Notification.Notifications.Sender;
import com.e_help.Notification.Notifications.Token;
import com.e_help.R;
import com.e_help.Team.OpportunitiesParticipantsActivity;
import com.e_help.Volunteer.Activity.TeamJoinActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskVolunteerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaskModel> list;
    Context context;

    public TaskVolunteerAdapter(Context context, List<TaskModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;
        TaskModel taskModel = list.get(position);
        holder1.title.setText(taskModel.getTitle() + "");
        holder1.description.setText(taskModel.getDescription() + "");
        holder1.date.setText(taskModel.getExpired() + "");
        holder1.user.setText(taskModel.getUser() + "");

        holder1.finish.setOnClickListener(new View.OnClickListener() {//لمن نضغط زر " انهاء المهمة"
            @Override// لمن نضعط الزر ح يترسل اشعار لقائد الفريق انو تمت المهمة
            public void onClick(View v) {
                sendNotification(taskModel.getId_team(),// اي دي قائد الفريق الي حيترسله الاشعار"المستقبل" + الرسالة
                        "تمت المهمة  "+taskModel.getTitle()+" للعضو "+taskModel.getUser() +" بنجاح");//لقائد الفريق
                Toast.makeText(context, "تمت المهمة بنجاح", Toast.LENGTH_SHORT).show();//للمتطوع

            }
        });


    }
    //ارسال الاشعارات للمستخدمين
    private void sendNotification(String receiver, final String message) {
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        DatabaseReference tokens = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference("Tokens");
        //بيجيب الداتا من الفايربيس على حسب دي الجملة
        //اول كنا نجيب كل الداتا ونقارن فيها
        //طريقة لعرض ممكن تكون في list - قارن فيها البيانات
        Query query = tokens.orderByKey().equalTo(receiver);//حيجيب التوكن تبعه
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

                    Data data = new Data(fuser.getUid(), "" + message, "Ehelp");
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        TextView title, description,date,user;

        Button finish;
        public Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            user = (TextView) itemView.findViewById(R.id.user);
            finish = (Button) itemView.findViewById(R.id.finish);
        }
    }
}