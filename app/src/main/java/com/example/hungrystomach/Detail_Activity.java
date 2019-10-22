package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Model.ShoppingCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_DES;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_NAME;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_PRICE;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_URL;

public class Detail_Activity extends AppCompatActivity  {
    public static final String EXTRA_FOOD = "extra_food";
    String productID = "";

    String imageUrl;
    String name;
    String description;
    String price;

    ElegantNumberButton numberButton;
    String get_quanity;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_actiity);

        productID = getIntent().getStringExtra("pid");

        imageUrl = getIntent().getStringExtra(EXTRA_URL);
        name = getIntent().getStringExtra(EXTRA_NAME);
        description = getIntent().getStringExtra(EXTRA_DES);
        price = getIntent().getStringExtra(EXTRA_PRICE);

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

        //set detail text info
        Glide.with(this).load(imageUrl).into(detail_img);
        detail_name.setText(name);
        detail_price.setText(price);
        detail_des.setText(description);

        //firebase
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);

        //when I click add to cart
        detail_atc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth m_auth = FirebaseAuth.getInstance();
                DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
                DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());
                DatabaseReference product = usr_uid.child(name);

                ShoppingCart sc = new ShoppingCart(name, price, Integer.parseInt(numberButton.getNumber()), imageUrl);
                usr_uid.child(name).setValue(sc);

                //cur_date
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String mofDate = dateFormat.format(date).toString();

                //cur_uid
                String cur_uid = m_auth.getCurrentUser().getUid();

                //sub_ttl
                double subTTL = Double.parseDouble(price) * Integer.parseInt(numberButton.getNumber());

                //product.child("product_price").setValue(price);
                //product.child("product_quantity").setValue(numberButton.getNumber());
                product.child("add_date").setValue(mofDate);
                product.child("sub_ttl").setValue(subTTL);
                //product.child("img_url").setValue(imageUrl);
                product.child("uploader_uid").setValue(cur_uid); //how to find uplaoder id?
                Toast.makeText(Detail_Activity.this,name +" has been added To cart",Toast.LENGTH_SHORT).show();
            }
        });

        //create new tmp database; each act only has one shoppingChat



        //added; but cannot add yourself food
    }



}
