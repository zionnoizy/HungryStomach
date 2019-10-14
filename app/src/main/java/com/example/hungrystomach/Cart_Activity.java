package com.example.hungrystomach;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Cart_Activity extends AppCompatActivity {

    RecyclerView recycler_view;
    RecyclerView.LayoutManager lm;

    TextView total_amount;
    Button btn_co, btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);

        recycler_view = findViewById(R.id.cart_recycler_view);
        recycler_view.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(lm);

        btn_co = findViewById(R.id.btn_co);
        btn_delete = findViewById(R.id.delete_item);
        total_amount = findViewById(R.id.total_price);

        //adapter?

        //delete item
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //checkout- btn_co
        btn_co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
