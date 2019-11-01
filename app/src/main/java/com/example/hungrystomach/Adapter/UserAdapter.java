package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.R;


import java.util.ArrayList;
import java.util.List;

//CardViewHolder


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context m_context;
    private List<User> all_users;
    double total_amounts;

    public UserAdapter(Context context, ArrayList<User> all_users) {
        this.m_context = context;
        this.all_users = all_users;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(m_context);
        View itemv = inflater.inflate(R.layout.layout_item_cart, parent, false);
        return new UserViewHolder(itemv);
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{
        public ImageView UserIcon;
        public TextView Username;

        public UserViewHolder (View itemView){
            super(itemView);
            UserIcon = (ImageView)itemView.findViewById(R.id.cart_thumbnail);
            Username = (TextView)itemView.findViewById(R.id.tv_username);
        }
    }


    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User usr = all_users.get(position);
        holder.Username.setText(usr.getUsername());
        if(usr.getIcon().equals("default_icon")){
            holder.UserIcon.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(m_context).load(usr.getIcon()).into(holder.UserIcon);
        }
    }


    public int getItemCount() {
        return all_users.size();
    }


    public void setListCart(ArrayList<User> m_listCart){
        this.all_users = m_listCart;
    }

}
