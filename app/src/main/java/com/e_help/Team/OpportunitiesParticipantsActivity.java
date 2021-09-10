package com.e_help.Team;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Adapter.OpportunitiesParticipantsAdapter;
import com.e_help.Model.OpportunitieRegisterModel;
import com.e_help.Notification.APIService;
import com.e_help.Notification.Notifications.Client;
import com.e_help.Notification.Notifications.Data;
import com.e_help.Notification.Notifications.MyResponse;
import com.e_help.Notification.Notifications.Sender;
import com.e_help.Notification.Notifications.Token;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpportunitiesParticipantsActivity extends AppCompatActivity {
    List<OpportunitieRegisterModel> resultsList; //المستخدمين المشاركين في الفرصة
    OpportunitiesParticipantsAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    DatabaseReference mdatabase,mdatabaseUser;
    TextView no_data;
    ProgressDialog dialog1;
    String ID = "";
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunities_participants);
        //للربط مع الفيربيس للاشعارات
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        dialog1 = new ProgressDialog(this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage("الرجاء الانتظار ...");
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);
        findViewById(R.id.noti_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(OpportunitiesParticipantsActivity.this)
                        // set message, title, and icon
                        .setTitle("اشعار الجميع")
                        .setMessage("هل انت متأكد من اشعار الجميع؟")
                        .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                dialog.dismiss();
                                dialog1.show();
                                for (int i = 0; i < resultsList.size(); i++) { // هنا يروح لكل اليوزرز في الليست ويلف عليهم ويرسلهم
                                    if (resultsList.get(i).isConfirmAttendance()) { // لو تم التحقق من حضورهم
                                        sendNotification(resultsList.get(i).getId_member(), // حيرسلهم المسج
                                                "تم قبول طلبك بنجاح");
                                        // resultsList.get(i).getId_member() الشخص اللي حارسله الاشعار
                                        dialog1.dismiss();
                                        Toast.makeText(OpportunitiesParticipantsActivity.this, "تم الارسال", Toast.LENGTH_SHORT).show();

                                    }
                                }


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
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                child("OpportunitiesRegister").child(preferences.getString("Uid", "")).child(ID);
        mdatabaseUser = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                child("Users");
        no_data = findViewById(R.id.no_data);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        nAdapter = new OpportunitiesParticipantsAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);


        progress_bar.setVisibility(View.VISIBLE);
        findViewById(R.id.accepted_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //بمجرد المتطوع اكد حضوره ومن ثم قائد الفريق قبل التأكيد ح ينحسب السكور
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(OpportunitiesParticipantsActivity.this)
                        // set message, title, and icon
                        .setTitle("تأكيد حضور الجميع")
                        .setMessage("هل انت متأكد من حضور الجميع؟")
                        .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                dialog.dismiss();
                                dialog1.show();
                                for (int i = 0; i < resultsList.size(); i++) {//لسته لكل الاعضاء اللي سرلهم كونفيرم
                                    if (resultsList.get(i).isConfirmAttendance()) {//هل المتطوع الموجود في اللسته اكد حضوره

                                        int finalI = i;// عشان نحفظ قيمة الـi هوا بدونها بيطلع error
                                        // هنا قائد الفريق بيأكد حضورهم
                                        //نجيب العضو اللي عملناله كونفيرم ونسويله اكسبتد - هنا قبول الجميع وصلو للفرصة+ احسبلهم النقاط
                                        mdatabase.child(resultsList.get(i).getId_member())
                                                .child("accepted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog1.dismiss();
                                                Toast.makeText(OpportunitiesParticipantsActivity.this, "تم", Toast.LENGTH_SHORT).show();
                                                //لزيادة عدد النقاط على حسب عدد ساعات التطوع

                                                //كود لحساب النقاط
                                                mdatabaseUser.child(resultsList.get(finalI).getId_member()) //نحتاج نوصل لداتا بيس عشان النقاط points
                                                        .child("point").runTransaction(new Transaction.Handler() {
                                                           //من الفايربيس وفرولنا runTransaction
                                                          // لمن تكون الداتاعبارة عن intger
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData ) {
                                                        Integer score = mutableData.getValue(Integer.class); // runTransactionجبلي السكور القديم من ميثود
                                                        if (score == null) { // لو مافي اصلا سكور حتى ما يكون 0 - يعني ممكن يعلق النظام
                                                            return Transaction.success(mutableData);
                                                        } // لو في سكور قديمة ضفلي عليها النقاط الجديدة
                                                        //كل ساعة بنقطة
                                                        //يضيف عدد الساعات على السكور القديم
                                                        mutableData.setValue(score + resultsList.get(finalI).getNum_h()); // سكور القديم + عدد الساعات((لان كل ساعة بنقطة= سكور الجديد))"

                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }


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


        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OpportunitieRegisterModel opportunitiesModel = snapshot.getValue(OpportunitieRegisterModel.class);
                    if (opportunitiesModel.isConfirmAttendance()) {
                        resultsList.add(opportunitiesModel);
                        nAdapter.notifyDataSetChanged();
                    }
                    if (!opportunitiesModel.isAccepted()) {
                        findViewById(R.id.accepted_L).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.accepted_L).setVisibility(View.GONE);
                    }
                }
                if (resultsList.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress_bar.setVisibility(View.GONE);
                resultsList.clear();
                nAdapter.notifyDataSetChanged();
                recyclerView.removeAllViews();
                if (resultsList.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);
                }
                Toast.makeText(OpportunitiesParticipantsActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ارسال الاشعارات للمستخدمين
    private void sendNotification(String receiver, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference("Tokens");
        //هنا استعمل تكنيك
        Query query = tokens.orderByKey().equalTo(receiver);//هنا خزن الاشخاص اللي حيرسلهم الاشعار 
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class); // هنا خزنا سناب شوت للمستخدم اللي ابغا ارسله الاشعار داخل اوبجكت من توكن (كاننا شايلينو)
                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser(); // هنا جبنا اليوزر اللي حيرسل اشعار
                      //هنا سوينا اوبجكت لداتا اللي حنرسلها
                    //  Authintication اعطيناها الid لليوزر اللي حيرسل الاشعار - حطيناها لتاكيد انو اليوزر من نفس التطبيق مو تطبيق اخر
                    //والمسج مررناها من فوق والتايتل
                    Data data = new Data(fuser.getUid(), "" + message, "Ehelp");

                    // الداتا ح نعطيها لـsender ونعطيه التوكن لشخص المستقبل (نعطيه لساعي البريد)
                    Sender sender = new Sender(data, token.getToken());

                    // دا الكود اللي يوصل الاشعار
                    apiService.sendNotification(sender) // اعطينا الميثود المرسل وهيا حتتصرف وترسله
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            //Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
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

}