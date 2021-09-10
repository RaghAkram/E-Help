package com.e_help.Team;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Model.HelpModel;
import com.e_help.Model.MemberModel;
import com.e_help.Model.User;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AddMemberActivity extends AppCompatActivity {
    EditText email_member;
    DatabaseReference mdatabase;
    String Uid;
    ProgressDialog dialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        dialogM = new ProgressDialog(this);
        dialogM.setMessage("جاري الحفظ يرجى الانتظار ...");
        dialogM.setIndeterminate(true);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email_member = (EditText) findViewById(R.id.email_member);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("TeamMembers").child(Uid);

        findViewById(R.id.invitation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogM.show();
                if (!email_member.getText().toString().trim().equals("")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                            .getReference().child("Users");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                boolean ex = false;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user.getUser_type() == 1) {
                                        if (email_member.getText().toString().equals(user.getEmail())) {
                                            String key = mdatabase.push().getKey();
                                            ex = true;
                                            MemberModel memberModel = new MemberModel(key, Uid, user.getId(), user.getEmail()
                                                    , user.getFirst_name(), user.getLast_name(), true);
                                            mdatabase.child(user.getId()).setValue(memberModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    dialogM.dismiss();
                                                    Toast.makeText(AddMemberActivity.this, "تمت الدعوة بنجاح", Toast.LENGTH_SHORT).show();

                                                    sendEmail(email_member.getText().toString(), user.getName_team());
                                                    email_member.setText("");

                                                }
                                            });

                                        }
                                    }

                                }
                                if (!ex) {
                                    Toast.makeText(AddMemberActivity.this,
                                            "البريد الالكتروني المضاف غير متوفر", Toast.LENGTH_SHORT).show();
                                    dialogM.dismiss();
                                }

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(AddMemberActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            }
        });
    }

    protected void sendEmail(String email, String Name_team) {

        String[] TO = {email + ""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-help");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "تمت دعوتك للانضمام الى فريق " + Name_team + " الرجاء مراجعة التطبيق لقبول او رفض الدهوة");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddMemberActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }
}