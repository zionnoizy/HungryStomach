package com.example.hungrystomach;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.ShoppingCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.hungrystomach.Adapter.DetailAdapter.FoodViewHolder.EXTRA_DES;
import static com.example.hungrystomach.Adapter.DetailAdapter.FoodViewHolder.EXTRA_NAME;
import static com.example.hungrystomach.Adapter.DetailAdapter.FoodViewHolder.EXTRA_PRICE;
import static com.example.hungrystomach.Adapter.DetailAdapter.FoodViewHolder.EXTRA_URL;
import static com.example.hungrystomach.Adapter.DetailAdapter.FoodViewHolder.EXTRA_UUID;

public class Detail_Activity extends AppCompatActivity  {
    private String TAG = "Detail_debug";
    String imageUrl;
    String name;
    String price;
    String description;
    String uploader_uid;


    ElegantNumberButton numberButton;
    String get_quanity;

    DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("all_uploaded_image");
    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    String cur_uid = m_auth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_actiity);

        imageUrl = getIntent().getStringExtra(EXTRA_URL);
        name = getIntent().getStringExtra(EXTRA_NAME);
        price = getIntent().getStringExtra(EXTRA_PRICE);
        description = getIntent().getStringExtra(EXTRA_DES);
        uploader_uid = getIntent().getStringExtra(EXTRA_UUID);

        Button detail_atc = findViewById(R.id.detail_atc);
        ImageView detail_img = findViewById(R.id.img_detail);
        TextView detail_name = findViewById(R.id.text_detail_name);
        TextView detail_des = findViewById(R.id.text_detail_des);
        TextView detail_price = findViewById(R.id.text_detail_price);

        numberButton = (ElegantNumberButton)findViewById(R.id.quantitiy);
        numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_quanity = numberButton.getNumber();
            }
        });

        Glide.with(this).load(imageUrl).into(detail_img);
        detail_name.setText(name);
        detail_price.setText(price);
        detail_des.setText(description);

        detail_atc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cannot add your food
                Log.d(TAG, cur_uid + uploader_uid);
                if(cur_uid.equals(uploader_uid)){
                    Toast.makeText(Detail_Activity.this,"You cannot add your food in to cart.",Toast.LENGTH_SHORT).show();
                } else{
                    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
                    DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());
                    DatabaseReference product = usr_uid.child(name);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String mofDate = dateFormat.format(date);
                    double subTTL = Double.parseDouble(price) * Integer.parseInt(numberButton.getNumber());
                    Toast.makeText(Detail_Activity.this,name +" has been added To cart",Toast.LENGTH_SHORT).show();

                    ShoppingCart sc = new ShoppingCart(name, price, Integer.parseInt(numberButton.getNumber()), imageUrl, mofDate, subTTL, cur_uid); //uploader_uid
                    usr_uid.child(name).setValue(sc);
                }

            }
        });

    }



}
