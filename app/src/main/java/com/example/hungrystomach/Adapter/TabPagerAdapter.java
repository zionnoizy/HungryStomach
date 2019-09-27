package com.example.hungrystomach.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.hungrystomach.Fragment.AllFoodFragment;
import com.example.hungrystomach.Fragment.FoodFragment2;
import com.example.hungrystomach.Fragment.FoodFragment3;
import com.example.hungrystomach.Fragment.UserFragment;


public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabCount = 3;
    private Context mContext;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
        //this.mContext = context;
        //private String tabTitiles[] = new String[]{"All Food", "American", "Chinese", "Indian", "Japanese", "Korean", "Mediterranean", "Dessert", "Drink"}
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new AllFoodFragment();
        else if(position == 1)
            return new FoodFragment2();
         else if (position == 2)
            return new FoodFragment3();

        else
            return null;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return tabCount;

    }

    /*
    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_usefulinfo);
            case 1:
                return mContext.getString(R.string.category_places);
            case 2:
                return mContext.getString(R.string.category_food);
            case 3:
                return mContext.getString(R.string.category_nature);
            default:
                return null;
        }
    }
    */

    /* chatroom_v
    public Fragment Chat_getItem(int c_position) {
        if (c_position == 0)
            return new ChatFragment();
        else if(c_position == 1)
            return new UserFragment();

        else
            return null;
    }
    */

}
