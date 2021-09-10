package com.e_help.Organisation;

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
import com.e_help.User.RegisterActivity;
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


public class EditProfileOrgActivity extends AppCompatActivity {

    EditText name_org, phone_num_org, email_org;
    Spinner city,TheField;
    Button save;
    DatabaseReference mdatabase;
    ProgressDialog mDialog;

    String Uid = "";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile_org);

        save = (Button) findViewById(R.id.save);


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        city = (Spinner) findViewById(R.id.city);
        GetCity(city);
        TheField = (Spinner) findViewById(R.id.TheField);
        GetTheField(TheField);

        name_org = (EditText) findViewById(R.id.name_org);
        phone_num_org = (EditText) findViewById(R.id.phone_num_org);
        email_org = (EditText) findViewById(R.id.email_org);


        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    name_org.setText(user.getName_entity() + "");
                    phone_num_org.setText(user.getPhone_num_entity() + "");
                    email_org.setText(user.getEmail() + "");
                    selectedCity = user.getCity_entity_id();
                    selectedNameCity = user.getCity_entityName();
                    city.setSelection(selectedCity);

                    selectedTheField = user.getThe_field_entity_id();
                    selectedNameTheField = user.getThe_field_entityName();
                    TheField.setSelection(selectedTheField);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileOrgActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(name_org.getText().toString().trim())) {
            Toast.makeText(EditProfileOrgActivity.this, "أدخل اسم المنظمة", Toast.LENGTH_SHORT).show();
            name_org.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone_num_org.getText().toString().trim())) {
            Toast.makeText(EditProfileOrgActivity.this, "أدخل رقم الموبايل", Toast.LENGTH_SHORT).show();
            phone_num_org.requestFocus();
            return false;
        } else if (!isValidPhone(phone_num_org.getText().toString().trim())) {
            Toast.makeText(EditProfileOrgActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone_num_org.requestFocus();
            return false;
        } else if (!isValidEmail(email_org.getText().toString().trim())) {
            Toast.makeText(EditProfileOrgActivity.this, "أدخل الايميل بشكل صحيح", Toast.LENGTH_SHORT).show();
            email_org.requestFocus();
            return false;
        }  else if (selectedCity == 0) {
            Toast.makeText(EditProfileOrgActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
            return false;
        }  else if (selectedTheField == 0) {
            Toast.makeText(EditProfileOrgActivity.this, "أدخل المجال", Toast.LENGTH_SHORT).show();
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

        User userNew = new User(user.getId(), name_org.getText().toString() + "",
                 phone_num_org.getText().toString() + "",
                user.getId_num_entity() + "", user.getEmail() + "", user.getEntity_typeName() + "",
                user.getEntity_type_id(), selectedNameTheField,selectedTheField, selectedNameCity, selectedCity,3,user.isActive());

        mdatabase.setValue(userNew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();

                Toast.makeText(EditProfileOrgActivity.this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
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

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(EditProfileOrgActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
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

    int selectedTheField = 0;
    String selectedNameTheField;

    public void GetTheField(Spinner the_field) {

        List<dataModel> listModels = new ArrayList<>();
        listModels.add(new dataModel(0, "اختر المجال"));
        listModels.add(new dataModel(1, "التعليم"));
        listModels.add(new dataModel(2, "الصحة"));
        listModels.add(new dataModel(3, "الاماكن المقدسة"));
        listModels.add(new dataModel(4, "الجمعيات الخيرية"));
        listModels.add(new dataModel(5, "البيئة"));
        listModels.add(new dataModel(6, "الترفيه"));
        listModels.add(new dataModel(7, "افتراضي"));
        listModels.add(new dataModel(8, "التدريب والتطوير"));
        listModels.add(new dataModel(9, "الرعاية بالمسنين"));
        listModels.add(new dataModel(10, "تنمية المجتمع"));
        listModels.add(new dataModel(11, "رعاية الاطفال"));
        listModels.add(new dataModel(12, "خدمات سكنية"));

        final String[] name = new String[listModels.size()];
        final String[] idList = new String[listModels.size()];

        for (int i = 0; i < listModels.size(); i++) {
            name[i] = listModels.get(i).getName();
            idList[i] = listModels.get(i).getId() + "";
        }

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(EditProfileOrgActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
        the_field.setAdapter(adapterPiece);
        the_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                } catch (Exception ex) {
                }
                if (item != null) {
                    selectedTheField = Integer.parseInt(idList[position]);
                    selectedNameTheField = name[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
