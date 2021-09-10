package com.e_help.Volunteer.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e_help.Model.OpportunitiesModel;
import com.e_help.Model.User;
import com.e_help.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    View view;
    List<User> UserVolunteer = new ArrayList<>();
    // متغيرات للاحصائيات
    // حامر على المستخدمين واحسب بالارقام
    int num_val = 0;
    int number_team = 0;
    int number_org = 0;
    int number_volunteer_opertunity=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        //المراكز الاولى والثانية والثالثة + السكور حقتهم
        TextView name1 = (TextView) view.findViewById(R.id.name1);
        TextView name2 = (TextView) view.findViewById(R.id.name2);
        TextView name3 = (TextView) view.findViewById(R.id.name3);

        TextView score1 = (TextView) view.findViewById(R.id.score1);
        TextView score2 = (TextView) view.findViewById(R.id.score2);
        TextView score3 = (TextView) view.findViewById(R.id.score3);
        // هنا الاحصائيات
        TextView num_volunteer = (TextView) view.findViewById(R.id.num_volunteer);
        TextView num_volunteer_opertunity = (TextView) view.findViewById(R.id.num_volunteer_opertunity);
        TextView num_team = (TextView) view.findViewById(R.id.num_team);
        TextView num_org = (TextView) view.findViewById(R.id.num_org);

        //كل اللي فوق مخزنين هنا اصلا فاجيبهم
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/")
                .getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVolunteer.clear();//array من نوع list
                num_val = 0;
                number_team = 0;
                number_org = 0;
                if (dataSnapshot.exists())
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//يجبلي كل الداتا لليوزر
                        User user = snapshot.getValue(User.class);//ارتبها في كلاس عشان اعرف اتعامل معاها
                        if (user.getUser_type() == 1) { // اتاكد من نوعه + اضيفه في لسته الفلنتير
                            UserVolunteer.add(user);  // هنا انضاف في الليست (عشان احتاجه في الليدر بورد)
                            num_val++;// هنا زاد في الاحصائيات

                        } else if (user.getUser_type() == 2) {
                            number_team++;

                        } else if (user.getUser_type() == 3) {
                            number_org++;
                        }
                        // محا يحسب الادمن عشان التايب حقه 10
                    }
                // خلاص فوق حسب الاحصائيات => هنا ح يعرضهافي تكست
                num_volunteer.setText(num_val + "");
                num_team.setText(number_team + "");
                num_org.setText(number_org + "");


                 // هنا حاجيب 3 مراكز الاولى ( الي لهم اعلى 3 سكورز في التطبيق)
                // الجافا وفرت ميثودCollections.sort تسأل على اي اساس حارتبها
                //حاقله رتبلي هيا على اساس points  حقت userVolunteer اللي خزناهم في الليست "فقط للمتطوعين"
                //حيقارن السكورز بين متطوعين

                Collections.sort(UserVolunteer, new Comparator<User>(){
                    //compares two class specific objects (x, y) given as parameters.
                    //لترتيب على أساس المعايير criteria باستخدام (Comparator)
                    //accepts two arguments and returns a value that determines the sort order.
                    public int compare(User obj1, User obj2) {
                        return Integer.valueOf(obj2.getPoint()).compareTo(Integer.valueOf(obj1.getPoint()));   // To compare integer values
                    }
                });
                //وحيرتبلي الليست من الاكثر نقاطاً للاقل
                // اخدنا اول ثلاثة
                if(UserVolunteer.size()>3){
                    name1.setText(UserVolunteer.get(0).getFirst_name()+" "+UserVolunteer.get(0).getLast_name());
                    name2.setText(UserVolunteer.get(1).getFirst_name()+" "+UserVolunteer.get(1).getLast_name());
                    name3.setText(UserVolunteer.get(2).getFirst_name()+" "+UserVolunteer.get(2).getLast_name());

                    score1.setText(UserVolunteer.get(0).getPoint()+"");
                    score2.setText(UserVolunteer.get(1).getPoint()+"");
                    score3.setText(UserVolunteer.get(2).getPoint()+"");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // احصائية "لعدد الفرص"
        DatabaseReference mdatabaseOpportunities = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").
                getReference().child("Opportunities"); // حنجيب كل الفرص من الفايربيس
        mdatabaseOpportunities.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number_volunteer_opertunity=0;  // نتاكد انها صفر
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    number_volunteer_opertunity++; // نضيف على العدد
                }
                num_volunteer_opertunity.setText(number_volunteer_opertunity+"");    // نعرض عدد الفرص في تكست
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}