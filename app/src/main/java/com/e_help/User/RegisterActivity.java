package com.e_help.User;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    EditText first_name, last_name, phone_num, id_num, email, password, confirm_password, date_of_birth, Voluntary_interests, Educational_level;
    Spinner gender,city_vol;

    EditText name_team, leader_name, phone_num_team, id_num_leader, email_team, password_team, confirm_password_team, social_media;
    Spinner the_field, city;

    EditText name_entity, phone_num_entity, id_num_entity, email_team_entity, password_entity, confirm_password_entity;
    Spinner entity_type, the_field_entity, city_entity;


    DatabaseReference mdatabase;
    ProgressDialog mDialog;

    int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        //المتطوع
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        phone_num = (EditText) findViewById(R.id.phone_num);
        id_num = (EditText) findViewById(R.id.id_num);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        date_of_birth = (EditText) findViewById(R.id.date_of_birth);
        Voluntary_interests = (EditText) findViewById(R.id.Voluntary_interests);
        Educational_level = (EditText) findViewById(R.id.Educational_level);
        GetGender();
        getTime(date_of_birth);

        city_vol = (Spinner) findViewById(R.id.city_vol);
        GetCity(city_vol);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().child("Users");


        RelativeLayout r0 = (RelativeLayout) findViewById(R.id.r0);
        RelativeLayout r1 = (RelativeLayout) findViewById(R.id.r1);
        RelativeLayout r2 = (RelativeLayout) findViewById(R.id.r2);

        RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);
        RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
        RadioButton radio2 = (RadioButton) findViewById(R.id.radio2);
        radio0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r0.setVisibility(View.VISIBLE);
                    r1.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    type = 1;
                    selectedCity = 0;
                    selectedTheField = 0;
                    city.setSelection(0);
                    city_entity.setSelection(0);
                    the_field.setSelection(0);
                    the_field_entity.setSelection(0);
                }
            }
        });
        radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r1.setVisibility(View.VISIBLE);
                    r0.setVisibility(View.GONE);
                    r2.setVisibility(View.GONE);
                    type = 2;
                    selectedCity = 0;
                    selectedTheField = 0;
                    city.setSelection(0);
                    city_entity.setSelection(0);
                    the_field.setSelection(0);
                    the_field_entity.setSelection(0);
                }

            }
        });
        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    r2.setVisibility(View.VISIBLE);
                    r1.setVisibility(View.GONE);
                    r0.setVisibility(View.GONE);
                    type = 3;
                    selectedCity = 0;
                    selectedTheField = 0;
                    city.setSelection(0);
                    city_entity.setSelection(0);
                    the_field.setSelection(0);
                    the_field_entity.setSelection(0);
                }

            }
        });
        city = (Spinner) findViewById(R.id.city);
        GetCity(city);

        //الفريق
        name_team = (EditText) findViewById(R.id.name_team);
        leader_name = (EditText) findViewById(R.id.leader_name);
        phone_num_team = (EditText) findViewById(R.id.phone_num_team);
        id_num_leader = (EditText) findViewById(R.id.id_num_leader);
        email_team = (EditText) findViewById(R.id.email_team);
        password_team = (EditText) findViewById(R.id.password_team);
        confirm_password_team = (EditText) findViewById(R.id.confirm_password_team);
        social_media = (EditText) findViewById(R.id.social_media);
        the_field = (Spinner) findViewById(R.id.the_field);
        GetTheField(the_field);

        //الجهة
        name_entity = (EditText) findViewById(R.id.name_entity);
        phone_num_entity = (EditText) findViewById(R.id.phone_num_entity);
        id_num_entity = (EditText) findViewById(R.id.id_num_entity);
        email_team_entity = (EditText) findViewById(R.id.email_team_entity);
        password_entity = (EditText) findViewById(R.id.password_entity);
        confirm_password_entity = (EditText) findViewById(R.id.confirm_password_entity);
        the_field_entity = (Spinner) findViewById(R.id.the_field_entity);
        GetTheField(the_field_entity);
        GetEntityType();
        city_entity = (Spinner) findViewById(R.id.city_entity);
        GetCity(city_entity);

        findViewById(R.id.buttonRegister0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation1())
                    UserRegister(email.getText().toString(), password.getText().toString());
            }
        });
        findViewById(R.id.buttonRegister1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation2())
                    UserRegister(email_team.getText().toString(), password_team.getText().toString());
            }
        });
        findViewById(R.id.buttonRegister2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation3())
                    UserRegister(email_team_entity.getText().toString(), password_entity.getText().toString());
            }
        });
    }


    public boolean validation1() {
        if (TextUtils.isEmpty(first_name.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل الاسم الاول", Toast.LENGTH_SHORT).show();
            first_name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(last_name.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل الاسم الاخير", Toast.LENGTH_SHORT).show();
            last_name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone_num.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم الموبايل", Toast.LENGTH_SHORT).show();
            phone_num.requestFocus();
            return false;
        } else if (!isValidPhone(phone_num.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone_num.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(id_num.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم الهوية الوطنية", Toast.LENGTH_SHORT).show();
            id_num.requestFocus();
            return false;
        } else if (!isValidEmail(email.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل الايميل بشكل صحيح", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return false;
        } else if (!isValidPassword(password.getText().toString().trim())) { //password valid
            Toast.makeText(RegisterActivity.this, "يجب أن تكون كلمة المرور معقدة >> حرف علوي وسفلي ورقم (Aa123)", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirm_password.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل تأكيد كلمة المرور", Toast.LENGTH_SHORT).show();
            confirm_password.requestFocus();
            return false;
        } else if (!confirm_password.getText().toString().equals(password.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
            confirm_password.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(date_of_birth.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل تاريخ الميلاد", Toast.LENGTH_SHORT).show();
            date_of_birth.requestFocus();
            return false;
        } else if (selectedGender == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل الجنس", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(Voluntary_interests.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل الاهتمامات التطوعية", Toast.LENGTH_SHORT).show();
            Voluntary_interests.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(Educational_level.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل المستوى التعليمي", Toast.LENGTH_SHORT).show();
            Educational_level.requestFocus();
            return false;
        } else if (selectedCity == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public boolean validation2() {
        if (TextUtils.isEmpty(name_team.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل اسم الفريق", Toast.LENGTH_SHORT).show();
            name_team.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(leader_name.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل اسم القائد", Toast.LENGTH_SHORT).show();
            leader_name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone_num_team.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم الموبايل", Toast.LENGTH_SHORT).show();
            phone_num_team.requestFocus();
            return false;
        } else if (!isValidPhone(phone_num_team.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone_num_team.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(id_num_leader.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم الهوية الوطنية", Toast.LENGTH_SHORT).show();
            id_num_leader.requestFocus();
            return false;
        } else if (!isValidEmail(email_team.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل الايميل بشكل صحيح", Toast.LENGTH_SHORT).show();
            email_team.requestFocus();
            return false;
        } else if (!isValidPassword(password_team.getText().toString().trim())) { //password valid
            Toast.makeText(RegisterActivity.this, "يجب أن تكون كلمة المرور معقدة >> حرف علوي وسفلي ورقم (Aa123)", Toast.LENGTH_SHORT).show();
            password_team.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirm_password_team.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل تأكيد كلمة المرور", Toast.LENGTH_SHORT).show();
            confirm_password_team.requestFocus();
            return false;
        } else if (!confirm_password_team.getText().toString().equals(password_team.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
            confirm_password_team.requestFocus();
            return false;
        } else if (selectedTheField == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل المجال", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(social_media.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل اسم حساب التواصل الاجتماعي", Toast.LENGTH_SHORT).show();
            social_media.requestFocus();
            return false;
        } else if (selectedCity == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public boolean validation3() {
        if (TextUtils.isEmpty(name_entity.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل اسم الجهة", Toast.LENGTH_SHORT).show();
            name_entity.requestFocus();
            return false;
        } else if (selectedEntityType == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل نوع الجهة (حكومية ام خاصة) ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(phone_num_entity.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم الموبايل", Toast.LENGTH_SHORT).show();
            phone_num_entity.requestFocus();
            return false;
        } else if (!isValidPhone(phone_num_entity.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone_num_entity.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(id_num_entity.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل رقم الترخيص", Toast.LENGTH_SHORT).show();
            id_num_entity.requestFocus();
            return false;
        } else if (!isValidEmail(email_team_entity.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل الايميل بشكل صحيح", Toast.LENGTH_SHORT).show();
            email_team_entity.requestFocus();
            return false;
        } else if (!isValidPassword(password_entity.getText().toString().trim())) { //password valid
            Toast.makeText(RegisterActivity.this, "يجب أن تحتوي كلمة المرور على 8 حروف وارقام  >> مثال (Aa123)", Toast.LENGTH_SHORT).show();
            password_entity.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(confirm_password_entity.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "أدخل تأكيد كلمة المرور", Toast.LENGTH_SHORT).show();
            confirm_password_entity.requestFocus();
            return false;
        } else if (!confirm_password_entity.getText().toString().equals(password_entity.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
            confirm_password_entity.requestFocus();
            return false;
        } else if (selectedTheField == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل المجال", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedCity == 0) {
            Toast.makeText(RegisterActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
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

    private void UserRegister(String Email, String Password) {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("جاري إنشاء الحساب الرجاء الانتظار ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    mDialog.dismiss();
                    User user = null;
                    if (type == 1) {
                        user = new User(task.getResult().getUser().getUid(), first_name.getText().toString() + "",
                                last_name.getText().toString() + "", phone_num.getText().toString() + "",
                                id_num.getText().toString() + "", Email + "", date_of_birth.getText().toString() + "", Voluntary_interests.getText().toString() + "",
                                Educational_level.getText().toString() + "",
                                selectedNameGender, selectedGender,selectedNameCity, selectedCity,  1,true,0);
                    } else if (type == 2) {
                        user = new User(task.getResult().getUser().getUid(), name_team.getText().toString() + "",
                                leader_name.getText().toString() + "", phone_num_team.getText().toString() + "",
                                id_num_leader.getText().toString() + "", Email + "", social_media.getText().toString() + "",
                                selectedNameTheField, selectedTheField, selectedNameCity, selectedCity, 2,false,false);
                    } else if (type == 3) {
                        user = new User(task.getResult().getUser().getUid(), name_entity.getText().toString() + "",
                                phone_num_entity.getText().toString() + "",
                                id_num_entity.getText().toString() + "", Email + "", selectedNameEntityType, selectedEntityType,
                                selectedNameTheField, selectedTheField, selectedNameCity, selectedCity, 3,false);
                    }

                    mdatabase.child(task.getResult().getUser().getUid()).setValue(user);

                    Toast.makeText(RegisterActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, EmailConfirmActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "حدث خطأ في انشاء الحساب تأكد من الايميل المدخل وباقي البيانات", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getTime(final EditText editText) {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }


    private void sendEmailVerification() {//لارسال البريد الالكتروني الخاص بالتحقق
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "تحقق من بريدك الإلكتروني", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }
            });
        }
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

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
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

    int selectedGender = 0;
    String selectedNameGender;

    public void GetGender() {
        gender = (Spinner) findViewById(R.id.gender);

        List<dataModel> listModels = new ArrayList<>();
        listModels.add(new dataModel(0, "اختر الجنس"));
        listModels.add(new dataModel(1, "ذكر"));
        listModels.add(new dataModel(2, "أنثى"));

        final String[] name = new String[listModels.size()];
        final String[] idList = new String[listModels.size()];

        for (int i = 0; i < listModels.size(); i++) {
            name[i] = listModels.get(i).getName();
            idList[i] = listModels.get(i).getId() + "";
        }

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
        gender.setAdapter(adapterPiece);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                } catch (Exception ex) {
                }
                if (item != null) {
                    selectedGender = Integer.parseInt(idList[position]);
                    selectedNameGender = name[position];
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

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
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

    int selectedEntityType = 0;
    String selectedNameEntityType;

    public void GetEntityType() {
        entity_type = (Spinner) findViewById(R.id.entity_type);

        List<dataModel> listModels = new ArrayList<>();
        listModels.add(new dataModel(0, "حكومية ام خاصة"));
        listModels.add(new dataModel(1, "حكومية"));
        listModels.add(new dataModel(2, "خاصة"));

        final String[] name = new String[listModels.size()];
        final String[] idList = new String[listModels.size()];

        for (int i = 0; i < listModels.size(); i++) {
            name[i] = listModels.get(i).getName();
            idList[i] = listModels.get(i).getId() + "";
        }

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
        entity_type.setAdapter(adapterPiece);
        entity_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                } catch (Exception ex) {
                }
                if (item != null) {
                    selectedEntityType = Integer.parseInt(idList[position]);
                    selectedNameEntityType = name[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
