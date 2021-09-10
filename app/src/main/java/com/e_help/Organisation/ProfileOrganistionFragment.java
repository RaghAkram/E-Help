package com.e_help.Organisation;

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
import com.e_help.Team.EditProfileLeaderActivity;
import com.e_help.Team.JoinRequestsActivity;
import com.e_help.Team.OpportunitiesOfferedActivity;
import com.e_help.Team.TeamMembersActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class ProfileOrganistionFragment extends Fragment {
    View view;
    TextView name, location, phone;
    String Uid = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_organistion, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");
        name = (TextView) view.findViewById(R.id.name);
        location = (TextView) view.findViewById(R.id.location);
        phone = (TextView) view.findViewById(R.id.phone);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getName_entity() + "");
                    location.setText(user.getCity_entityName());
                    phone.setText(user.getPhone_num_entity() + "");
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
                Intent newActivity = new Intent(getContext(), EditProfileOrgActivity.class);
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