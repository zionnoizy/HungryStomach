package com.example.hungrystomach;


import android.content.Intent;
import android.os.Bundle;

import com.example.hungrystomach.Adapter.TabPagerAdapter;
import com.example.hungrystomach.Fragment.FromNewFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configureTabLayout();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Home_Activity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                Log.e("token", "find fcm token" + mToken);
            }
        });

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
                Toast.makeText(this,"Upload Your Home Made Food Here", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Home_Activity.this, Upload_Activity.class);
                startActivity(i);
                return true;
            case R.id.it_shoppingcart:
                Intent intent = new Intent(Home_Activity.this, Cart_Activity.class);
                startActivity(intent);
                return true;
            case R.id.receipt:
                Intent go_receipt = new Intent(Home_Activity.this, Receipt_Activity.class);
                startActivity(go_receipt);
                return true;
            case R.id.request:
                Intent go_status = new Intent(Home_Activity.this, Request_Activity.class);
                startActivity(go_status);
                return true;
            case R.id.rating_food:
                Intent go_rating = new Intent(Home_Activity.this, Rating_Activity.class);
                startActivity(go_rating);
                return true;
            case R.id.it_settings:
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

}