package com.e_help.Volunteer.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Adapter.TeamVolunteerAdapter;
import com.e_help.Model.User;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VolunteerTeamsActivity extends AppCompatActivity {
    List<User> resultsList;
    TeamVolunteerAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    TextView no_data;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_teams);

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");

        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resultsList = new ArrayList<>();

        no_data = findViewById(R.id.no_data);
        recyclerView = findViewById(R.id.recycler);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new TeamVolunteerAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);

        progress_bar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progress_bar.setVisibility(View.GONE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getUser_type() == 2) {
                            if (user.isIs_ads()) {
                                resultsList.add(user);
                                nAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (resultsList.size() == 0) {
                        no_data.setVisibility(View.VISIBLE);
                    } else {
                        no_data.setVisibility(View.GONE);
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VolunteerTeamsActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progress_bar.setVisibility(View.GONE);
                resultsList.clear();
                nAdapter.notifyDataSetChanged();
                recyclerView.removeAllViews();

                if (resultsList.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);

                }
                finish();
            }
        });


    }
}