package com.example.hungrystomach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hungrystomach.Detail_Activity;
import com.example.hungrystomach.Model.Comment;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context m_context;
    private ArrayList<Comment> m_listComment;

    public CommentAdapter(Context context, ArrayList<Comment> comment_list) {
        this.m_context = context;
        this.m_listComment = comment_list;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(m_context).inflate(R.layout.layout_item_rated, parent, false);
        return new CommentViewHolder(v);
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder{
        public TextView RatingComment;
        public RatingBar RatingStar;

        public CommentViewHolder (View itemView){
            super(itemView);
            RatingComment = itemView.findViewById(R.id.rated_comment);
            RatingStar = itemView.findViewById(R.id.rated_bar);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment c = m_listComment.get(position);
        Log.d("Comment_debug", c.getComment());
        holder.RatingComment.setText(c.getComment());
        holder.RatingStar.setRating(c.getRating());

    }

    public int getItemCount() {
        return m_listComment.size();
    }

}
