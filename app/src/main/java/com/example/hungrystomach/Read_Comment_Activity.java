package com.example.hungrystomach;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungrystomach.Adapter.CommentAdapter;
import com.example.hungrystomach.Adapter.RequestAdapter;
import com.example.hungrystomach.Model.Comment;
import com.example.hungrystomach.Model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hungrystomach.Detail_Activity.READ_RANDOM_KEY;

public class Read_Comment_Activity extends AppCompatActivity {
    ArrayList<Comment> list_comemnt;
    CommentAdapter adapter;
    RecyclerView recyclerView;

    String my_uid;

    String item_random_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_comments);

        recyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        item_random_key = getIntent().getStringExtra(READ_RANDOM_KEY);
        loadAllComment();
    }

    private void loadAllComment(){
        LinearLayoutManager lm = new LinearLayoutManager(Read_Comment_Activity.this);
        recyclerView.setLayoutManager(lm);
        list_comemnt = new ArrayList<>();

        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comment").child(item_random_key);


        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_comemnt.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Comment all_comment = snapshot.getValue(Comment.class);
                    list_comemnt.add(all_comment);
                    adapter = new CommentAdapter(Read_Comment_Activity.this, list_comemnt);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
