package com.e_help.Team;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e_help.Model.MemberModel;
import com.e_help.Model.TaskModel;
import com.e_help.Model.User;
import com.e_help.Model.dataModel2;
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

public class AddTaskActivity extends AppCompatActivity {
    //فقط عرفناهم .. عشان اقدر اوصلهم
    EditText title, description, date;
    DatabaseReference mdatabaseTeamMembers,referenceTask;
    //عندنا2 داتابيس .. عشان بنعرض بيانات من واحد وبنخزن في التاني
    //نعرض التيم ميمبرز للفريق
    //عشان نخزن التاسك
    String Uid;
    ProgressDialog dialogM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE); // لجلب الداتا المخزنة
        Uid = preferences.getString("Uid", "");// جيب الداتا المخزنة"id لقائد الفريق" وبناءً عليه ح يجبلي داتا الفريق فقط
        dialogM = new ProgressDialog(this);
        dialogM.setMessage("جاري الحفظ يرجى الانتظار ...");
        dialogM.setIndeterminate(true);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        date = (EditText) findViewById(R.id.date);
        getTime(date);
        mdatabaseTeamMembers = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("TeamMembers").child(Uid); // تعريف الجدول
        referenceTask = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Task").child(Uid);// تعريف الجدول - خزنا كل المهام داخل Uid حقت الفريق
        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) { //تتاكد انو الحقول معبئة بشكل صحيح
                    dialogM.show();//جاري الحفط

                    String key = referenceTask.push().getKey();//جنريت key للتاسك - ضيف العنصر واعطيني key تبعه بس لسا مجلد فاضي بس نسميه
                    //لو ما حطينا هنا getKey- حيكتب على نفس الكي كل شويا
                     //نعبي المودل باننا ننشأ ريكورد جديد
                    //Uid للفريق
                    TaskModel taskModel = new TaskModel(key, Uid, title.getText().toString(),
                            description.getText().toString(), date.getText().toString(), selectedNameUser, selectedUser);//selectedUser=id
                    //او تكون معينه للجميع ويكون id=0
                    referenceTask.child(key).setValue(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() { //هنا نعطيه key ونعبي المجلد
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            dialogM.dismiss();
                            Toast.makeText(AddTaskActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();

                            title.setText("");
                            description.setText("");
                            date.setText("");

                        }
                    });
                }
            }
        });
        GetUser();

    }

    public boolean validation() {
        if (TextUtils.isEmpty(title.getText().toString().trim())) {
            Toast.makeText(AddTaskActivity.this, "أدخل العنوان", Toast.LENGTH_SHORT).show();
            title.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(description.getText().toString().trim())) {
            Toast.makeText(AddTaskActivity.this, "أدخل الوصف", Toast.LENGTH_SHORT).show();
            description.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(date.getText().toString().trim())) {
            Toast.makeText(AddTaskActivity.this, "أدخل موعد الانتهاء", Toast.LENGTH_SHORT).show();
            date.requestFocus();
            return false;
        }
        return true;
    }

    String selectedUser = "0";
    String selectedNameUser = "";

    public void GetUser() {
        //ربط السبينر من xml الى الجافا
        Spinner userSp = (Spinner) findViewById(R.id.sp_user);
        //عملنا list وهي القائمة التي تحتوي على العناصر اللي حنعرضها في السبينر من نوع array
        //نعبي فيها اعضاء الفريق
        List<dataModel2> listModels = new ArrayList<>();

        // (1)
        listModels.add(new dataModel2("0", "تعيين الجميع")); // اول عنصر ضفناه - تعيين الجميع واعطيناها id=0
        //بنروح نجيب كل اعضاء الفريق من الداتا بيس ونخزنها في listModels
        mdatabaseTeamMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { // لو الداتا اللي نبغاها موجودة "يكون في اعضاء للفريق"
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // ناخد سناب شوت من الداتا ونقسمها على شكل مودل كالعادة
                        MemberModel user = snapshot.getValue(MemberModel.class);
                        if (user.isAccepted())//لو كان اليوزر مقبول في الفريق
                            listModels.add(new dataModel2(user.getId_user(), user.getFname_user() + " " + user.getLname_user()));
                        //بنخزن اليوزر في listModels بس بنعطيها شكل معين عشان يعرضها السبينر
                        // لاننا فقط نبغى id و name = لان السبينر تاخد String فقط
                    }
                }

                // انشأنا 2 array واعطيناها نفس الحجم لـlist model
                //عشان نبغى نقسم البيانات الموجودة في list model
                //array= id و array=name
                //لان السبينر بياخد فقط array من نوع String يعني فقط ح نحتاج array for name
                final String[] name = new String[listModels.size()];
                final String[] idList = new String[listModels.size()];
                //جبنا كل الداتا اللي في listModel وعبيناهم في array
                for (int i = 0; i < listModels.size(); i++) {
                    name[i] = listModels.get(i).getName();
                    idList[i] = listModels.get(i).getId() + "";
                }
                // الآن يجب علينا أن نقوم بإنشاء Adapter بسيط (زي ما نسوي في RecyclerView) ليقوم بعرض هذه العناصر في Spinner
                // وهو عبارة عن ArrayAdapter يقوم بأخذ :
                // 1- Context
                // شكل العنصر الذي سيتم عرضه (ملف xml)
                //وأخيراً الليست او Array - وهنا array=name لانها سترينق
                ArrayAdapter adapterPiece = new ArrayAdapter<String>(AddTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, name);

                //(2)
                userSp.setAdapter(adapterPiece);//كدا ربطنا الادابتر مع السبينر
                userSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override// لمن اختار شخص محدد حيتفعل الاكشن ليسنر onItemSelected
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);//حيروح يجيب بيانات الاوبجكت لليورز اللي ضغطنا عليه ويربطه مع position
                        try {
                            ((TextView) view).setTextColor(Color.BLACK); //غيرنا الخط  لاسود لان التكست كان بيطلع معانا ابيض
                        } catch (Exception ex) {
                        }
                        if (item != null) {
                            //متغيرين String
                            //فقط عشان اجيب اسمائهم و id
                            selectedUser = idList[position];//نخزن فيه id list = array للعنصر اللي اخترناه
                            selectedNameUser = name[position];//نخزن فيه name list = array للعنصر اللي اخترناه
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddTaskActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
    public void getTime(final EditText editText) {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                date.set(year, monthOfYear, dayOfMonth);
                String myFormat = "dd-MM-yyyy";// HH:mm";
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
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

    }

}