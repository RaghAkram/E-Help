package com.e_help.Volunteer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.e_help.Model.GpsUtils;
import com.e_help.Model.HelpModel;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class
AddImmediateHelpActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText name, phone, city, details;
    EditText locationET, photoET;
    String link = "";
    RadioButton r0, r1, r2, r3;
    String type = "تبرع بالدم";
    RadioButton time_6h, time_12h, time_24h;
    int timeH = 12;
    ProgressDialog dialogM;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    EditText more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_immediate_help);
        dialogM = new ProgressDialog(this);
        dialogM.setMessage("جاري الحفظ يرجى الانتظار ...");
        dialogM.setIndeterminate(true);

        firebaseStorage = FirebaseStorage.getInstance();
        mainRef = firebaseStorage.getReference();
        userImagesRef = mainRef.child("images");
        database = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().child("ImmediateHelp");
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        city = (EditText) findViewById(R.id.city);
        details = (EditText) findViewById(R.id.details);

        r0 = (RadioButton) findViewById(R.id.r0);
        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        r3 = (RadioButton) findViewById(R.id.r3);
        more = (EditText) findViewById(R.id.more);
        r0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "تبرع بالدم";
                    more.setVisibility(View.GONE);
                }
            }
        });
        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "بحث عن مفقود";
                    more.setVisibility(View.GONE);
                }
            }
        });
        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "عناية بنبتة";
                    more.setVisibility(View.GONE);
                }
            }
        });
        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "أخرى";
                    more.setVisibility(View.VISIBLE);
                }
            }
        });
        time_6h = (RadioButton) findViewById(R.id.time_6h);
        time_12h = (RadioButton) findViewById(R.id.time_12h);
        time_24h = (RadioButton) findViewById(R.id.time_24h);

        time_6h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    timeH = 6;
            }
        });
        time_12h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    timeH = 12;
            }
        });
        time_24h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    timeH = 24;
            }
        });

        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        locationET = (EditText) findViewById(R.id.location);
        photoET = (EditText) findViewById(R.id.photo);
        showDialog();
        locationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        photoET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

            findViewById(R.id.buttonPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    dialogM.show();//يعرضلنا مسج جاري الحفظ
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//يتأكد انو نفس اليوزر الي دخل عشان يجيب معلوماته
                    String key = database.push().getKey();//ينشأ keys ويضيفها في الداتابيس

                    if (type.equals("أخرى")) {
                        type = more.getText().toString();//عبارة تيكست
                    }
                    //انشأنا ريكورد في المودل وحفظناها في الداتابيس
                    HelpModel helpModel = new HelpModel(key, name.getText().toString() + "",
                            phone.getText().toString() + "", type + "",
                            locationET.getText().toString() + "", city.getText().toString() + "",
                            link + "", details.getText().toString() + "",
                            timeH + "", System.currentTimeMillis() + (timeH * 60 * 60 * 1000), user.getUid(), true);
                    //اخدنا الوقت الحالي بالميلي سكند + وضفنا عليه الوقت المتاح وحولناه لميلي سكند= الوقت الي نخلص فيه بالميلي سكند
                    //حسبنا الوقت بالمللي سكند ومرا دقيقة

                    database.child(key).setValue(helpModel).addOnCompleteListener(new OnCompleteListener<Void>() {//الكي اللي انشاناها خليها للمودل
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialogM.dismiss();
                            Toast.makeText(AddImmediateHelpActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                            finish();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogM.dismiss();
                            Toast.makeText(AddImmediateHelpActivity.this, "حدث خطأ " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    public static Dialog dialog;
    public static Double lat = 0.0, lng = 0.0;

    public void showDialog() {
        dialog = new Dialog(AddImmediateHelpActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_dialog_add_location);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(AddImmediateHelpActivity.this);

        ImageView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        (dialog.findViewById(R.id.send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat == 0.0) {
                    Toast.makeText(AddImmediateHelpActivity.this, "الرجاء اختيار الموقع", Toast.LENGTH_SHORT).show();
                } else {
                    locationET.setText(lat + "," + lng);

                    dialog.dismiss();

                }
            }
        });


    }

    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;

    LocationRequest mLocationRequest;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap = googleMap;

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.6839312, 47.1559343), 7.0f));
            new GpsUtils(AddImmediateHelpActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                }
            });
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Request Location Permission
                    checkLocationPermission();

                }
            } else {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));//.icon(icon));
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    //      mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
                }
            });
        } catch (Exception ex) {
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddImmediateHelpActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
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

    private static final int PICK_IMG_REQUEST = 7588;
    FirebaseStorage firebaseStorage;
    StorageReference mainRef, userImagesRef;
    DatabaseReference database;

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        try {

            if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                upload(imageUri);
            } else {
                Toast.makeText(this, "لم يتم اختيار صورة :/", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
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

                            link = String.valueOf(downloadUrl);
                            String path = task.getResult().getStorage().getPath();

                            photoET.setText("" + task.getResult().getStorage().getName());
                            Toast.makeText(AddImmediateHelpActivity.this, "تم رفع الصورة بنجاح", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddImmediateHelpActivity.this, "Upload Failed :( " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean validation() {
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            Toast.makeText(AddImmediateHelpActivity.this, "أدخل الاسم", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            Toast.makeText(AddImmediateHelpActivity.this, "أدخل رقم موبايل", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return false;
        } else if (!isValidPhone(phone.getText().toString().trim())) {
            Toast.makeText(AddImmediateHelpActivity.this, "أدخل رقم موبايل صالح", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(locationET.getText().toString().trim())) {
            Toast.makeText(AddImmediateHelpActivity.this, "أدخل الموقع", Toast.LENGTH_SHORT).show();
            locationET.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(city.getText().toString().trim())) {
            Toast.makeText(AddImmediateHelpActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
            city.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(details.getText().toString().trim())) {
            Toast.makeText(AddImmediateHelpActivity.this, "أدخل الوصف", Toast.LENGTH_SHORT).show();
            details.requestFocus();
            return false;
        }

        if (type.equals("أخرى")) {
            if (TextUtils.isEmpty(more.getText().toString().trim())) {
                Toast.makeText(AddImmediateHelpActivity.this, "أدخل الحقل أخرى", Toast.LENGTH_SHORT).show();
                more.requestFocus();
                return false;
            }
        }
        return true;
    }


    public static boolean isValidPhone(String s) {
        Pattern Phone = Pattern.compile(
                "^((?:[+?0?0?966]+)(?:\\s?\\d{2})(?:\\s?\\d{7}))$");
        return !TextUtils.isEmpty(s) && Phone.matcher(s).matches();
    }
}