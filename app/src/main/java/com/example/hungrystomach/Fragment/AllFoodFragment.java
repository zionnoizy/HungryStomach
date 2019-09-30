package com.example.hungrystomach.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Adapter.FoodAdapter;
import com.example.hungrystomach.Model.Food;
import com.example.hungrystomach.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


//show image with info, uploader
public class AllFoodFragment extends Fragment {
    private View contactsView;
    private ArrayList<Food> entries;
    private DatabaseReference ref;
    private FirebaseDatabase dbr;
    private FoodAdapter adapter;

    private RecyclerView recycler_view;
    public AllFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contactsView = inflater.inflate(R.layout.home_main, container, false); //v

        recycler_view = (RecyclerView) contactsView.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        final FragmentActivity ga = getActivity(); //2a
        LinearLayoutManager lm = new LinearLayoutManager(ga); //2
        recycler_view.setLayoutManager(lm);

        entries = new ArrayList<Food>();

        ref = FirebaseDatabase.getInstance().getReference().child("all_uploaded_image");
        ref.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    //Log.e("Count " ,"" + dataSnapshot.getChildrenCount());
                    Food data = dataSnapshot.getValue(Food.class);
                    //data.setPrice(dataSnapshot.child("price").getValue().toString());
                    //data.setImageUrl(dataSnapshot.child("image_uri").getValue().toString());
                    entries.add(data);
                    //Log.e("Get All Description", data.get_price());
                }
                adapter = new FoodAdapter(getActivity(), entries);
                adapter.notifyDataSetChanged();
                adapter.setListFood(entries);


                 //3; Error
                recycler_view.setAdapter(adapter);
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
        return contactsView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedUbstabceState){
        super.onViewCreated(view, savedUbstabceState);
        this.contactsView = view;
    }
}
