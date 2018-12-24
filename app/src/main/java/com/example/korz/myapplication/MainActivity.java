package com.example.korz.myapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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

        final SimplePagerAdapter simplePagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        simplePagerAdapter.setCount(10);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(simplePagerAdapter);

        viewPageIndicator = findViewById(R.id.viewPageIndicator);
        viewPager.setCurrentItem(1);
        viewPageIndicator.setViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i > simplePagerAdapter.getCount() - 3) {
                    simplePagerAdapter.setCount(simplePagerAdapter.getCount() + 10);
                    //viewPageIndicator.setViewPager(viewPager);
                    viewPageIndicator.updateCount(viewPager);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

}
