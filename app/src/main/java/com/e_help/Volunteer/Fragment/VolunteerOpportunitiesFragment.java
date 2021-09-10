package com.e_help.Volunteer.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e_help.Adapter.OpportunitiesAdapter;
import com.e_help.Opportunities.AddOpportunitiesActivity;
import com.e_help.Model.OpportunitiesModel;
import com.e_help.Model.dataModel;
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

import static android.content.Context.MODE_PRIVATE;

public class VolunteerOpportunitiesFragment extends Fragment {
    View view;
    int Type = 0;
    FloatingActionButton fab_add;
    List<OpportunitiesModel> resultsList, Filter;
    OpportunitiesAdapter nAdapter;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    DatabaseReference mdatabase;
    Spinner entity_type, city_sp;
    TextView no_data;
    EditText search;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_opportunities, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        preferences.getString("Uid", "");
        Type = preferences.getInt("UserType", 0);
        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
        entity_type = (Spinner) view.findViewById(R.id.entity_type);
        GetTheField(entity_type);
        city_sp = (Spinner) view.findViewById(R.id.city_sp);
        GetCity(city_sp);
        Button member_team = (Button) view.findViewById(R.id.member_team);
        if (Type == 2 || Type == 3) {
            fab_add.setVisibility(View.VISIBLE);
        }
        member_team.setVisibility(View.VISIBLE);
        member_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VolunteerTeamsActivity.class);
                startActivity(intent);
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddOpportunitiesActivity.class);
                startActivity(intent);
            }
        });

        mdatabase = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("Opportunities");

        search = view.findViewById(R.id.search);
        no_data = view.findViewById(R.id.no_data);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);

        recyclerView = view.findViewById(R.id.recycler);
        resultsList = new ArrayList<>();
        Filter = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        nAdapter = new OpportunitiesAdapter(getContext(), resultsList);
        recyclerView.setAdapter(nAdapter);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                nAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        progress_bar.setVisibility(View.VISIBLE);

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resultsList.clear();
                progress_bar.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OpportunitiesModel helpModel = snapshot.getValue(OpportunitiesModel.class);

                    if (helpModel.isActive()) {
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
                Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    int selectedTheField = 0;
    String selectedNameTheField = "";

    public void GetTheField(Spinner the_field) {

        List<dataModel> listModels = new ArrayList<>();
        listModels.add(new dataModel(0, "اختر المجال"));
        listModels.add(new dataModel(1, "التعليم"));
        listModels.add(new dataModel(2, "الصحة"));
        listModels.add(new dataModel(3, "الاماكن المقدسة"));
        listModels.add(new dataModel(4, "الجمعيات الخيرية"));
        listModels.add(new dataModel(5, "البيئة"));
        listModels.add(new dataModel(6, "الترفيه"));
        listModels.add(new dataModel(7, "افتراضي"));
        listModels.add(new dataModel(8, "التدريب والتطوير"));
        listModels.add(new dataModel(9, "الرعاية بالمسنين"));
        listModels.add(new dataModel(10, "تنمية المجتمع"));
        listModels.add(new dataModel(11, "رعاية الاطفال"));
        listModels.add(new dataModel(12, "خدمات سكنية"));

        final String[] name = new String[listModels.size()];
        final String[] idList = new String[listModels.size()];

        for (int i = 0; i < listModels.size(); i++) {
            name[i] = listModels.get(i).getName();
            idList[i] = listModels.get(i).getId() + "";
        }

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, name);
        the_field.setAdapter(adapterPiece);
        the_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                } catch (Exception ex) {
                }
                if (item != null) {
                    selectedTheField = Integer.parseInt(idList[position]);
                    selectedNameTheField = name[position];
                    Filter.clear();
                    for (int i = 0; i < resultsList.size(); i++) {
                        if ((resultsList.get(i).getType().equals(selectedNameTheField) || selectedNameTheField.equals("اختر المجال")) &&
                                (resultsList.get(i).getCity().equals(selectedNameCity) || selectedNameCity.equals("اختر مدينة"))) {
                            Filter.add(resultsList.get(i));
                        }

                    }
                    if (selectedTheField == 0) {
                        nAdapter = new OpportunitiesAdapter(getContext(), resultsList);
                        Filter.addAll(resultsList);
                        no_data.setVisibility(View.GONE);
                    } else {
                        nAdapter = new OpportunitiesAdapter(getContext(), Filter);
                        if (Filter.size() == 0) {
                            no_data.setVisibility(View.VISIBLE);
                        } else {
                            no_data.setVisibility(View.GONE);
                        }

                    }
                    recyclerView.setAdapter(nAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    int selectedCity = 0;
    String selectedNameCity = "";

    public void GetCity(Spinner city) {

        List<dataModel> listModels = new ArrayList<>();
        listModels.add(new dataModel(0, "اختر مدينة"));
        listModels.add(new dataModel(1, "المدينة المنورة"));
        listModels.add(new dataModel(2, "ينبع"));
        listModels.add(new dataModel(3, "جدة"));
        listModels.add(new dataModel(4, "مكة"));
        listModels.add(new dataModel(5, "الطايف"));
        listModels.add(new dataModel(-1, "افتراضية"));


        final String[] name = new String[listModels.size()];
        final String[] idList = new String[listModels.size()];

        for (int i = 0; i < listModels.size(); i++) {
            name[i] = listModels.get(i).getName();
            idList[i] = listModels.get(i).getId() + "";
        }

        ArrayAdapter adapterPiece = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, name);
        city.setAdapter(adapterPiece);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                try {
                    ((TextView) view).setTextColor(Color.BLACK);
                } catch (Exception ex) {
                }
                if (item != null) {
                    selectedCity = Integer.parseInt(idList[position]);
                    selectedNameCity = name[position];

                    Filter.clear();
                    for (int i = 0; i < resultsList.size(); i++) {
                        if ((resultsList.get(i).getType().equals(selectedNameTheField) || selectedNameTheField.equals("اختر المجال")) &&
                                (resultsList.get(i).getCity().equals(selectedNameCity) || selectedNameCity.equals("اختر مدينة"))) {
                            Filter.add(resultsList.get(i));
                        }

                    }
                    if (selectedCity == 0) {
                        nAdapter = new OpportunitiesAdapter(getContext(), resultsList);
                        Filter.addAll(resultsList);
                        no_data.setVisibility(View.GONE);
                    } else {
                        nAdapter = new OpportunitiesAdapter(getContext(), Filter);
                        if (Filter.size() == 0) {
                            no_data.setVisibility(View.VISIBLE);
                        } else {
                            no_data.setVisibility(View.GONE);
                        }

                    }
                    recyclerView.setAdapter(nAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}