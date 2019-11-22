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

import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_DES;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_KEY;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_NAME;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_PRICE;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_KEY;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_URL;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_UUID;

public class Detail_Activity extends AppCompatActivity  {
    private String TAG = "Detail_debug";
    String imageUrl;
    String name;
    String price;
    String description;
    String uploader_uid;
    String key;

    ElegantNumberButton numberButton;
    String get_quanity;

    DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("all_uploaded_image");

    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    String my_uid = m_auth.getCurrentUser().getUid();

    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);

    TextView detail_uploader;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_actiity);

        imageUrl = getIntent().getStringExtra(EXTRA_URL);
        name = getIntent().getStringExtra(EXTRA_NAME);
        price = getIntent().getStringExtra(EXTRA_PRICE);
        description = getIntent().getStringExtra(EXTRA_DES);
        uploader_uid = getIntent().getStringExtra(EXTRA_UUID);
        key = getIntent().getStringExtra(EXTRA_KEY);
        Query get_key = database_ref.child(key);

        Button detail_atc = findViewById(R.id.detail_atc);
        ImageView detail_img = findViewById(R.id.img_detail);
        TextView detail_name = findViewById(R.id.text_detail_name);
        TextView detail_des = findViewById(R.id.text_detail_des);
        TextView detail_price = findViewById(R.id.text_detail_price);
        detail_uploader = findViewById(R.id.uploader);

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

        //only detail could view info
        get_key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food u = dataSnapshot.getValue(Food.class);
                String uploader = u.getUploader();
                detail_uploader.setText(uploader);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        detail_atc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, my_uid + uploader_uid);
                if (my_uid.equals(uploader_uid)) {//cannot add your food
                    Toast.makeText(Detail_Activity.this, "You cannot add your prepared food in to cart.", Toast.LENGTH_SHORT).show();
                } else {
                    CheckWithSameCookerToCart();
                }
            }
        });

    }

    public void CheckWithSameCookerToCart(){
        Query query = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("DTAG", "s:" + dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() != 0){ //if exist
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String mofDate = dateFormat.format(date);
                    double subTTL = Double.parseDouble(price) * Integer.parseInt(numberButton.getNumber());

                    //shopping cart exist
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ShoppingCart sc = snapshot.getValue(ShoppingCart.class);
                        if (sc.getUploader_uid().equals(uploader_uid)) { //same uploader
                            Toast.makeText(Detail_Activity.this, name + ": has been added To cart", Toast.LENGTH_SHORT).show();
                            ShoppingCart atc = new ShoppingCart(name, price, Integer.parseInt(numberButton.getNumber()), imageUrl, mofDate, subTTL, my_uid, uploader_uid);
                            cartListRef.child(name).setValue(atc);
                        } else
                            Toast.makeText(Detail_Activity.this, "Your cannot add item with different cooker(s) in shopping cart", Toast.LENGTH_SHORT).show();
                    }
                }
                else{ //cart empty
                    AddItemToEmptyCart();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void AddItemToEmptyCart(){
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
        DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());
        DatabaseReference product = usr_uid.child(name);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String mofDate = dateFormat.format(date);
        double subTTL = Double.parseDouble(price) * Integer.parseInt(numberButton.getNumber());
        Toast.makeText(Detail_Activity.this, "Your first Item has been added To cart", Toast.LENGTH_SHORT).show();

        ShoppingCart sc = new ShoppingCart(name, price, Integer.parseInt(numberButton.getNumber()), imageUrl, mofDate, subTTL, my_uid, uploader_uid);
        usr_uid.child(name).setValue(sc);
    }

}
