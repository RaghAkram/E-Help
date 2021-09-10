package com.e_help.Team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Adapter.OpportunitiesOfferdAdapter;
import com.e_help.Model.OpportunitiesModel;
import com.e_help.Opportunities.AddOpportunitiesActivity;
import com.e_help.R;
import com.e_help.Volunteer.Activity.VolunteerTeamsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OpportunitiesOfferedActivity extends AppCompatActivity {
    List<OpportunitiesModel> resultsList;
    OpportunitiesOfferdAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    DatabaseReference mdatabase;
    TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunities_offered);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);


        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("Opportunities");

        no_data = findViewById(R.id.no_data);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        nAdapter = new OpportunitiesOfferdAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);


        progress_bar.setVisibility(View.VISIBLE);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OpportunitiesModel opportunitiesModel = snapshot.getValue(OpportunitiesModel.class);

                    if (opportunitiesModel.isActive() && opportunitiesModel.getKey_user_added().equals(preferences.getString("Uid", ""))) {
                        resultsList.add(opportunitiesModel);
                        nAdapter.notifyDataSetChanged();
                    }
                }
                if (resultsList.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress_bar.setVisibility(View.GONE);
                resultsList.clear();
                nAdapter.notifyDataSetChanged();
                recyclerView.removeAllViews();
                if (resultsList.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);

                }
                Toast.makeText(OpportunitiesOfferedActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}