package com.example.korz.myapplication;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimplePagerAdapter extends FragmentPagerAdapter {
    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    int count;

    @Override
    public Fragment getItem(int i) {
        Color color = new Color();
        return PageFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "page " + (position);
    }
}
