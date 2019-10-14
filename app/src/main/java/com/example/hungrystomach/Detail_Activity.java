package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Adapter.FoodAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_DES;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_NAME;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_PRICE;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_URL;

public class Detail_Activity extends AppCompatActivity  {
    public static final String EXTRA_FOOD = "extra_food";
    String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_actiity);

        productID = getIntent().getStringExtra("pid");

        String imageUrl = getIntent().getStringExtra(EXTRA_URL);
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String description = getIntent().getStringExtra(EXTRA_DES);
        String price = getIntent().getStringExtra(EXTRA_PRICE);

        Button detail_atc = findViewById(R.id.detail_atc);
        ImageView detail_img = findViewById(R.id.img_detail);
        TextView detail_name = findViewById(R.id.text_detail_name);
        TextView detail_des = findViewById(R.id.text_detail_des);
        TextView detail_price = findViewById(R.id.text_detail_price);
        
        Glide.with(this).load(imageUrl).into(detail_img);
        detail_name.setText(name);
        detail_price.setText(price);
        detail_des.setText(description);

        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
        DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        //when I click detail_atc


        //create new tmp database; each act only has one shoppingChat

        //added; but cannot add yourself food
    }


}
