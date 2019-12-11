package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Model.Request;
import com.example.hungrystomach.Model.ShoppingCart;
import com.example.hungrystomach.Model.User;
import com.example.hungrystomach.R;
import com.example.hungrystomach.Status_Update_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private Context m_context;
    private List<Request> m_listRequest;
    public static final String EXTRA_POSITION = "NoImage";
    public static final String EXTRA_RANDOM_KEY = "NoRandomKey";
    public static final String EXTRA_BUYER_UID = "NoBuyerUID";
    String requester;
    public RequestAdapter(Context context, List<Request> request_list) {
        this.m_context = context;
        this.m_listRequest = request_list;
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(m_context).inflate(R.layout.layout_item_request, parent, false);
        return new RequestViewHolder(v);
    }


    public class RequestViewHolder extends RecyclerView.ViewHolder{
        private TextView RequestDate;
        private TextView RequestPrice;
        private ImageView RequestItemImage;
        private List<ShoppingCart> RequestItem;
        private TextView RequestNum;
        private TextView Requester;

        public RequestViewHolder (View itemView){
            super(itemView);
            RequestPrice = itemView.findViewById(R.id.request_price);
            RequestDate = itemView.findViewById(R.id.request_date);
            RequestNum = itemView.findViewById(R.id.display_request);
            Requester = itemView.findViewById(R.id.request_from);
            RequestItemImage = (ImageView)itemView.findViewById(R.id.cart_thumbnail);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestAdapter.RequestViewHolder holder, int position) {
        Request request = m_listRequest.get((holder.getAdapterPosition()));
        holder.RequestDate.setText(request.getRequestDate());
        holder.RequestPrice.setText("$ " + String.valueOf(request.getGrand_total()));
        holder.RequestNum.setText("Request#" + String.valueOf(position+1));

        final String num = String.valueOf(request.getRequest_entry_no());
        final String random_key = String.valueOf(request.getRandomkey());
        final String buyer_uid = String.valueOf(request.getBuyer_uid());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent detailreq = new Intent(m_context, Status_Update_Activity.class);
                detailreq.putExtra(EXTRA_POSITION, num);
                detailreq.putExtra(EXTRA_RANDOM_KEY, random_key);
                detailreq.putExtra(EXTRA_BUYER_UID, buyer_uid);
                Log.d("RA_Debug", num + random_key + buyer_uid);
                m_context.startActivity(detailreq);
            }
        });


        //get requester name
        DatabaseReference find = FirebaseDatabase.getInstance().getReference().child("users").child(request.getBuyer_uid());
        find.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                requester = u.getUsername();
                holder.Requester.setText("requester:   " + requester);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    public int getItemCount() {
        return m_listRequest.size();
    }


    public void setListRequest(ArrayList<Request> request_list){
        this.m_listRequest = request_list;
    }


}