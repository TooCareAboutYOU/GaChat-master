package com.gachat.main.ui.chat.switchroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends BaseActivity {

    private static final String TAG = "ChatsActivity";

    private ViewPager mViewpager;
    private List<Fragment> mList;
    private ViewPagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;
    private RoomFragment mRoomFragment=RoomFragment.getInstance();


    @Override
    protected int setLayoutResourceID() {   return R.layout.activity_chats;   }

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        Log.i(TAG, "initView: ");
        mList=new ArrayList<>();
        mFragmentManager=getSupportFragmentManager();
        mViewpager=findViewById(R.id.viewPager);
        mPagerAdapter = new ViewPagerAdapter(mFragmentManager);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {

        mViewpager.setAdapter(mPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //在这里不处理position的原因是因为getItem方法在
        //instantiateItem方法中调用。只要在调用前处理
        //position即可，以免重复处理
        @Override
        public Fragment getItem(int position) {  return mList.get(position);  }

        @Override
        public int getCount() {   return Integer.MAX_VALUE;   }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //处理position。让数组下标落在[0,fragmentList.size)中，防止越界
            position = position % mList.size();
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position).getView());
        }
    }

    @Override
    protected void onDestroy() {
        if (mList != null) {
            mList.clear();
            mList=null;
        }
        super.onDestroy();
    }
}
