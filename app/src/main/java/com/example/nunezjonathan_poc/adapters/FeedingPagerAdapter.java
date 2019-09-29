package com.example.nunezjonathan_poc.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.nunezjonathan_poc.ui.fragments.BottleFragment;
import com.example.nunezjonathan_poc.ui.fragments.NurseFragment;

import java.util.ArrayList;
import java.util.List;

public class FeedingPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<Fragment>(){{
        add(new NurseFragment());
        add(new BottleFragment());
    }};

    private final List<String> fragmentTitlesList = new ArrayList<String>(){{
        add("Nurse");
        add("Bottle");
    }};

    public FeedingPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitlesList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
