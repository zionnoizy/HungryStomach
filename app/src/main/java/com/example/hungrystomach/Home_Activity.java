package com.example.hungrystomach;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import com.example.hungrystomach.Adapter.TabPagerAdapter;
import com.example.hungrystomach.Fragment.AllFoodFragment;
import com.example.hungrystomach.Fragment.FoodFragment2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Home_Activity extends AppCompatActivity {

    FirebaseAuth m_auth;

    FoodFragment2 fragment;
    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configureTabLayout();
        //sendNotification();


        //get InstanceID
        /*
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tokenErr", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("GenTok", msg);
                    }
                });
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.it_upload:
                Toast.makeText(this,"Upload Your Food Here", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Home_Activity.this, Upload_Activity.class);
                startActivity(i);
                return true;
            case R.id.it_shoppingcart:
                Intent intent = new Intent(Home_Activity.this, Cart_Activity.class);
                startActivity(intent);
                return true;
            case R.id.it_chat:
                Intent go_chat = new Intent(Home_Activity.this, RoomUser_Activity.class);
                startActivity(go_chat);
                return true;
            case R.id.sort_oldest:
                fragment.sort_name(); //fixed?
                return true;
            case R.id.it_settings:
                //Toast.makeText(this,"Redirected to Setting Section", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(Home_Activity.this, Setting_Activity.class);
                startActivity(intent2);
                return true;
            case R.id.it_logout:
                userLogout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void userLogout(){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this,"Successfully Logout. Redirecting to HomePage", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Home_Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void configureTabLayout(){
        TabLayout tab_lyout = (TabLayout) findViewById(R.id.tab_layout);

        tab_lyout.addTab(tab_lyout.newTab().setText("All Food"));
        tab_lyout.addTab(tab_lyout.newTab().setText("From Newest"));
        tab_lyout.addTab(tab_lyout.newTab().setText("Search"));

        final ViewPager view_pager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), tab_lyout.getTabCount());
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_lyout));

        tab_lyout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                view_pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab){
                //
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab){
                //
            }

        });
    }
    //////////////////////////////////////////////////////////////////////////////////
    public void sendNotification(View v){
    }
}