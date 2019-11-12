package com.example.hungrystomach.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Detail_Activity;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;

import java.util.ArrayList;


public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.FoodViewHolder> {
    private Context m_context;
    private ArrayList<Food> m_listFood;

    public DetailAdapter(Context context, ArrayList<Food> food_list) {
        this.m_context = context;
        this.m_listFood = food_list;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_fragment_all_food, parent, false);
        return new FoodViewHolder(v);
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder{
        public ImageView FoodIcon;
        public TextView FoodName;
        public TextView FoodDescription;
        public TextView FoodPrice;
        public Button Fooddetail;
        public TextView FoodUploader;

        public static final String EXTRA_URL = "NoImage";
        public static final String EXTRA_NAME = "NoName";
        public static final String EXTRA_DES = "NoDes";
        public static final String EXTRA_PRICE = "NoPrice";
        public static final String EXTRA_UUID = "NoUID";

        public FoodViewHolder (View itemView){
            super(itemView);
            FoodIcon = (ImageView)itemView.findViewById(R.id.thumbnail);
            FoodName = itemView.findViewById(R.id.foodname);
            FoodDescription = itemView.findViewById(R.id.fooddescription);
            FoodPrice = itemView.findViewById(R.id.foodprice);
            Fooddetail = itemView.findViewById(R.id.fooddetail);

            Fooddetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){

                        Food clcikedItem = m_listFood.get(position);
                        Intent to_detail = new Intent(m_context, Detail_Activity.class);

                        to_detail.putExtra(EXTRA_URL, clcikedItem.getUri());
                        to_detail.putExtra(EXTRA_NAME, clcikedItem.getName());
                        to_detail.putExtra(EXTRA_PRICE, clcikedItem.getPrice());
                        to_detail.putExtra(EXTRA_DES, clcikedItem.getDescription());
                        to_detail.putExtra(EXTRA_UUID, clcikedItem.getUploader_uid());

                        m_context.startActivity(to_detail);
                    }
                }
            });
        }




    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food f = m_listFood.get(position);
        holder.FoodName.setText(f.getName());
        holder.FoodDescription.setText(f.getDescription());
        holder.FoodPrice.setText(f.getPrice());
        //Picasso.get().load(m_listFood.get(position).get_url()).fit().into(holder.FoodIcon);
        Glide.with(m_context).load(f.getUri()).into(holder.FoodIcon);
    }


    public int getItemCount() {
        return m_listFood.size();
    }


    public void setListFood(ArrayList<Food> m_listFood){
        this.m_listFood = m_listFood;
    }



}
