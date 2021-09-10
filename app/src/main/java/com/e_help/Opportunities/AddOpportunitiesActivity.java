package com.e_help.Opportunities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.e_help.Model.GpsUtils;
import com.e_help.Model.OpportunitiesModel;
import com.e_help.Model.dataModel;
import com.e_help.R;
import com.e_help.User.RegisterActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddOpportunitiesActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText name, description,name_organization,skills,num_hour,num_valunteer,gender;
    Spinner the_field_entity, city;
    EditText start_date, end_date;

    RadioButton actual, virtual, yes, no;

    boolean actualB=true,degree=true;
    ProgressDialog dialogM;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_opportunities);
        dialogM = new ProgressDialog(this);
        dialogM.setMessage("جاري الحفظ يرجى الانتظار ...");
        dialogM.setIndeterminate(true);

        database = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference().
                child("Opportunities");
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        name_organization = (EditText) findViewById(R.id.name_organization);
        skills = (EditText) findViewById(R.id.skills);
        num_hour = (EditText) findViewById(R.id.num_hour);
        num_valunteer = (EditText) findViewById(R.id.num_valunteer);
        gender = (EditText) findViewById(R.id.gender);
        city = (Spinner) findViewById(R.id.city);
        GetCity(city);
        start_date = (EditText) findViewById(R.id.start_date);
        end_date = (EditText) findViewById(R.id.end_date);
        the_field_entity = (Spinner) findViewById(R.id.the_field_entity);
        GetTheField(the_field_entity);
        getTime(start_date);
        getTime(end_date);
        actual = (RadioButton) findViewById(R.id.actual);
        virtual = (RadioButton) findViewById(R.id.virtual);
        yes = (RadioButton) findViewById(R.id.yes);
        no= (RadioButton) findViewById(R.id.no);

        actual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actualB=true;
                    selectedNameCity="";
                    city.setVisibility(View.VISIBLE);
                }
            }
        });
        virtual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actualB=false;
                    selectedNameCity="افتراضية";
                    city.setVisibility(View.GONE);
                }
            }
        });
        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    degree=true;
                }
            }
        });
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    degree=false;
                }
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(AddOpportunitiesActivity.this);


        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.buttonPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    dialogM.show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String key = database.push().getKey();

                    OpportunitiesModel opportunitiesModel=new OpportunitiesModel(key,name.getText().toString(),
                            description.getText().toString(), selectedNameTheField,name_organization.getText().toString(),
                            skills.getText().toString(), num_hour.getText().toString(), num_valunteer.getText().toString(),
                            gender.getText().toString(), selectedNameCity, start_date.getText().toString(),
                            end_date.getText().toString(), actualB, degree, lat,  lng, user.getUid(), false) ;

                        database.child(key).setValue(opportunitiesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialogM.dismiss();
                            Toast.makeText(AddOpportunitiesActivity.this, "تمت الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogM.dismiss();
                            Toast.makeText(AddOpportunitiesActivity.this, "حدث خطأ " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    public static Double lat = 0.0, lng = 0.0;
    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;

    LocationRequest mLocationRequest;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.6839312, 47.1559343), 7.0f));
            new GpsUtils(AddOpportunitiesActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                                ActivityCompat.requestPermissions(AddOpportunitiesActivity.this,
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

    public boolean validation() {
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل الاسم", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(description.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل الوصف", Toast.LENGTH_SHORT).show();
            description.requestFocus();
            return false;
        }else if (selectedTheField==0) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل المجال", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(name_organization.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "الرجاء ادخال اسم المنظمة", Toast.LENGTH_SHORT).show();
            name_organization.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(skills.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل المهارات", Toast.LENGTH_SHORT).show();
            skills.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(num_hour.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل عدد الساعات", Toast.LENGTH_SHORT).show();
            num_hour.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(num_valunteer.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل عدد المتطوعين", Toast.LENGTH_SHORT).show();
            num_valunteer.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(gender.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل الجنس", Toast.LENGTH_SHORT).show();
            gender.requestFocus();
            return false;
        }else if (selectedCity==0) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل المدينة", Toast.LENGTH_SHORT).show();
            city.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(start_date.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل تاريخ البدء", Toast.LENGTH_SHORT).show();
            start_date.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(end_date.getText().toString().trim())) {
            Toast.makeText(AddOpportunitiesActivity.this, "أدخل تاريخ الانتهاء", Toast.LENGTH_SHORT).show();
            end_date.requestFocus();
            return false;
        }else if (lat == 0.0) {
            Toast.makeText(AddOpportunitiesActivity.this, "الرجاء اختيار الموقع", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(AddOpportunitiesActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
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


    public void getTime(final EditText editText) {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(AddOpportunitiesActivity.this, new DatePickerDialog.OnDateSetListener() {

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
        datePickerDialog.getDatePicker();

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

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(AddOpportunitiesActivity.this, android.R.layout.simple_spinner_dropdown_item, name);
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