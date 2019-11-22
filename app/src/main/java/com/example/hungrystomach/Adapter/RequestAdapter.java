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
import com.example.hungrystomach.Model.Request;
import com.example.hungrystomach.Model.ShoppingCart;
import com.example.hungrystomach.R;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private Context m_context;
    private ArrayList<Request> m_listRequest;

    public RequestAdapter(Context context, ArrayList<Request> request_list) {
        this.m_context = context;
        this.m_listRequest = request_list;
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(m_context);
        View itemv = inflater.inflate(R.layout.layout_item_request, parent, false);
        return new RequestViewHolder(itemv);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, int position) {
        Request sc = m_listRequest.get((holder.getAdapterPosition()));
        //holder.RequestItem.setText(sc.getProduct_price());
        //holder.RequestDate.setText(String.valueOf(sc.getRequestDate()));

        //Glide.with(m_context).load(sc.getImg_url()).into(holder.RequestItemImage);
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{
        private ImageView RequestItemImage;
        private List<ShoppingCart> RequestItem;
        private TextView RequestDate;

        public RequestViewHolder (View itemView){
            super(itemView);
            RequestItemImage = (ImageView)itemView.findViewById(R.id.cart_thumbnail);
            RequestItem = itemView.findViewById(R.id.cart_foodname);
            RequestDate = itemView.findViewById(R.id.cart_foodprice);
        }
    }


    public int getItemCount() {
        return m_listRequest.size();
    }


    public void setListCart(ArrayList<Request> request_list){
        this.m_listRequest = request_list;
    }


}