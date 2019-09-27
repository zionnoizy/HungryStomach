package com.example.hungrystomach.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.R;
import com.google.firebase.database.core.Context;


import java.util.ArrayList;
import java.util.List;

//CardViewHolder
/*
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>
public class UserAdapter extends RecyclerView.ListAdapter {
    private List<User> all_users;
    private ImageView m_profile_image;
    //public static final int LOADING_ITEM = 0;

    public UserAdapter(ImageView m_profile_image){
        all_users = new ArrayList();
        this.m_profile_image = mProfileImage;
    }

    public void addUsers(List<User> all_users){
        int user_pos = all_users.size();
        this.all_users.addAll(products);
        notifyItemRangeInserted(user_pos, all_users.size());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //if(viewType == LOADING_ITEM){
        //    View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
        //    return new LoadingHolder(row);
        //}

    }

    //Hold View Of Users
    private class User_Holder extends RecyclerView.ViewHolder{
        ImageView iv_user_thumb;
        TextView tv_username,

        public User_Holder(View itemView){
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            iv_user_thumb = itemView.findViewById(R.id.profile_image);
        }
    }

    private class LoadingHolder extends RecyclerView.ViewHolder{
        public LoadingHolder(View itemView){
            super(itemView);
        }
    }
}
*/
