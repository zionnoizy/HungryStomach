package com.example.hungrystomach;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hungrystomach.Model.Food;

public class Detail_Activity extends AppCompatActivity {
    public static final String EXTRA_FOOD = "extra_food";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_actiity);

        ImageView detail_img = findViewById(R.id.img_detail);
        Button detail_atc = findViewById(R.id.detail_atc);
        TextView detail_name = findViewById(R.id.text_detail_name);
        TextView detail_des = findViewById(R.id.text_detail_des);
        TextView detail_price = findViewById(R.id.text_detail_price);

        /*
        Food fd = getIntent().getParcelablExtra(EXTRA_MOUNTION);

        //Picasso

        //description setText all fours

         */
    }
}
