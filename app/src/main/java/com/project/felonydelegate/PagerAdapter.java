package com.project.felonydelegate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int behave;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        behave = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                Report report = new Report();
                return report;
            }
            case 1:{
                MapsFragment mapsFragment = new MapsFragment();
                return mapsFragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return behave;
    }
}
