package com.example.hungrystomach.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.hungrystomach.Fragment.AllFoodFragment;
import com.example.hungrystomach.Fragment.FoodFragment2;
import com.example.hungrystomach.Fragment.FoodFragment3;
import com.example.hungrystomach.Fragment.UserFragment;


public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabCount = 3;
    private Context mContext;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm, TabPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabCount = numberOfTabs;
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


}
