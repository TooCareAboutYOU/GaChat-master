package com.gachat.main.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.ui.user.fragments.GetDollsFragment;
import com.gachat.main.ui.user.fragments.PostDollsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyDollsActivity extends BaseActivity {


    TabLayout mTabLayout;
    ViewPager mViewPager;

    private String[] tabs={"获取娃娃","失去娃娃"};
    private List<Fragment> fragments;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_my_dolls;
    }

    @Override
    protected void initView() {
        setBackIcon(R.drawable.icon_back);
        mTabLayout=findViewById(R.id.tabLayout);
        mViewPager=findViewById(R.id.viewPager);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {
        initTabLayout();
    }


    private void initTabLayout() {
        //MODE_FIXED标签栏不可滑动，各个标签会平分屏幕的宽度
//        mTabLayout.setTabMode(tabCount <= MOVABLE_COUNT ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);

        mTabLayout.addTab(mTabLayout.newTab().setText(tabs[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(tabs[1]));

        LinearLayout linearLayout = (LinearLayout) mTabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));

        fragments = new ArrayList<>();
        fragments.add(GetDollsFragment.newInstance());
        fragments.add(PostDollsFragment.newInstance());

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
