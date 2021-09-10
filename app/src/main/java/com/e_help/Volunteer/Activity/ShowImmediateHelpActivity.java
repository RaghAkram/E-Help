package com.e_help.Volunteer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.e_help.Adapter.ImmediateHelpAdapter;
import com.e_help.Model.HelpModel;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowImmediateHelpActivity extends AppCompatActivity {
    List<HelpModel> resultsList;
    ImmediateHelpAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    DatabaseReference mdatabase;

    TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_immediate_help);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("ImmediateHelp");

        no_data = findViewById(R.id.no_data);
        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new ImmediateHelpAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);

        progress_bar.setVisibility(View.VISIBLE);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HelpModel helpModel = snapshot.getValue(HelpModel.class);

                    if (new Date().getTime() < helpModel.getEndAt() && helpModel.isActive()) {
                        resultsList.add(helpModel);
                        nAdapter.notifyDataSetChanged();
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
                progress_bar.setVisibility(View.GONE);
                resultsList.clear();
                nAdapter.notifyDataSetChanged();
                recyclerView.removeAllViews();

                if (resultsList.size() == 0) {
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    no_data.setVisibility(View.GONE);

                }
                Toast.makeText(ShowImmediateHelpActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}