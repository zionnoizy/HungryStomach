package com.example.hungrystomach.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hungrystomach.Fragment.AllFoodFragment;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context m_context;
    private ArrayList<Food> m_listFood  = new ArrayList<Food>();

    public FoodAdapter(Context context, ArrayList<Food> food_list) {//m_context, food_list
        this.m_context = context;
        this.m_listFood = food_list;
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_all_food, parent, false);
        return new FoodViewHolder(v);
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder{
        public ImageView FoodIcon;
        public TextView FoodName;
        public TextView FoodDescription;
        public TextView FoodPrice;
        public Button Fooddetail;

        public FoodViewHolder (View itemView){
            super(itemView);
            FoodIcon = (ImageView)itemView.findViewById(R.id.thumbnail);
            FoodName = itemView.findViewById(R.id.foodname);
            FoodDescription = itemView.findViewById(R.id.fooddescription);
            FoodPrice = itemView.findViewById(R.id.foodprice);
            Fooddetail = itemView.findViewById(R.id.fooddetail);
        }

        public void onClick(View view) {
            Log.e("name",FoodName.toString());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        Food f = m_listFood.get(position);
        holder.FoodName.setText(f.get_name());
        holder.FoodDescription.setText(f.get_description());
        holder.FoodPrice.setText(f.get_price());
        holder.FoodPrice.setText(f.get_url());
        //Picasso.get().load(m_listFood.get(position).get_url()).fit().into(holder.FoodIcon);
        Glide.with(m_context).load(f.get_url()).into(holder.FoodIcon); //getListFood().get(position).get_icon()
    }

    public void setListFood(ArrayList<Food> m_listFood){
        this.m_listFood = m_listFood;
    }

    @Override
    public int getItemCount() {
        return m_listFood.size();
    }

    public Context get_context() {
        return m_context;
    }

}
