package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Detail_Activity;

import com.example.hungrystomach.Model.ShoppingCart;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context m_context;
    private ArrayList<ShoppingCart> m_listCart;
    double grandT=0;

    public CartAdapter(Context context, ArrayList<ShoppingCart> cart_list) {
        this.m_context = context;
        this.m_listCart = cart_list;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(m_context);
        View itemv = inflater.inflate(R.layout.layout_item_cart, parent, false);
        return new CartViewHolder(itemv);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public class CartViewHolder extends RecyclerView.ViewHolder{
        public ImageView CartFoodIcon;
        public TextView CartFoodName;
        public TextView CartFoodPrice;
        public TextView CartFoodQuantity;
        public TextView CartTTL;
        public TextView sub_total;
        public Button delete_item;
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
        DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());

        public CartViewHolder (View itemView){
            super(itemView);
            CartFoodIcon = (ImageView)itemView.findViewById(R.id.cart_thumbnail);
            CartFoodName = itemView.findViewById(R.id.cart_foodname);
            CartFoodPrice = itemView.findViewById(R.id.cart_foodprice);
            CartFoodQuantity = itemView.findViewById(R.id.quantity);
            CartTTL = (TextView)itemView.findViewById(R.id.total_price);
            sub_total = itemView.findViewById(R.id.sub_total);
            delete_item = itemView.findViewById(R.id.delete_item);
            /*
            //delete item
            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ShoppingCart get_deleting_item = m_listCart.get(position);

                    DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("shopping_cart");
                    String user_uid = m_auth.getCurrentUser().getUid();
                    String deleting_food_name = get_deleting_item.getTitle();

                    Query deleting_food_query = cartListRef.child(user_uid).orderByChild("title").equalTo(deleting_food_name); //might +id

                    deleting_food_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue(); //
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "Please Check Your Network Setting", databaseError.toException());
                        }
                    });
                }
            });
            */
        }




    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        ShoppingCart sc = m_listCart.get((holder.getAdapterPosition()));
        holder.CartFoodName.setText(sc.getProduct_name());
        holder.CartFoodPrice.setText(sc.getProduct_price());
        holder.CartFoodQuantity.setText(String.valueOf(sc.getQuantity()));

        double getPrice= Double.parseDouble(m_listCart.get(position).getProduct_price());
        int getQ = m_listCart.get(position).getQuantity();
        double sub_t = getPrice * getQ; //but cannot get total amount
        grandT += sub_t;

        holder.sub_total.setText(String.valueOf(sub_t));

        Glide.with(m_context).load(sc.getImg_url()).into(holder.CartFoodIcon);
    }


    public int getItemCount() {
        return m_listCart.size();
    }


    public void setListCart(ArrayList<ShoppingCart> m_listCart){
        this.m_listCart = m_listCart;
    }

    public double grandTotal(){
        double ttl = 0;
        for (int i=0; i<m_listCart.size(); ++i){
            double p = Double.parseDouble(m_listCart.get(i).getProduct_price());
            double q = m_listCart.get(i).getQuantity();
            double subT = p*q;
            ttl += subT;
        }
        return ttl;
    }

}
