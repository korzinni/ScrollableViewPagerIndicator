package com.example.korz.myapplication;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimplePagerAdapter extends FragmentPagerAdapter {
    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Color color = new Color();
        return PageFragment.newInstance(0);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "page " + (position);
    }
}
