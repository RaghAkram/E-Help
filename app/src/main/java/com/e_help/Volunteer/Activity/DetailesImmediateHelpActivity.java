package com.e_help.Volunteer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Adapter.ImmediateHelpAdapter;
import com.e_help.Adapter.LatestUpdateAdapter;
import com.e_help.Model.HelpModel;
import com.e_help.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DetailesImmediateHelpActivity extends AppCompatActivity {
    TextView title_type, name, phone, city, details, time_end;

    EditText title_new;
    RelativeLayout LAdd;
    RecyclerView recycler;
    Button end_order;
    ImageView camera;
    ProgressDialog dialog2;
    String ID;
    DatabaseReference mdatabase;
    ImageView img;
    HelpModel helpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes_immediate_help);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog2 = new ProgressDialog(DetailesImmediateHelpActivity.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("الرجاء الانتظار ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();
        Intent intent = getIntent();

        ID = intent.getStringExtra("ID");

        img = (ImageView) findViewById(R.id.img);
        title_type = (TextView) findViewById(R.id.title_type);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        city = (TextView) findViewById(R.id.city);
        details = (TextView) findViewById(R.id.details);
        time_end = (TextView) findViewById(R.id.time_end);
        recycler = (RecyclerView) findViewById(R.id.recycler);


        LAdd = (RelativeLayout) findViewById(R.id.LAdd);
        title_new = (EditText) findViewById(R.id.title_new);
        camera = (ImageView) findViewById(R.id.camera);
        end_order = (Button) findViewById(R.id.end_order);

        userImagesRef = FirebaseStorage.getInstance().getReference().child("images");

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                child("ImmediateHelp").child(ID);
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        get();
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog2.dismiss();

                try {
                    helpModel = dataSnapshot.getValue(HelpModel.class);
                    title_type.setText("" + helpModel.getType());
                    name.setText("" + helpModel.getName());
                    phone.setText("" + helpModel.getNumber());
                    city.setText("" + helpModel.getCity());
                    details.setText("" + helpModel.getDetails());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm aa");
                    Date date = new Date();
                    date.setTime(helpModel.getEndAt());

                    time_end.setText("" + sdf.format(date));

                    if (users.getUid().equals(helpModel.getKey_user_added())) {
                        LAdd.setVisibility(View.VISIBLE);
                        end_order.setVisibility(View.VISIBLE);
                    }
                    Picasso.get().load(helpModel.getImg_url()).into(img);

                } catch (Exception e) {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog2.dismiss();
                Toast.makeText(DetailesImmediateHelpActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

            findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title_new.getText().toString().equals("")) {//فالديشن لو ضغط زر send والتكست بوكس فاضي
                    dialog2.show();
                    String key = mdatabase.child("LatestUpdates").push().getKey();
                   //لمن يضيف تحديث بيروح للهيلب مودل بييضيف داخله مودل لتحديثات
                    HelpModel.LatestUpdatesModel latestUpdatesModel =
                            new HelpModel.LatestUpdatesModel(key, title_new.getText().toString(), Link_img);
                    mdatabase.child("LatestUpdates").child(key).setValue(latestUpdatesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog2.dismiss();
                            title_new.setText("");//نفضي الحقول
                            Link_img = "";
                            Toast.makeText(DetailesImmediateHelpActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailesImmediateHelpActivity.this, "حدث خطأ " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog2.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(DetailesImmediateHelpActivity.this, "يجب ادخال نص", Toast.LENGTH_SHORT).show();
                    title_new.requestFocus();
                }

            }
        });
        end_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailesImmediateHelpActivity.this)
                        .setTitle("الغاء الطلب")
                        .setMessage("هل انت متأكد من الغاء الطلب ؟")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mdatabase.child("Active").setValue(false);
                                finish();
                            }
                        })
                        .setNegativeButton("لا", null).show();
            }
        });
    }

    String Link_img = "";

    private static final int PICK_IMG_REQUEST = 7588;
    StorageReference userImagesRef;

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            upload(imageUri);
        } else {
            Toast.makeText(this, "لم يتم اختيار صورة :/", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        final String imageName = UUID.randomUUID().toString() + ".jpg";


        userImagesRef.child(imageName).putFile(uri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(progress + "");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            Link_img = String.valueOf(downloadUrl);

                            Toast.makeText(DetailesImmediateHelpActivity.this, "تم رفع الصورة بنجاح", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailesImmediateHelpActivity.this, "Upload Failed :( " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    List<HelpModel.LatestUpdatesModel> resultsList;
    LatestUpdateAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;

    public void get() {
        DatabaseReference mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("ImmediateHelp").child(ID).child("LatestUpdates");

        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new LatestUpdateAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);

        progress_bar.setVisibility(View.VISIBLE);


        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HelpModel.LatestUpdatesModel latestUpdatesModel = snapshot.getValue(HelpModel.LatestUpdatesModel.class);

                    resultsList.add(latestUpdatesModel);
                    nAdapter.notifyDataSetChanged();

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress_bar.setVisibility(View.GONE);

                Toast.makeText(DetailesImmediateHelpActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}