package com.e_help.Volunteer.Activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e_help.Model.User;
import com.e_help.Model.dataModel;
import com.e_help.R;
import com.e_help.User.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class EditProfileVolunteerActivity extends AppCompatActivity {

    EditText first_name, last_name, phone_num, date_of_birth,
            email_vol, id_num,Voluntary_interests,Educational_level;
    Spinner city;
    Button save;
    DatabaseReference mdatabase;
    ProgressDialog mDialog;

    String Uid = "";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile_volunteer);

        save = (Button) findViewById(R.id.save);


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        city = (Spinner) findViewById(R.id.city);
        GetCity(city);

        //المتطوع
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        phone_num = (EditText) findViewById(R.id.phone_num);
        date_of_birth = (EditText) findViewById(R.id.date_of_birth);
        getTime(date_of_birth);
        email_vol = (EditText) findViewById(R.id.email_vol);
        id_num = (EditText) findViewById(R.id.id_num);
        Voluntary_interests = (EditText) findViewById(R.id.Voluntary_interests);
        Educational_level = (EditText) findViewById(R.id.Educational_level);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    first_name.setText(user.getFirst_name() + "");
                    last_name.setText(user.getLast_name() + "");
                    phone_num.setText(user.getPhone_num() + "");
                    date_of_birth.setText(user.getDate_of_birth() + "");
                    email_vol.setText(user.getEmail() + "");
                    id_num.setText(user.getId_num() + "");
                    Voluntary_interests.setText(user.getVoluntary_interests() + "");
                    Educational_level.setText(user.getEducational_level() + "");

                    selectedCity = user.getCity_entity_id();
                    selectedNameCity = user.getCity_entityName();
                    city.setSelection(selectedCity);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileVolunteerActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(first_name.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل الاسم الاول", Toast.LENGTH_SHORT).show();
            first_name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(last_name.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل الاسم الاخير", Toast.LENGTH_SHORT).show();
            last_name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone_num.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل رقم الموبايل", Toast.LENGTH_SHORT).show();
            phone_num.requestFocus();
            return false;
        } else if (!isValidPhone(phone_num.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone_num.requestFocus();
            return false;
        } else if (!isValidEmail(email_vol.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل الايميل بشكل صحيح", Toast.LENGTH_SHORT).show();
            email_vol.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(date_of_birth.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل تاريخ الميلاد", Toast.LENGTH_SHORT).show();
            date_of_birth.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(id_num.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل رقم الهوية الوطنية", Toast.LENGTH_SHORT).show();
            id_num.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(Voluntary_interests.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل الاهتمامات التطوعية", Toast.LENGTH_SHORT).show();
            Voluntary_interests.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(Educational_level.getText().toString().trim())) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل المستوى التعليمي", Toast.LENGTH_SHORT).show();
            Educational_level.requestFocus();
            return false;
        } else if (selectedCity == 0) {
            Toast.makeText(EditProfileVolunteerActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
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

        User userNew = new User(user.getId(), first_name.getText().toString() + "",
                last_name.getText().toString() + "", phone_num.getText().toString() + "",
                user.getId_num() + "", user.getEmail() + "", date_of_birth.getText().toString() + "",
                Voluntary_interests.getText().toString(), Educational_level.getText().toString(), user.getGenderName(),user.getGender_id(),selectedNameCity, selectedCity,
                1, user.isActive(),user.getPoint());

        mdatabase.setValue(userNew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();

                Toast.makeText(EditProfileVolunteerActivity.this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
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

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(EditProfileVolunteerActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
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
    public void getTime(final EditText editText) {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileVolunteerActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editText.setText(sdf.format(date.getTime()));
                editText.setError(null);

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datePickerDialog.show();
            }
        });
        datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());

    }


}
