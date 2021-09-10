package com.e_help.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e_help.Admin.AdminMainActivity;
import com.e_help.Model.User;
import com.e_help.R;
import com.e_help.Volunteer.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    EditText Email, Password;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);

        dialog = new ProgressDialog(this);
        dialog.setMessage("جاري تسجيل الدخول يرجى الانتظار ...");
        dialog.setIndeterminate(true);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSign();
            }
        });

    }

    private void userSign() {
        if (TextUtils.isEmpty(Email.getText().toString().trim())) {
            Toast.makeText(LoginActivity.this, "أدخل البريد الإلكتروني الصحيح", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Password.getText().toString().trim())) {
            Toast.makeText(LoginActivity.this, "أدخل كلمة المرور الصحيحة", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString().trim()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this,
                                    "تسجيل الدخول لم ينجح تأكد من الاتصال بالانترنت او الايميل وكلمة المرور", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

        boolean isPressed=true;
        private void checkIfEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        if (users.isEmailVerified()) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                    .getReference().child("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()&&isPressed)
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user.getEmail().equalsIgnoreCase(Email.getText().toString())) {
                                if (user.isActive()) {
                                    dialog.dismiss();
                                    //تخزين البيانات لحين الحاجة اليها يتم استدعاءها بسرعة
                                    SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                                    editor.putString("Uid", users.getUid());
                                    editor.putString("email", Email.getText().toString());
                                    editor.putInt("UserType", user.getUser_type());
                                    if (user.getUser_type() == 1) {
                                        editor.putString("nameVal", user.getFirst_name() + " " + user.getLast_name());
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else if (user.getUser_type() == 2) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else if (user.getUser_type() == 3) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else if (user.getUser_type() == 10) {
                                        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                        startActivity(intent);
                                    }
                                    editor.apply();
                                    LoginActivity.this.finish();
                                    isPressed=false;
                                }else {
                                    isPressed=false;
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "يحتاج الى تفعيل من قبل الادمن", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "حدث خطا في عملية تسجيل الدخول" , Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(this, "تحقق من البريد الإلكتروني", Toast.LENGTH_SHORT).show();
        }
    }


}