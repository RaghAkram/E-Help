package com.e_help.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Adapter.OpportunitiesAcceptAdapter;
import com.e_help.Model.OpportunitiesModel;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OpportunitiesAcceptActivity extends AppCompatActivity {
    List<OpportunitiesModel> resultsList;
    OpportunitiesAcceptAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_opportunities);


        findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Opportunities");


        no_data = findViewById(R.id.no_data);
        recyclerView = findViewById(R.id.recycler);
        resultsList = new ArrayList<>();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        nAdapter = new OpportunitiesAcceptAdapter(this, resultsList);
        recyclerView.setAdapter(nAdapter);// ربطنا الادابتر مع الريسايكلر فيو

        progress_bar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OpportunitiesModel opportunitiesModel = snapshot.getValue(OpportunitiesModel.class);
                    if (!opportunitiesModel.isActive()) {//للتأكد من ان الفرص التطوعية غير مفعلة مسبقا
                        resultsList.add(opportunitiesModel);

                    }
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
                Toast.makeText(OpportunitiesAcceptActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}