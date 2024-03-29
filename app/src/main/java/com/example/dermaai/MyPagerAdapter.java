package com.example.dermaai;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 3;
    private int[] imageResources = {
            R.drawable.dr1,
            R.drawable.dr3,
            R.drawable.dr4
    };
    private String[] titles = {
            "Dr. A Sharma",
            "Dr. Geet Shetty",
            "Dr. SK Khan"
    };
    private String[] details = {
            "\uD83D\uDCCC MBBS, FRCS London\n\n\uD83D\uDCDE Phone-9632587410\n\n\uD83D\uDCCD Location-BK Block, Delhi",
            "\uD83D\uDCCC MBBS, Orthopaedics Spl\n\n\uD83D\uDCDE Phone-1236589740\n\n\uD83D\uDCCD Location-Maniktala Kolkata",
            "\uD83D\uDCCC MBBS, Dermatologist\n\n\uD83D\uDCDE Phone-6985231470\n\n\uD83D\uDCCD Location-Sovabazar, Chennai"
    };

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Return a new fragment instance with the image resource ID
        return MyFragment.newInstance(titles[position], imageResources[position], details[position]);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
