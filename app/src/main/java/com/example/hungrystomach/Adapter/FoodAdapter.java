package com.example.hungrystomach.Adapter;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private ArrayList<Food> m_listFood;
    private Context m_context;

    private ImageView image_view;
    FirebaseAuth m_auth;
    Food f;

    public FoodAdapter(ArrayList<Food> list, Context context) {//Foodlist,this
        this.m_listFood = list;
        this.m_context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_all_food_item, parent, false);
        return new FoodViewHolder(v);
    }





    public class FoodViewHolder extends RecyclerView.ViewHolder{
        public ImageView FoodIcon;
        public TextView FoodName;
        public TextView FoodDescription;
        public TextView FoodPrice;

        public FoodViewHolder (View itemView){
            super(itemView);
            FoodIcon = (ImageView)itemView.findViewById(R.id.thumbnail);
            FoodName = itemView.findViewById(R.id.foodname);
            //FoodDescription = itemView.findViewById(R.id.tv_des);
            //FoodPrice = itemView.findViewById(R.id.tv_price);
            //m_auth = FirebaseAuth.getInstance();
        }

        public void onClick(View view) {
            Log.e("name",FoodName.toString());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        f = m_listFood.get(position);
        holder.FoodName.setText(f.get_name());
        f.set_icon(f.get_icon());
        Picasso .get()
                .load(f.get_imgurl()) // Uri.parse(m_listFood.get(position).getLogo())
                //.error(R.drawable.placeholder)
                .fit()
                .into(holder.FoodIcon);
    }

    public ArrayList<Food> getListFood() {
        return m_listFood;
    }

    public void setListFood(ArrayList<Food> listFood) {
        this.m_listFood = listFood;
    }

    @Override
    public int getItemCount() {
        return m_listFood.size();
    }

    public void setUploads(ArrayList<Food> uploads) {
        this.m_listFood = uploads;
        this.notifyDataSetChanged();
    }

}
