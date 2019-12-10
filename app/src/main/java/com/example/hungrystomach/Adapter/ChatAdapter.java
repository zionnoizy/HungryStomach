package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Model.Chat;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatRoomViewHolder> {

    private static final int MSG_RECEIVER_LEFT = 0;
    private static final int MSG_SENDER_RIGHT = 1;
    Context context;
    List<Chat> chatList;

    FirebaseUser find_usr;
    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }


    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_SENDER_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_sender_right, viewGroup, false);
            return new ChatRoomViewHolder(view);
        } else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_receiver_left, viewGroup, false);
            return new ChatRoomViewHolder(view);
        }
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder{
        TextView RoomMsg;
        //TextView RoomTime;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            RoomMsg = itemView.findViewById(R.id.sr_msg);
            //RoomTime = itemView.findViewById(R.id.sr_time);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder myHolder, final int i) {
        String msg = chatList.get(i).getMessage();
        String receiver = chatList.get(i).getReceiver();
        //String timeStamp = chatList.get(i).getTimes();

        //Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        //cal.setTimeInMillis(Long.parseLong(timeStamp));
        //String dateTime = DateFormat.format("dd/MM/yyyy hh:mm", cal).toString();

        myHolder.RoomMsg.setText(msg);
        //myHolder.RoomTime.setText(dateTime);

        //Glide.with(context).load(image_url).into(holder.FoodIcon);
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position){
        find_usr = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(find_usr.getUid()))
            return MSG_SENDER_RIGHT;
        else
            return MSG_RECEIVER_LEFT;

    }
}
