package com.example.nunezjonathan_poc.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.nunezjonathan_poc.R;
import com.example.nunezjonathan_poc.adapters.FeedingPagerAdapter;
import com.example.nunezjonathan_poc.interfaces.FeedingActivityListener;
import com.example.nunezjonathan_poc.ui.viewModels.FeedingViewModel;
import com.google.android.material.tabs.TabLayout;

public class FeedingFragment extends Fragment {

    private FeedingActivityListener mListener;
    private SharedPreferences sharedPrefs;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof FeedingActivityListener) {
            mListener = (FeedingActivityListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_feeding, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.activities_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_log) {
            mListener.viewLog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            sharedPrefs = getActivity().getSharedPreferences("currentChild", Context.MODE_PRIVATE);
            int tabPosition = sharedPrefs.getInt("feedingTab", 0);

            FeedingPagerAdapter pagerAdapter = new FeedingPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            ViewPager viewPager = view.findViewById(R.id.pager);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(tabPosition, false);

            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (getActivity() != null) {
                        sharedPrefs.edit().putInt("feedingTab", tab.getPosition()).apply();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

    }
}
