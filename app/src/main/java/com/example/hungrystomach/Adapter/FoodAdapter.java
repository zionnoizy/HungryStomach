package com.example.hungrystomach.Adapter;


import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {

        f = m_listFood.get(position);
        holder.FoodName.setText(f.get_name());
        //holder.FoodDescription.setText(f.get_description());
        //holder.FoodPrice.setText("USD. " + f.get_price());
        //Glide.with(m_context).load(f.get_img()).into(holder.FoodImageView); //load(Uri.parse(f.get_img()
        Picasso.get().load(f.get_imgurl()).into(holder.FoodImageView);

    }


    public class FoodViewHolder extends RecyclerView.ViewHolder{
        public ImageView FoodImageView;
        public TextView FoodName;
        public TextView FoodDescription;
        public TextView FoodPrice;

        public FoodViewHolder (View itemView){
            super(itemView);
            FoodImageView = itemView.findViewById(R.id.thumbnail);
            FoodName = itemView.findViewById(R.id.foodname);
            //FoodDescription = itemView.findViewById(R.id.tv_des);
            //FoodPrice = itemView.findViewById(R.id.tv_price);
            m_auth = FirebaseAuth.getInstance();
        }
    }

    public ArrayList<Food> getListFood() {
        return m_listFood;
    }

    public void setListFood(ArrayList<Food> listFood) {
        this.m_listFood = listFood;
    }

    @Override
    public int getItemCount() {
        return getListFood().size();
    }

    public void setUploads(ArrayList<Food> uploads) {
        this.m_listFood = uploads;
        this.notifyDataSetChanged();
    }

    /*
    @NonNull
    @Override
    public FoodAdapterHolder onCreateViewHolder(int position, @NonNull ViewGroup parent, int viewType) {
        View v = parent;
        Food f = m_food.get(position);
        if(v == null) {
            //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            v = inflater.inflate(R.layout.list, null);
        }

        if(f != null){
            TextView et_fname = (TextView) v.findViewById(R.id.et_fname);
            TextView et_fdesc = (TextView) v.findViewById(R.id.et_fdesc);
            TextView et_fprice = (TextView) v.findViewById(R.id.et_fprice);
            if(et_fname != null)
                et_fname.setText("Name: " + c.getId());
            if(et_fdesc != null)
                et_fdesc.setText("Description: " + c.getId());
            if(et_fprice != null)
                et_fprice.setText("Price: " + c.getId());
         }
         return v;
        }
    }
    */

}
