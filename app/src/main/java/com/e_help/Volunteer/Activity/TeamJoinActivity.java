package com.e_help.Volunteer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e_help.Model.RequestModel;
import com.e_help.Model.User;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TeamJoinActivity extends AppCompatActivity {

    Button buttonJoin;
    DatabaseReference mdatabase;
    ProgressDialog mDialog;
    ImageView logo;

    String ID = "",Uid;
    User user;
    TextView name_team, description, city, leader_name;

    int Type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_volunteer_teams_detailes);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("الرجاء الانتظار ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        buttonJoin = (Button) findViewById(R.id.buttonJoin);
        logo = (ImageView) findViewById(R.id.logo);
        name_team = (TextView) findViewById(R.id.name_team);
        description = (TextView) findViewById(R.id.description);
        city = (TextView) findViewById(R.id.city);
        leader_name = (TextView) findViewById(R.id.leader_name);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        Intent intent = getIntent();
        Type = preferences.getInt("UserType", 0);
        ID = intent.getStringExtra("ID");

        if(Type==1){
            buttonJoin.setVisibility(View.VISIBLE);
        }else {
            buttonJoin.setVisibility(View.GONE);
        }

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(ID);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// بنعرض فيها تفاصيل الفريق ولمن المتطوع يبغا يقدم على الفريق - يقدم طلب انضمام
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    name_team.setText(user.getName_team() + "");
                    leader_name.setText(user.getLeader_name() + "");
                    mDialog.dismiss();
                    description.setText(user.getDescription() + "");
                    city.setText(user.getCityName());
                    DatabaseReference mdatabaseJoin = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                            getReference().child("Join_requests");//اضيف فيها طلبات الانضمام

                    buttonJoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                                    .getReference().child("Users");
                            //نجيب بيانات اليوزر ونخزنها في جدول Join request
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists())
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            User user = snapshot.getValue(User.class);
                                            mDialog.dismiss();
                                            if (user.getId().equals(Uid)) {//الId للمتطوع الي عامل لوق ان يقارنها في id  في جدول اليورز
                                                //ممكن نختصرها في .childUser(id
                                                String key = mdatabaseJoin.push().getKey();//باجيبب بيانات اليوزر اللي عامل لوق ان واضيفه في المودل
                                                RequestModel requestModel = new RequestModel(key,ID,Uid,user.getFirst_name()
                                                        ,user.getLast_name(),user.getEmail());//رسلنا بيانات المتطوع - عشان قائد الفريق يشوف يبغا يقبل او لا
                                                mdatabaseJoin.child(ID).child(Uid).setValue(requestModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();
                                                        Toast.makeText(TeamJoinActivity.this, "تم ارسال طلب الانضمام بنجاح", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mDialog.dismiss();

                                    Toast.makeText(TeamJoinActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TeamJoinActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }


}
