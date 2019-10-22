package com.example.hungrystomach.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Detail_Activity;
import com.example.hungrystomach.Fragment.AllFoodFragment;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;

import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context m_context;
    private ArrayList<Food> m_listFood;

    public FoodAdapter(Context context, ArrayList<Food> food_list) {
        this.m_context = context;
        this.m_listFood = food_list;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_fragment_all_food, parent, false);
        return new FoodViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder{
        public ImageView FoodIcon;
        public TextView FoodName;
        public TextView FoodDescription;
        public TextView FoodPrice;
        public Button Fooddetail;

        public static final String EXTRA_URL = "NoImage";
        public static final String EXTRA_NAME = "NoName";
        public static final String EXTRA_DES = "NoDes";
        public static final String EXTRA_PRICE = "NoPrice";

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

                        to_detail.putExtra(EXTRA_URL, clcikedItem.get_uri());
                        to_detail.putExtra(EXTRA_NAME, clcikedItem.get_name());
                        to_detail.putExtra(EXTRA_PRICE, clcikedItem.get_price());
                        to_detail.putExtra(EXTRA_DES, clcikedItem.get_description());

                        m_context.startActivity(to_detail);
                    }
                }
            });
        }




    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food f = m_listFood.get(position);
        holder.FoodName.setText(f.get_name());
        holder.FoodDescription.setText(f.get_description());
        holder.FoodPrice.setText(f.get_price());
        //Picasso.get().load(m_listFood.get(position).get_url()).fit().into(holder.FoodIcon);
        Glide.with(m_context).load(f.get_uri()).into(holder.FoodIcon);
    }


    public int getItemCount() {
        return m_listFood.size();
    }


    public void setListFood(ArrayList<Food> m_listFood){
        this.m_listFood = m_listFood;
    }
}
