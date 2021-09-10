package com.e_help.Volunteer;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.e_help.Notification.Notifications.Token;
import com.e_help.Organisation.ProfileOrganistionFragment;
import com.e_help.R;
import com.e_help.Volunteer.Fragment.CheckFragment;
import com.e_help.Volunteer.Fragment.HomeFragment;
import com.e_help.Volunteer.Fragment.ImmediateHelpFragment;
import com.e_help.Team.ProfileTeamFragment;
import com.e_help.Volunteer.Fragment.ProfileVolunteerFragment;
import com.e_help.Volunteer.Fragment.VolunteerOpportunitiesFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    int user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        user_type = preferences.getInt("UserType", 0);//للتحقق من نوع المستخدم

        setContentView(R.layout.main_volunteer);

        viewPager = (ViewPager) findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getCustomView().findViewById(R.id.icon2).setBackgroundColor(getResources().getColor(R.color.orange));
                tab.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.icon2).setBackgroundColor(getResources().getColor(R.color.gray_tab));
                tab.getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//لجلب بيانات المستخدم الذي سجل دخوله
        //لتخزين توكن الفيربيس لغرض الاشعارات
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("newToken", newToken);
                        if (firebaseUser != null) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance("https://ehelp-24142-default-rtdb.firebaseio.com/").getReference("Tokens");
                            Token token = new Token(newToken);
                            reference.child(firebaseUser.getUid()).setValue(token);
                        }
                    }
                });

    }


    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(R.drawable.chat);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);


        tabLayout.getTabAt(2).setIcon(R.drawable.like);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
        if (user_type == 1) {
            tabLayout.getTabAt(3).setIcon(R.drawable.pin);
            tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(4).setIcon(R.drawable.user);
            tabLayout.getTabAt(4).getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);

        } else {
            tabLayout.getTabAt(3).setIcon(R.drawable.user);
            tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);

        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.custom_tab);
        }
        tabLayout.getTabAt(0).getCustomView().findViewById(R.id.icon2).setBackgroundColor(getResources().getColor(R.color.orange));
        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());

        viewPager.setCurrentItem(0, false);

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ImmediateHelpFragment());
        adapter.addFragment(new VolunteerOpportunitiesFragment());

        if (user_type == 1) {
            adapter.addFragment(new CheckFragment());
            adapter.addFragment(new ProfileVolunteerFragment());
        } else if (user_type == 2) {
            adapter.addFragment(new ProfileTeamFragment());
        } else {
            adapter.addFragment(new ProfileOrganistionFragment());
        }

        viewPager.setAdapter(adapter);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }


    }


}
