package com.example.gallerylitepro.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.gallerylitepro.Fregment.FevouriteFragment;
import com.example.gallerylitepro.Fregment.GalleryFragment;
import com.example.gallerylitepro.Fregment.VideoFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 3;
    private final String[] tabTitles = new String[]{"Photos Gallery", "Videos Gallery", "Favourite Items"};

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GalleryFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new FevouriteFragment();
            default:
                return new GalleryFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
