package com.e_help.Team;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e_help.Model.User;
import com.e_help.Model.dataModel;
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
import java.util.regex.Pattern;


public class EditProfileLeaderActivity extends AppCompatActivity {

    EditText name_team, leader_name, phone_num_team, description,
            email_team, social_media;
    Spinner city;
    Button save;
    DatabaseReference mdatabase;
    ProgressDialog mDialog;

    String Uid = "";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile_leader);

        save = (Button) findViewById(R.id.save);


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        city = (Spinner) findViewById(R.id.city);
        GetCity(city);

        //الفريق
        name_team = (EditText) findViewById(R.id.name_team);
        leader_name = (EditText) findViewById(R.id.leader_name);
        social_media = (EditText) findViewById(R.id.social_media);
        phone_num_team = (EditText) findViewById(R.id.phone_num_team);
        email_team = (EditText) findViewById(R.id.email_team);
        description = (EditText) findViewById(R.id.description);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    name_team.setText(user.getName_team() + "");
                    leader_name.setText(user.getLeader_name() + "");
                    social_media.setText(user.getSocial_media() + "");
                    phone_num_team.setText(user.getPhone_num_team() + "");
                    email_team.setText(user.getEmail() + "");
                    description.setText(user.getDescription() + "");
                    selectedCity = user.getCity_id();
                    selectedNameCity = user.getCityName();
                    city.setSelection(selectedCity);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileLeaderActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation())
                    UserEdit();
            }
        });


    }


    public boolean validation() {
        if (TextUtils.isEmpty(name_team.getText().toString().trim())) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل اسم الفريق", Toast.LENGTH_SHORT).show();
            name_team.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(leader_name.getText().toString().trim())) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل اسم القائد", Toast.LENGTH_SHORT).show();
            leader_name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone_num_team.getText().toString().trim())) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل رقم الموبايل", Toast.LENGTH_SHORT).show();
            phone_num_team.requestFocus();
            return false;
        } else if (!isValidPhone(phone_num_team.getText().toString().trim())) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone_num_team.requestFocus();
            return false;
        } else if (!isValidEmail(email_team.getText().toString().trim())) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل الايميل بشكل صحيح", Toast.LENGTH_SHORT).show();
            email_team.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(social_media.getText().toString().trim())) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل اسم حساب التواصل الاجتماعي", Toast.LENGTH_SHORT).show();
            social_media.requestFocus();
            return false;
        } else if (selectedCity == 0) {
            Toast.makeText(EditProfileLeaderActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public static boolean isValidPhone(String s) {
        Pattern Phone = Pattern.compile(
                "^((?:[+?0?0?966]+)(?:\\s?\\d{2})(?:\\s?\\d{7}))$");
        return !TextUtils.isEmpty(s) && Phone.matcher(s).matches();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void UserEdit() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("جاري التعديل الرجاء الانتظار ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        User userNew = new User(user.getId(), name_team.getText().toString() + "",
                leader_name.getText().toString() + "", phone_num_team.getText().toString() + "",
                user.getId_num_leader() + "", user.getEmail() + "", social_media.getText().toString() + "",
                user.getEntity_typeName(), user.getEntity_type_id(), selectedNameCity, selectedCity, 2, description.getText().toString() + "",user.isIs_ads(),user.isActive());

        mdatabase.setValue(userNew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();

                Toast.makeText(EditProfileLeaderActivity.this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }


    int selectedCity = 0;
    String selectedNameCity;

    public void GetCity(Spinner city) {

        List<dataModel> listModels = new ArrayList<>();
        listModels.add(new dataModel(0, "اختر مدينة"));
        listModels.add(new dataModel(1, "المدينة المنورة"));
        listModels.add(new dataModel(2, "ينبع"));
        listModels.add(new dataModel(3, "جدة"));
        listModels.add(new dataModel(4, "مكة"));
        listModels.add(new dataModel(5, "الطايف"));


        final String[] name = new String[listModels.size()];
        final String[] idList = new String[listModels.size()];

        for (int i = 0; i < listModels.size(); i++) {
            name[i] = listModels.get(i).getName();
            idList[i] = listModels.get(i).getId() + "";
        }

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(EditProfileLeaderActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
        city.setAdapter(adapterPiece);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                } catch (Exception ex) {
                }
                if (item != null) {
                    selectedCity = Integer.parseInt(idList[position]);
                    selectedNameCity = name[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
