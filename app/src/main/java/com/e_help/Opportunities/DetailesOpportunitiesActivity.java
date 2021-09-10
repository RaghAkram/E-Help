package com.e_help.Opportunities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.e_help.Model.OpportunitieRegisterModel;
import com.e_help.Model.OpportunitiesModel;
import com.e_help.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailesOpportunitiesActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView name, description, name_organization, start_date, end_date, num_hour,
            city, type, entity_type, skills, gender, num_valunteer, degree;

    ProgressDialog dialog2;
    String ID, Uid;
    DatabaseReference mdatabase, mdatabaseOpportunitiesRegister;

    OpportunitiesModel opportunitiesModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes_opportunities);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailesOpportunitiesActivity.this);

        dialog2 = new ProgressDialog(DetailesOpportunitiesActivity.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("الرجاء الانتظار ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");

        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        name_organization = (TextView) findViewById(R.id.name_organization);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        num_hour = (TextView) findViewById(R.id.num_hour);
        city = (TextView) findViewById(R.id.city);
        type = (TextView) findViewById(R.id.type);
        entity_type = (TextView) findViewById(R.id.entity_type);
        skills = (TextView) findViewById(R.id.skills);
        gender = (TextView) findViewById(R.id.gender);
        num_valunteer = (TextView) findViewById(R.id.num_valunteer);
        degree = (TextView) findViewById(R.id.degree);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                child("Opportunities").child(ID);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog2.dismiss();

                try {
                    opportunitiesModel = dataSnapshot.getValue(OpportunitiesModel.class);
                    name.setText("" + opportunitiesModel.getName());
                    description.setText("" + opportunitiesModel.getDescription());
                    name_organization.setText("" + opportunitiesModel.getName_organization());
                    start_date.setText("" + opportunitiesModel.getTime_start());
                    end_date.setText("" + opportunitiesModel.getTime_end());
                    num_hour.setText("" + opportunitiesModel.getNum_hour());
                    city.setText("" + opportunitiesModel.getCity());
                    if (opportunitiesModel.isActual()) {
                        type.setText("حضورية");
                        findViewById(R.id.actual_l).setVisibility(View.VISIBLE);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(opportunitiesModel.getLat(),
                                opportunitiesModel.getLng())));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(opportunitiesModel.getLat(),
                                opportunitiesModel.getLng()), 14.0f));
                    } else {
                        type.setText("افتراضية");
                        findViewById(R.id.actual_l).setVisibility(View.GONE);

                    }
                    entity_type.setText("" + opportunitiesModel.getType());
                    skills.setText("" + opportunitiesModel.getSkills());
                    gender.setText("" + opportunitiesModel.getGender());
                    num_valunteer.setText("" + opportunitiesModel.getNum_valunteer());
                    if (opportunitiesModel.isDegree()) {
                        degree.setText("نعم");
                    } else {
                        degree.setText("لا");
                    }



                } catch (Exception e) {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog2.dismiss();
                Toast.makeText(DetailesOpportunitiesActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");


        int Type = preferences.getInt("UserType", 0);
        if (Type == 1) {
            findViewById(R.id.register_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.register_btn).setVisibility(View.GONE);
        }
        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
                mdatabaseOpportunitiesRegister = FirebaseDatabase.
                        getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                        child("OpportunitiesRegister");

                                FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                        OpportunitieRegisterModel model = new OpportunitieRegisterModel(opportunitiesModel.getId(), opportunitiesModel.getName() + "",
                                opportunitiesModel.getName_organization(), preferences.getString("nameVal", "") + "",
                                opportunitiesModel.getKey_user_added(), Uid + "", opportunitiesModel.isActual(), opportunitiesModel.getLat(), opportunitiesModel.getLng(),
                                opportunitiesModel.getCity(), false, false,Integer.parseInt(opportunitiesModel.getNum_hour()));

                        mdatabaseOpportunitiesRegister.child(opportunitiesModel.getKey_user_added()).child(opportunitiesModel.getId()).child(users.getUid()).
                                setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog2.dismiss();
                                Toast.makeText(DetailesOpportunitiesActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailesOpportunitiesActivity.this, "حدث خطأ " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();
                    }
                });


            }
        });

    }

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

}