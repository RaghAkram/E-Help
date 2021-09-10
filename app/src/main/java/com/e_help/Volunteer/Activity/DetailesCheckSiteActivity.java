package com.e_help.Volunteer.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.e_help.Model.GpsUtils;
import com.e_help.Model.OpportunitieRegisterModel;
import com.e_help.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DetailesCheckSiteActivity extends AppCompatActivity implements OnMapReadyCallback {

    ProgressDialog dialog2, dialog1;
    String ID, Uid,IdTeam;
    DatabaseReference mdatabase;
    OpportunitieRegisterModel opportunitiesModel;
    CardView gps_off;
    RelativeLayout gps_online;
    boolean check_is_one = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_site);
        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        gps_off = (CardView) findViewById(R.id.gps_off);
        gps_online = (RelativeLayout) findViewById(R.id.gps_online);
        findViewById(R.id.turn_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermission();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailesCheckSiteActivity.this);

        dialog2 = new ProgressDialog(DetailesCheckSiteActivity.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("الرجاء الانتظار ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();

        dialog1 = new ProgressDialog(DetailesCheckSiteActivity.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage("جاري مقارنة موقع المتطوع مع الموقع المدرج في الفرصة");
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IdTeam= intent.getStringExtra("IdTeam");
        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                child("OpportunitiesRegister").child(IdTeam).child(ID).child(Uid);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog2.dismiss();
                try {
                    opportunitiesModel = dataSnapshot.getValue(OpportunitieRegisterModel.class);

                    if (opportunitiesModel.isActual()) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(opportunitiesModel.getLat(),
                                opportunitiesModel.getLng())));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(opportunitiesModel.getLat(),
                                opportunitiesModel.getLng()), 14.0f));//هنا عشان اول ما يفتح الماب حيجبلي السعودية

                    }

                    if (check_is_one) {//اذا عمل التشيك ان ورجع فتح التشيك ان مرا ثانية
                        if (opportunitiesModel.isConfirmAttendance()) {//حاقله انتا اتحققت وخلاص
                            Toast.makeText(DetailesCheckSiteActivity.this, "تم التحقق", Toast.LENGTH_SHORT).show();
                            finish();//ونقفل الشاشة
                        }
                    }

                } catch (Exception e) {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog2.dismiss();
                Toast.makeText(DetailesCheckSiteActivity.this, "حدث خطأ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //لتحديد الموقع على الخريطة
        try {
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.6839312, 47.1559343), 7.0f));
            new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    isGPS = isGPSEnable;

                    gps_off.setVisibility(View.GONE);
                    gps_online.setVisibility(View.VISIBLE);


                }
            });
            if (!isGPS) {
                gps_off.setVisibility(View.VISIBLE);
                gps_online.setVisibility(View.GONE);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    dialog1.show();
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Request Location Permission
                    checkLocationPermission();

                }
            } else {

                mMap.setMyLocationEnabled(true);
            }

        } catch (Exception ex) {
        }
    }
    //حيتنفذ بعد مااعطيه صلاحية الوصول للموقع + GPS شغال
    //طول مايكون في تغيير على الماب بيرجعلي المواقع كلها
    //لو اتنفذ حيرجعلي list location
    Location mLastLocation;
    Marker mCurrLocationMarker;//يكون على الماب
    FusedLocationProviderClient mFusedLocationClient;//
    LocationRequest mLocationRequest;
    boolean isSuccess = true;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();//حتنحفضظ الداتا هنا- ترجع الموقع تبع المستخدم
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                gps_off.setVisibility(View.GONE);
                gps_online.setVisibility(View.VISIBLE);
                Location location = locationList.get(locationList.size() - 1);//هنا عشان نجيب آخر لوكيشن لليوزر
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());//بيطبع في خطوط الطول والعرض
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                Location loc2 = new Location("");//أنشانامتغير من نوع لوكيشن
                loc2.setLatitude(opportunitiesModel.getLat());//وضفنا فيه لوكيشن الفرصة من خلال lat - lng
                loc2.setLongitude(opportunitiesModel.getLng());
                float distanceInMeters = location.distanceTo(loc2);// عشان  ميثود distanceTo تاخد متغيرات من نوع لوكيشن - بيقارن المسافة
                //location = للمتطوع
                float KMTrue = 1 * 1000;//المسافة المطلوبة -
                //
                if (distanceInMeters <= KMTrue) {//لو اتحقق الشرط حنروح نعديل على الكونفيرم اتندس = true
                    mdatabase.child("confirmAttendance").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {


                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog1.dismiss();
                            if (isSuccess) {
                                //لان الكود زي الفور لوب كل مااتغير موقعه ح يخرنه ف حطينا دا الشرط
                                //لو مافي الشرط ح تطلعلي اليرت ريالوق كل شويا
                                AlertDialog alertDialog = new AlertDialog.Builder(DetailesCheckSiteActivity.this).create();
                                alertDialog.setTitle("تم");
                                alertDialog.setMessage("تم الوصول بنجاح");
                                alertDialog.setIcon(getApplicationContext().getDrawable(R.drawable.check_mark));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "تم",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                alertDialog.show();
                            }
                            isSuccess = false;//هنا ح يخليه فولز عشان ينهيه - وقف هنا في الكود لا يعدل على الباقي
                            check_is_one=false;//لو فتح الكود مرا تانية حاقله انو تم التحقق خلاص
                        }
                    });
                } else {
                    dialog1.dismiss();
                    Toast.makeText(DetailesCheckSiteActivity.this, "المكان غير متطابق مع الفرصة", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private boolean isGPS = false;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            gps_off.setVisibility(View.VISIBLE);
            gps_online.setVisibility(View.GONE);
        } else {
            new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    isGPS = isGPSEnable;

                    gps_off.setVisibility(View.GONE);
                    gps_online.setVisibility(View.VISIBLE);
                }
            });
            if (!isGPS) {
                gps_off.setVisibility(View.VISIBLE);
                gps_online.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                        dialog1.show();
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

}