package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import com.example.hungrystomach.Model.Comment;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.Model.UnRating;
import com.example.hungrystomach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private Context m_context;
    private ArrayList<UnRating> m_listCart;

    DatabaseReference dbRef;
    String my_uid;
    String food_name;
    String get_random_key;

    public RatingAdapter(Context context, ArrayList<UnRating> cart_list) {
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
                    //find position
                    int position = getAdapterPosition();
                    UnRating unrating_fd = m_listCart.get(position);
                    final String random_key = unrating_fd.getRandom_key();
                    final String name = unrating_fd.getName();



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
                                Log.d("Rating_debug", "+" + RatingBar.getRating());
                                Save_Rating(RatingBar.getRating(), RatingComment.getText().toString(), random_key, name); //food_name needed
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
        UnRating unRating = m_listCart.get(holder.getAdapterPosition());
        my_uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
        food_name = unRating.getName();
        get_random_key = unRating.getRandom_key();


        Glide.with(m_context).load(unRating.getUrl()).into(holder.RatingIcon);
    }

    @Override
    public int getItemCount() {
        return m_listCart.size();
    }


    public void Save_Rating(final float r, String comment, String random_key, String food_name){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("all_uploaded_image").child(random_key);
        Log.d("Rating_debug", "+" + r + "/" + random_key);
        Query query = mDatabase.orderByChild("rating");

        query.addValueEventListener(new ValueEventListener() {
            boolean processDone = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food u = dataSnapshot.getValue(Food.class);
                Log.d("Rating_debug", u.getRating() + "+" + processDone);
                float get_cur_rating = u.getRating();
                if (get_cur_rating == 0.0 && !processDone) {
                    mDatabase.child("rating").setValue(r);
                    u.setRating(r);
                    processDone = true;
                }
                else if (!processDone){
                    mDatabase.child("rating").setValue(r / 5);
                    u.setRating(r/5);
                    processDone = true;
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        delete_rating(food_name);

        DatabaseReference create_comment = FirebaseDatabase.getInstance().getReference().child("comment").child(random_key);
        Comment one_comment = new Comment(r, comment);
        create_comment.push().setValue(one_comment);

    }

    public void delete_rating(String food_name){
        final DatabaseReference delete_rateditem = FirebaseDatabase.getInstance().getReference("unrate").child(my_uid).child(food_name);
        delete_rateditem.removeValue();
    }


}