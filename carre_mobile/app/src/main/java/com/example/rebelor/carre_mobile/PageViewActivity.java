package com.example.rebelor.carre_mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.rebelor.carre_mobile.fragments.MyHealthRecordsFragment;
import com.example.rebelor.carre_mobile.fragments.RiskEvidenceGraphFragment;
import com.example.rebelor.carre_mobile.fragments.AbouFragment;
import com.example.rebelor.carre_mobile.fragments.UserGoalsFragment;

import java.util.ArrayList;
import java.util.List;

public class PageViewActivity extends  AppCompatActivity{

    MyPageAdapter pageAdapter;
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view);

        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(pageAdapter);


    }


    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(AbouFragment.newInstance("Fragment 1"));
        fList.add(MyHealthRecordsFragment.newInstance("Fragment 2"));
        fList.add(RiskEvidenceGraphFragment.newInstance("Fragment 3"));
        fList.add(UserGoalsFragment.newInstance("Fragment 4"));

        return fList;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }



}
