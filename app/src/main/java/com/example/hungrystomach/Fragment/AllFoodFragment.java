package com.example.hungrystomach.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Home_Activity;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


//show image with info, uploader
public class AllFoodFragment extends Fragment {
    private View contactsView;
    private RecyclerView recycler_view;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    private ArrayList<Food> entries;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    public AllFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsView = inflater.inflate(R.layout.fragment_all_food_item, container, false);

        init_adapter();


        //get_fb_data();
        //load_data_from_firebase();

        return contactsView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedUbstabceState){
        super.onViewCreated(view, savedUbstabceState);
        this.contactsView = view;
        get_fb_data();

    }
    private void get_fb_data(){
        myRef = mFirebaseDatabase.getReference("image");
        myRef.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                //Log.e("Count " ,"" +dataSnapshot.getChildrenCount());
                Food data = dataSnapshot.getValue(Food.class);
                entries.add(data);
                recycler_view.setAdapter(rvAdapter);
                //Log.e("Get Des", data.get_description());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            //
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            //
            }
        });
    }

    void init_adapter(){
        recycler_view = (RecyclerView) contactsView.findViewById(R.id.recycler_view); //1

        entries = new ArrayList<>();
        rvAdapter = new FoodAdapter(entries,getContext());
        recycler_view.setAdapter(rvAdapter);

        rvLayoutManager = new LinearLayoutManager(getActivity()); //2
        recycler_view.setLayoutManager(rvLayoutManager); //3
    }
}
