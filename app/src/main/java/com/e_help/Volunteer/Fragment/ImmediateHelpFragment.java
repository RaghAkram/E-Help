package com.e_help.Volunteer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.e_help.R;
import com.e_help.Volunteer.Activity.AddImmediateHelpActivity;
import com.e_help.Volunteer.Activity.ShowImmediateHelpActivity;

public class ImmediateHelpFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_immediate_help, container, false);

        view.findViewById(R.id.buttonHelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), AddImmediateHelpActivity.class);
                startActivity(newActivity);
            }
        });
        view.findViewById(R.id.buttonJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getContext(), ShowImmediateHelpActivity.class);
                startActivity(newActivity);
            }
        });
        return view;
    }


}