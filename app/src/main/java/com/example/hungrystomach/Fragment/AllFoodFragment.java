package com.example.hungrystomach.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Detail_Activity;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//implements View.OnClickListener
public class AllFoodFragment extends Fragment{
    private ArrayList<Food> entries;
    private DatabaseReference ref;
    private FirebaseDatabase dbr;
    FoodAdapter adapter;
    public Button btn_detail;

    public AllFoodFragment() {
        //empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contactsView = inflater.inflate(R.layout.fragment_all_food, container, false);

        RecyclerView recycler_view = (RecyclerView) contactsView.findViewById(R.id.recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(lm);

        entries = new ArrayList<Food>();
        adapter = new FoodAdapter(getActivity(), entries);

        recycler_view.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("all_uploaded_image");
        ref.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Food data = dataSnapshot.getValue(Food.class);
                entries.add(data);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //btn_detail = (Button) contactsView.findViewById(R.id.fooddetail);
        return contactsView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.contactsView = view;
    }
    */

    /*
    private void populateList() {
        RecyclerView recycler_view = (RecyclerView) contactsView.findViewById(R.id.recycler_view);
        FoodAdapter<Food> saveAdapter;
        saveAdapter = new SaveListAdapter();
        recycler_view.setAdapter(saveAdapter);
    }
    */


}
