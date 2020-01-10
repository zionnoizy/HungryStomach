package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Detail_Activity;
import com.example.hungrystomach.Model.BuyerFoodList;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;

import java.util.ArrayList;

public class BuyerFoodListAdapter extends RecyclerView.Adapter<BuyerFoodListAdapter.BuyerFoodListViewHolder> {
    private Context m_context;
    private ArrayList<BuyerFoodList> m_listFood;

    public BuyerFoodListAdapter(Context context, ArrayList<BuyerFoodList> buyer_food_list) {
        this.m_context = context;
        this.m_listFood = buyer_food_list;
    }

    @NonNull
    @Override
    public BuyerFoodListAdapter.BuyerFoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ordered_food, parent, false);
        return new BuyerFoodListAdapter.BuyerFoodListViewHolder(v);
    }


    public class BuyerFoodListViewHolder extends RecyclerView.ViewHolder {
        public ImageView BFLIMAGE;
        public TextView BFLNAME;
        public TextView BFLPRICE;
        public TextView BFLQUANTITY;

        public BuyerFoodListViewHolder(View itemView) {
            super(itemView);
            BFLIMAGE = (ImageView) itemView.findViewById(R.id.orderfood_thumbnail);
            BFLNAME = itemView.findViewById(R.id.orderfood_name);
            BFLPRICE = itemView.findViewById(R.id.orderfood_price);
            BFLQUANTITY = itemView.findViewById(R.id.orderfood_quantity);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BuyerFoodListAdapter.BuyerFoodListViewHolder holder, int position) {
        BuyerFoodList bfl = m_listFood.get(position);
        Glide.with(m_context).load(bfl.getUrl()).into(holder.BFLIMAGE);
        holder.BFLNAME.setText(bfl.getName());
        holder.BFLPRICE.setText(" $" + bfl.getProduct_price());
        holder.BFLQUANTITY.setText("x " + bfl.getQuantity());
    }

    public int getItemCount() {
        return m_listFood.size();
    }
}

