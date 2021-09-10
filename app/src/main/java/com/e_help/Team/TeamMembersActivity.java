package com.e_help.Team;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Adapter.TeamMembersAdapter;
import com.e_help.Model.MemberModel;
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

import java.util.ArrayList;
import java.util.List;

public class TeamMembersActivity extends AppCompatActivity {
    List<MemberModel> resultsList;
    TeamMembersAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    DatabaseReference mdatabase;

    TextView no_data;
    String Uid;
    ProgressDialog dialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_members_activity);
        dialogM = new ProgressDialog(this);
        dialogM.setMessage("جاري الاعلان يرجى الانتظار ...");
        dialogM.setIndeterminate(true);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");

        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User  user = dataSnapshot.getValue(User.class);
                    if (user.isIs_ads()){
                        findViewById(R.id.remove_ads_order_member).setVisibility(View.VISIBLE);//يظهر زر الالغاء
                        findViewById(R.id.ads_order_member).setVisibility(View.GONE);//يلغي زر الاعلان

                        findViewById(R.id.remove_ads_order_member).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//لمن يضغط زي الالغاء
                                dialogM.show();
                                //حتسير قيمة is_ads = false
                                databaseReference.child("is_ads").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogM.dismiss();
                                        Toast.makeText(TeamMembersActivity.this, "تم الغاء الاعلان بنجاح", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }else {//لو غير مفعل الاعلان
                        findViewById(R.id.remove_ads_order_member).setVisibility(View.GONE);
                        findViewById(R.id.ads_order_member).setVisibility(View.VISIBLE);
                        findViewById(R.id.ads_order_member).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//لمن يضغط زر الاعلان
                                dialogM.show();//حتسير قيمة is_ads = True
                                databaseReference.child("is_ads").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogM.dismiss();
                                        Toast.makeText(TeamMembersActivity.this, "تم الاعلان بنجاح", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish();
            }
        });




        findViewById(R.id.add_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(TeamMembersActivity.this, AddMemberActivity.class);
                startActivity(newActivity);
            }
        });

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("TeamMembers").child(Uid);

        no_data = findViewById(R.id.no_data);
        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new TeamMembersAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);

        progress_bar.setVisibility(View.VISIBLE);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MemberModel memberModel = snapshot.getValue(MemberModel.class);
                    resultsList.add(memberModel);
                    nAdapter.notifyDataSetChanged();
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
                Toast.makeText(TeamMembersActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}