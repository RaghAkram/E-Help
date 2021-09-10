package com.e_help.Team;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.e_help.Model.User;
import com.e_help.R;
import com.e_help.Volunteer.Activity.AddImmediateHelpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class ProfileTeamFragment extends Fragment {
    View view;
    TextView name_team, leader_name, social_media;

    String Uid = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_team, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        name_team = (TextView) view.findViewById(R.id.name_team);
        leader_name = (TextView) view.findViewById(R.id.leader_name);
        social_media = (TextView) view.findViewById(R.id.social_media);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    name_team.setText(user.getName_team() + "");
                    leader_name.setText(user.getLeader_name() + "");
                    social_media.setText(user.getSocial_media() + "");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        view.findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), EditProfileLeaderActivity.class);
                startActivity(newActivity);


            }
        });
        view.findViewById(R.id.add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), AddTaskActivity.class);
                startActivity(newActivity);
            }
        });
        view.findViewById(R.id.member_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), TeamMembersActivity.class);
                startActivity(newActivity);
            }
        });
        view.findViewById(R.id.orders_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), JoinRequestsActivity.class);
                startActivity(newActivity);
            }
        });
        view.findViewById(R.id.my_opportunities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), OpportunitiesOfferedActivity.class);
                startActivity(newActivity);
            }
        });



        return view;
    }
}