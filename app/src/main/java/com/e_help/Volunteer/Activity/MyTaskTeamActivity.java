package com.e_help.Volunteer.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Adapter.TaskVolunteerAdapter;
import com.e_help.Adapter.TeamMembersAdapter;
import com.e_help.Model.MemberModel;
import com.e_help.Model.TaskModel;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//لازم نجيب بيانات الفريق اولا
public class MyTaskTeamActivity extends AppCompatActivity {
    List<TaskModel> resultsList;
    TaskVolunteerAdapter nAdapter;
    RecyclerView recyclerView;//عرض Scrolling List بها قائمة كبيرة من البيانات
    ProgressBar progress_bar; //الدايره الي تدور
    TextView no_data;
    String Uid;//اخزن فيها id للفريق
    List<String> listTeams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");

        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resultsList = new ArrayList<>();

        no_data = findViewById(R.id.no_data);
        recyclerView = findViewById(R.id.recycler);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new TaskVolunteerAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter); //ربطناها بالادابتر -  يقوم بعرض قائمة العناصر في الـ RecyclerView

        progress_bar.setVisibility(View.VISIBLE);
        DatabaseReference mdatabaseTask = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("Task");
        DatabaseReference mdatabaseTeamMembers = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("TeamMembers");//عشان من خلال حيقدر يوصل للفريق تبعه
        //لجلب الفرق التي تم المشاركة فيها
        mdatabaseTeamMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //كود يجبلي كل المهام الكل الفرق المشترك فيها
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {// اتنين فور لوب عشان دخلت جوا 2 child
                        //لان جوا teams members في كل الفرق
                        //وجوا الفريق في كل المهام
                        //ممكن المتطوع يشترك في اكتر من فريق
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {//هنا حيلف للمهام
                            MemberModel memberModel = snapshot1.getValue(MemberModel.class);//حاجيب كل المودلز للاعضاء
                            if (memberModel.getId_user().equals(Uid)) { //قارنت id user الموجود في التيم ميمبر معid تبعي
                                //يتأكد انو المتطوع دا داخل فريق اصلا
                                //يخزن id للفرق اللي هوا انضملها
                                listTeams.add(memberModel.getId_team());// رويضيفه في listTeam
                            }
                        }
                    }
                    //لالغاء التكرار
                    //لو في تكرار لاي id للفريق
                    Set<String> set = new HashSet<>(listTeams);//تلغي التكرار لمن اعطيها array list
                    listTeams.clear();//وضفناها لليست تيم
                    listTeams.addAll(set);//باخزن list teams في داخل set
                    resultsList.clear();//افضيها واعطيها محتوى set

                    //لعرض المهام
                    //اخدنا array تبع ListTeams
                    for (int i = 0; i < listTeams.size(); i++) {
                        mdatabaseTask.child(listTeams.get(i)).addValueEventListener(new ValueEventListener() {
                            //ح يجيب id للفريق الاول ويجب المهمات اللي فيه
                            //وح يجيب مهمات الفريق التاني وحيعرضها برضو في list teams
                            //ويخزنهم في حاجة اسمهاresultList
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    progress_bar.setVisibility(View.GONE);
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        TaskModel user = snapshot.getValue(TaskModel.class);//عملنا اوبجكت عشان نجيب المهمات
                                        //ولكن قبلها نتاكد ...
                                        //لتأكد من ان المهمة للمتطوع او للجميع
                                        if (user.getUser_id().equals(Uid) || user.getUser_id().equals("0")) {//فقط ح يشوف المهمات الموكلة له + المعينة للجميع
                                            resultsList.add(user);
                                            nAdapter.notifyDataSetChanged();

                                        }

                                    }
                                    if (resultsList.size() == 0) {
                                        no_data.setVisibility(View.VISIBLE);
                                    } else {
                                        no_data.setVisibility(View.GONE);
                                    }

                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(MyTaskTeamActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                progress_bar.setVisibility(View.GONE);
                                resultsList.clear();
                                nAdapter.notifyDataSetChanged();
                                recyclerView.removeAllViews();
                            }
                        });
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyTaskTeamActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}