package com.gachat.main.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.event.ExitEvent;
import com.gachat.main.ui.home.CatchDollFragment;
import com.gachat.main.ui.home.GaChatFragment;
import com.gachat.main.ui.home.UserFragment;
import com.gachat.main.util.manager.ActivityManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @SuppressLint("StaticFieldLeak")
    protected static ViewPager viewPager;

    private List<Fragment> list = new ArrayList<>();
    private MenuItem menuItem;
    private BottomNavigationView mBottomNavigationView;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: ");
    }

    @Override
    public int setLayoutResourceID() { return R.layout.activity_main;}

    @Override
    public void initView() {
        viewPager =findViewById(R.id.viewpager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        list.add(new GaChatFragment());
        list.add(new CatchDollFragment());
        list.add(new UserFragment());
    }

    @Override
    public void initOperate(Bundle savedInstanceState) {
        new BottomNavigationViewHelper().disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setSelectedItemId(R.id.item_GaChat);
        viewPager.setCurrentItem(0);
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_GaChat:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.item_CatchDoll:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.item_user:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(position).setChecked(false);  // position  替换 0
                }
                menuItem = mBottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.item_GaChat:
                        menuItem.setIcon(R.drawable.tab_icon_kachat_selected);
                        resetToDefaultIconDoll();
                        resetToDefaultIconUser();
                        break;
                    case R.id.item_CatchDoll:
                        resetToDefaultIconGachat();
                        menuItem.setIcon(R.drawable.tab_icon_game_selected);
                        resetToDefaultIconUser();
                        break;
                    case R.id.item_user:
                        resetToDefaultIconGachat();
                        resetToDefaultIconDoll();
                        menuItem.setIcon(R.drawable.tab_icon_user_selected);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOffscreenPageLimit(2);  // 不为2 ButterKnife会失效
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private void resetToDefaultIconGachat() {
        mBottomNavigationView.getMenu().findItem(R.id.item_GaChat).setIcon(R.drawable.tab_icon_kachat_normal);
    }
    private void resetToDefaultIconDoll() {
        mBottomNavigationView.getMenu().findItem(R.id.item_CatchDoll).setIcon(R.drawable.tab_icon_game_normal);
    }

    private void resetToDefaultIconUser() {
        mBottomNavigationView.getMenu().findItem(R.id.item_user).setIcon(R.drawable.tab_icon_user_normal);
    }

    class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        void disableShiftMode(BottomNavigationView navigationView) {

            BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);

                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                    itemView.setShiftingMode(false);
                    itemView.setChecked(itemView.getItemData().isChecked());
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter{
        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public static void changepage(){  viewPager.setCurrentItem(1);  }

    //记录用户首次点击返回键的时间
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                Log.i("onKeyDown", "onKeyDown: MainActivity");
                ExitEvent.exitApp();
                ActivityManager.getInstance().exitSystem();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (list != null){
            list.clear();
            list=null;
        }
        super.onDestroy();
//        ApplicationHelper.getInstance().getRefWatcher().watch(this);
    }
}
