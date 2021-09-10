package com.e_help.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Adapter.UsersAcceptAdapter;
import com.e_help.Model.User;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptUserActivity extends AppCompatActivity {
    List<User> resultsList; // عشان اخزن المستخدمين قبل ان يتم قبولهم
    UsersAcceptAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_user);


        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users"); // هنا عشان اوصل للجدول نفسه


        no_data = findViewById(R.id.no_data); //معتمدة على الريسايكلر فيو - لمن مافي داتا ح تعرضها
        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //هنا تنعرض النتايج لليست تحت بعض - ممكن تكون كشبكة لو كانت GridLayoutManager
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true); //يكون متجاوب معانا لمن نعرض اشياء كتير

        nAdapter = new UsersAcceptAdapter(this, resultsList); //عرفنا الادابتر واعطيناه ليست عشان يعرضها
        recyclerView.setAdapter(nAdapter); //ربطنا الادابتر في الريسايكلر فيو

        progress_bar.setVisibility(View.VISIBLE);// الدائرة الي تدور اظهرها

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);// الدائرة الي تدور اخفيها
                //هنا الفايربيس يعطينا البيانات في ملف
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // هنا جبنا كل اليوزرز لاننا ما حطينا شروط فوق
                    User user = snapshot.getValue(User.class); // هنا قسمنا الداتا على شكل يوزرز
                    if (!user.isActive()){//للتأكد من ان المستخدم غير مفعل يكون الاكتف false
                        if (user.getUser_type()==2||user.getUser_type()==3){//للتأكد من ان المستخدم منظمة او فريق
                            resultsList.add(user); // هنا حيعرض كل اليوزرز الغير مفعلين + سواء كانو فريق او منظمة
                        }
                    }
                    nAdapter.notifyDataSetChanged();
                }
                if (resultsList.size() == 0) { // لو كان حجم الليست صفر اعرضلي التكست فيو "no data"
                    no_data.setVisibility(View.VISIBLE); // خليها مرئية
                } else {
                    no_data.setVisibility(View.GONE);//  مخفية

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { // هنا لو سار خطأ او شي بيطلعلنا التكست فيو " لا يوجد داتا"
                Toast.makeText(AcceptUserActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}