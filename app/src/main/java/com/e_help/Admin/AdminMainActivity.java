package com.e_help.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.e_help.R;
import com.e_help.User.FirstActivity;
import com.e_help.User.RegisterActivity;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // ح تتنشأ اوتوماتيكلي
        setContentView(R.layout.activity_admin_main); // نربط xml فيها

        findViewById(R.id.accept_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(AdminMainActivity.this, AcceptUserActivity.class); //هنا قلتله روح للاكسبت يوزر اكتفتي
                startActivity(newActivity); // عشان يشغلي الاكتفيتي اللي فوق كاني باقله show
            }
        });
        findViewById(R.id.accept_opportunities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(AdminMainActivity.this, OpportunitiesAcceptActivity.class);
                startActivity(newActivity);
            }
        });

        // زر تسجيل الخروج
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//  قفل الاكتفيتي اللي انا موجودة فيه حيرجع يوديني على صفحة login
            }
        });
    }
}