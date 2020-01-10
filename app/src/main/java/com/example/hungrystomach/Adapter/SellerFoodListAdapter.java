package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Model.BuyerFoodList;
import com.example.hungrystomach.Model.SellerFoodList;
import com.example.hungrystomach.Notification.Sender;
import com.example.hungrystomach.R;

import java.util.ArrayList;

public class SellerFoodListAdapter extends RecyclerView.Adapter<SellerFoodListAdapter.SellerFoodListViewHolder> {
    private Context m_context;
    private ArrayList<SellerFoodList> m_listFood;

    public SellerFoodListAdapter(Context context, ArrayList<SellerFoodList> buyer_food_list) {
        this.m_context = context;
        this.m_listFood = buyer_food_list;
    }

    @NonNull
    @Override
    public SellerFoodListAdapter.SellerFoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ordered_food, parent, false);
        return new SellerFoodListAdapter.SellerFoodListViewHolder(v);
    }


    public class SellerFoodListViewHolder extends RecyclerView.ViewHolder {
        public ImageView SFLIMAGE;
        public TextView SFLNAME;
        public TextView SFLPRICE;
        public TextView SFLQUANTITY;

        public SellerFoodListViewHolder(View itemView) {
            super(itemView);
            SFLIMAGE = (ImageView) itemView.findViewById(R.id.orderfood_thumbnail);
            SFLNAME = itemView.findViewById(R.id.orderfood_name);
            SFLPRICE = itemView.findViewById(R.id.orderfood_price);
            SFLQUANTITY = itemView.findViewById(R.id.orderfood_quantity);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SellerFoodListAdapter.SellerFoodListViewHolder holder, int position) {
        SellerFoodList sfl = m_listFood.get(position);
        Glide.with(m_context).load(sfl.getUrl()).into(holder.SFLIMAGE);
        holder.SFLNAME.setText(sfl.getName());
        holder.SFLPRICE.setText(" $" + sfl.getProduct_price());
        holder.SFLQUANTITY.setText("x " + sfl.getQuantity());
    }

    public int getItemCount() {
        return m_listFood.size();
    }
}
