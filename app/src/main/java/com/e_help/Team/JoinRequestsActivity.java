package com.e_help.Team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Adapter.JoinRequestsMembersAdapter;
import com.e_help.Model.RequestModel;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinRequestsActivity extends AppCompatActivity {
    List<RequestModel> resultsList;
    JoinRequestsMembersAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    TextView no_data;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requests);


        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");

        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Join_requests").child(Uid);

        resultsList = new ArrayList<>();

        no_data = findViewById(R.id.no_data);
        recyclerView = findViewById(R.id.recycler);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new JoinRequestsMembersAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);

        progress_bar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestModel memberModel = snapshot.getValue(RequestModel.class);
                    resultsList.add(memberModel);
                    nAdapter.notifyDataSetChanged();
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
                Toast.makeText(JoinRequestsActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}