package com.example.hungrystomach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hungrystomach.Adapter.ChatAdapter;
import com.example.hungrystomach.Adapter.CommentAdapter;
import com.example.hungrystomach.Adapter.RatingAdapter;
import com.example.hungrystomach.Model.Chat;
import com.example.hungrystomach.Model.Comment;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.ShoppingCart;
import com.example.hungrystomach.Model.UnRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_DES;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_KEY;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_NAME;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_PRICE;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_KEY;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_RATING;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_URL;
import static com.example.hungrystomach.Adapter.FoodAdapter.FoodViewHolder.EXTRA_UUID;

public class Detail_Activity extends AppCompatActivity  {

    String imageUrl;
    String name;
    String price;
    String description;
    String uploader_uid;
    String key;
    DatabaseReference findKeyRef = FirebaseDatabase.getInstance().getReference("all_uploaded_image");
    String rating_float;

    TextView detail_uploader;
    RatingBar ratingbar;
    Button FoodClickComment;
    ElegantNumberButton numberButton;
    String get_quanity;


    FirebaseAuth m_auth = FirebaseAuth.getInstance();
    String my_uid = m_auth.getCurrentUser().getUid();

    //CheckWithSameCookerToCart helper
    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);
    String find_uuid;

    //comment activity
    CommentAdapter adapter;
    public static final String READ_RANDOM_KEY = "NoRandomKey";


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
        rating_float = getIntent().getStringExtra(EXTRA_RATING);
        Query get_key = findKeyRef.child(key);

        Button detail_atc = findViewById(R.id.detail_atc);
        ImageView detail_img = findViewById(R.id.img_detail);
        TextView detail_name = findViewById(R.id.text_detail_name);
        TextView detail_des = findViewById(R.id.text_detail_des);
        TextView detail_price = findViewById(R.id.text_detail_price);
        detail_uploader = findViewById(R.id.uploader);
        ratingbar = findViewById(R.id.rating);
        FoodClickComment = findViewById(R.id.click_view_comment_btn);

        FoodClickComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent view_comment = new Intent(Detail_Activity.this, Read_Comment_Activity.class);
                view_comment.putExtra(READ_RANDOM_KEY, key);
                startActivity(view_comment);
            }
        });

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
        ratingbar.setRating(Float.parseFloat(rating_float));

        //only detail could view these info
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
                Log.d("Detail_debug", my_uid + uploader_uid);
                if (my_uid.equals(uploader_uid)) {//cannot add your food
                    Toast.makeText(Detail_Activity.this, "You cannot add your prepared food in to cart.", Toast.LENGTH_SHORT).show();
                } else {
                    CheckWithSameCookerToCart();
                }
            }
        });
    }

    public void CheckWithSameCookerToCart(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("shopping_cart").child(my_uid);
        ref.addValueEventListener(new ValueEventListener() {
            boolean processDone = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Detail_debug","+" + dataSnapshot.getChildrenCount());
                if(!processDone) {
                    if (dataSnapshot.getChildrenCount() != 0) { //if exist
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String mofDate = dateFormat.format(date);
                        double subTTL = Double.parseDouble(price) * Integer.parseInt(numberButton.getNumber());

                        //shopping cart exist
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ShoppingCart sc = snapshot.getValue(ShoppingCart.class);
                            find_uuid = sc.getUploader_uid();
                        }

                        if (find_uuid.equals(uploader_uid)) { //items are same cooker
                            Toast.makeText(Detail_Activity.this, name + ": has been added To cart.", Toast.LENGTH_SHORT).show();
                            ShoppingCart atc = new ShoppingCart(name, price, Integer.parseInt(numberButton.getNumber()), imageUrl, mofDate, subTTL, my_uid, uploader_uid, key);
                            cartListRef.child(name).setValue(atc);

                        } else {
                            Toast.makeText(Detail_Activity.this, "You cannot add item with different cooker(s) in shopping cart", Toast.LENGTH_SHORT).show();

                        }
                        processDone = true;
                    } else { //cart empty
                        AddItemToEmptyCart();
                        processDone = true;
                    }
                }
                processDone = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void AddItemToEmptyCart(){
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
        DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String mofDate = dateFormat.format(date);
        double subTTL = Double.parseDouble(price) * Integer.parseInt(numberButton.getNumber());
        Toast.makeText(Detail_Activity.this, "Your first Item has been added To cart", Toast.LENGTH_SHORT).show();

        ShoppingCart sc = new ShoppingCart(name, price, Integer.parseInt(numberButton.getNumber()), imageUrl, mofDate, subTTL, my_uid, uploader_uid, key);
        usr_uid.child(name).setValue(sc);
    }

}
