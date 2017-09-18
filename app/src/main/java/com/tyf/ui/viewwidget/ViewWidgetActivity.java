package com.tyf.ui.viewwidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tyf.ui.R;

import java.util.ArrayList;
import java.util.List;

public class ViewWidgetActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    //启动该Activity
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ViewWidgetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_widget);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout.setupWithViewPager(mViewPager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ListViewFragment());
        fragments.add(new RecyclerFragment());
        mViewPager.setAdapter(new ViewWidgetAdapter(getSupportFragmentManager(), fragments));
    }

    class ViewWidgetAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public ViewWidgetAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getClass().getName();
        }
    }
}
