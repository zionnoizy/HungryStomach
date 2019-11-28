package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.R;
import com.example.hungrystomach.RoomChat_Activity;
import com.example.hungrystomach.RoomUser_Activity;


import java.util.ArrayList;




public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context m_context;
    private ArrayList<User> all_users;
    public static final String EXTRA_HISUID = "NoHisUID";
    public UserAdapter(Context context, ArrayList<User> all_users) {
        this.m_context = context;
        this.all_users = all_users;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(m_context);
        View itemv = inflater.inflate(R.layout.layout_item_user, parent, false);
        return new UserViewHolder(itemv);
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{
        public ImageView UserIcon;
        public TextView Username;

        public UserViewHolder (View itemView){
            super(itemView);
            UserIcon = (ImageView)itemView.findViewById(R.id.usr_icon);
            Username = (TextView)itemView.findViewById(R.id.tv_username);
        }
    }


    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User usr = all_users.get(position);
        final String hisUID = usr.getUid();
        holder.Username.setText(usr.getUsername());
        if(usr.getIcon().equals("default_icon")){
            holder.UserIcon.setImageResource(R.drawable.default_icon);
        }
        else{
            Glide.with(m_context).load(usr.getIcon()).into(holder.UserIcon);
        }

        //click user
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(m_context, "welcome to chat room", Toast.LENGTH_LONG).show();
                Intent chatroom = new Intent(m_context, RoomChat_Activity.class);
                chatroom.putExtra(EXTRA_HISUID, hisUID);
                m_context.startActivity(chatroom);
            }
        });
    }

    public int getItemCount() {
        return all_users.size();
    }
}
