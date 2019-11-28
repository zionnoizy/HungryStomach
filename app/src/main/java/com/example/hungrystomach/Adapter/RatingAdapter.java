package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Model.Rating;
import com.example.hungrystomach.R;
import com.example.hungrystomach.Rating_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private Context m_context;
    private ArrayList<Rating> m_listCart;

    DatabaseReference dbRef;
    String my_uid;
    public RatingAdapter(Context context, ArrayList<Rating> cart_list) {
        this.m_context = context;
        this.m_listCart = cart_list;
    }

    @NonNull
    @Override
    public RatingAdapter.RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(m_context).inflate(R.layout.layout_item_rating, parent, false);
        return new RatingViewHolder(v);
    }


    public class RatingViewHolder extends RecyclerView.ViewHolder {
        private RatingBar RatingBar;
        private TextView RatingComment;
        private ImageView RatingIcon;
        public Button RatingBtn;
        private TextView RatingScale;
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        DatabaseReference rate = FirebaseDatabase.getInstance().getReference().child("rate");
        //DatabaseReference usr_uid = cartListRef.child(m_auth.getCurrentUser().getUid());

        public RatingViewHolder(View itemView) {
            super(itemView);
            RatingBar = itemView.findViewById(R.id.review_rating);
            RatingComment = itemView.findViewById(R.id.review_comment);
            RatingIcon = itemView.findViewById(R.id.review_thumbnail);
            RatingBtn = itemView.findViewById(R.id.review_sumit);
            RatingScale = itemView.findViewById(R.id.review_scale);

            RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    RatingScale.setText(String.valueOf(v));
                    switch ((int) ratingBar.getRating()) {
                        case 1:
                            RatingScale.setText("Undercook");
                            break;
                        case 2:
                            RatingScale.setText("Taste Bad");
                            break;
                        case 3:
                            RatingScale.setText("SoSo");
                            break;
                        case 4:
                            RatingScale.setText("Great");
                            break;
                        case 5:
                            RatingScale.setText("Tasty!");
                            break;
                        default:
                            RatingScale.setText("");
                    }
                }
            });
            RatingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RatingComment.getText().toString().isEmpty()) {
                        Toast.makeText(m_context, "Your Comment Empty", Toast.LENGTH_LONG).show();
                    } if(RatingBar.getRating() == 0.0) {
                        Toast.makeText(m_context, "Your Rating Is Empty", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        builder.setTitle("Submit");
                        builder.setMessage("Are you going to submit the reivew?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                save_rating(RatingBar.getRating());
                                RatingComment.setText("");
                                RatingBar.setRating(0);
                                Toast.makeText(m_context, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.RatingViewHolder holder, int position) {
        Rating rating = m_listCart.get(holder.getAdapterPosition());
        my_uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("unrate").child(my_uid).child(String.valueOf(holder.getAdapterPosition()));
        //Glide.with(m_context).load(rating.getIcon_url()).into(holder.RatingIcon);
    }

    @Override
    public int getItemCount() {
        return m_listCart.size();
    }

    public void save_rating(float r){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("all_uploaded_image");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("rating", r);

        mDatabase.child("-Luk55E0wNUE9bXk_pZ0").updateChildren(childUpdates);
    }

    public void delete_rating(){
        final DatabaseReference delete_rateditem = FirebaseDatabase.getInstance().getReference("unrate").child(my_uid);
        delete_rateditem.removeValue();
    }
}