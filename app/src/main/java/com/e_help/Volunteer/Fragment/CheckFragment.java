package com.e_help.Volunteer.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Adapter.OpportunitiesRegisterAdapter;
import com.e_help.Model.OpportunitieRegisterModel;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CheckFragment extends Fragment {
    View view;
    List<OpportunitieRegisterModel> resultsList;
    OpportunitiesRegisterAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    DatabaseReference mdatabase;
    TextView no_data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("OpportunitiesRegister");

        no_data = view.findViewById(R.id.no_data);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);

        recyclerView = view.findViewById(R.id.recycler);
        resultsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        nAdapter = new OpportunitiesRegisterAdapter(getContext(), resultsList);
        recyclerView.setAdapter(nAdapter);

        progress_bar.setVisibility(View.VISIBLE);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                            OpportunitieRegisterModel opportunitieRegisterModel = snapshot2.getValue(OpportunitieRegisterModel.class);

                            if (opportunitieRegisterModel.isActual() &&
                                            opportunitieRegisterModel.getId_member().equals(preferences.getString("Uid", ""))) {
                                resultsList.add(opportunitieRegisterModel);
                                nAdapter.notifyDataSetChanged();
                            }
                        }
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
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}