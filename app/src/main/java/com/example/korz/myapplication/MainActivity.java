package com.example.korz.myapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.korz.ScrollableViewPagerIndicator.ViewPageIndicator;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPageIndicator viewPageIndicator;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimplePagerAdapter simplePagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(simplePagerAdapter);

        viewPageIndicator = findViewById(R.id.viewPageIndicator);
        viewPager.setCurrentItem(9);
        viewPageIndicator.setViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

    }

}
