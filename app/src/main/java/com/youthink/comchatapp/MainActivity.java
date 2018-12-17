package com.youthink.comchatapp;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;

    private String user_addr;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewPager=(ViewPager)findViewById(R.id.pager);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }
            public void onTabUnselected(TabLayout.Tab tab) {    }
            public void onTabReselected(TabLayout.Tab tab) {    }
        });

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);

        /* 사용자 주소 저장 */
        Intent get_intent = getIntent();
        user_addr = get_intent.getStringExtra("user_addr");
    }

    public void onClick(View v){
        if(v==fab){
            Intent intent = new Intent(this, CreateRoomActivity.class);
            startActivityForResult(intent, 20);

        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments=new ArrayList<>();
        Context context = null;

        String title[]=new String[]{"채팅방 목록","내 채팅방"};

        public MyPagerAdapter(FragmentManager fm, Context context){
            super(fm);
            this.context = context;
            fragments.add(new AllChatFragment());
            fragments.add(new AllChatFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 20 && resultCode == RESULT_OK){
            Toast toast = Toast.makeText(this, "나눔방 생성 완료", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /* getUserAddr() : Fragment로 주소 넘기는 메소드 */
    public String getUserAddr(){
        return user_addr;
    }

}