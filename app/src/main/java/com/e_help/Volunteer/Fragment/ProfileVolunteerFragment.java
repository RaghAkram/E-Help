package com.e_help.Volunteer.Fragment;

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
import com.e_help.Team.TeamMembersActivity;
import com.e_help.User.FirstActivity;
import com.e_help.User.RegisterActivity;
import com.e_help.Volunteer.Activity.EditProfileVolunteerActivity;
import com.e_help.Volunteer.Activity.MyTaskTeamActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class ProfileVolunteerFragment extends Fragment {
    View view;
    TextView name, num_point, type_profile, interests;

    String Uid;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_volunteer, container, false);
        // Inflate the layout for this fragment

        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        Uid = preferences.getString("Uid", "");


        name = (TextView) view.findViewById(R.id.name);
        num_point = (TextView) view.findViewById(R.id.num_point);
        type_profile = (TextView) view.findViewById(R.id.type_profile);
        interests = (TextView) view.findViewById(R.id.interests);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users").child(Uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getFirst_name() + " " + user.getLast_name());
                    num_point.setText(user.getPoint() + " نقطة");
                    //لتحديد التصنيف
                    if (user.getPoint() >= 30) {
                        type_profile.setText("ذهبي");
                    } else if (user.getPoint() >= 20) {
                        type_profile.setText("فضي");
                    } else if (user.getPoint() >= 10) {
                        type_profile.setText("برونزي");
                    }else {
                        type_profile.setText("لايوجد تصنيف");
                    }
                    interests.setText(user.getVoluntary_interests() + "");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        view.findViewById(R.id.task_my_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), MyTaskTeamActivity.class);
                startActivity(newActivity);


            }
        });
        view.findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), EditProfileVolunteerActivity.class);
                startActivity(newActivity);
            }
        });

        return view;
    }
}