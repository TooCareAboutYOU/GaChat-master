package com.gachat.main.ui.chat.switchroom;

import android.annotation.SuppressLint;
import android.view.View;

import com.gachat.main.R;
import com.gachat.main.base.BaseFragment;

/**
 *
 */
public class RoomFragment extends BaseFragment {

    public RoomFragment(){}
    public static RoomFragment getInstance(){
        return RoomFragmentHolder.instance;
    }
    private static class RoomFragmentHolder{
        @SuppressLint("StaticFieldLeak")
        public static final RoomFragment instance=new RoomFragment();
    }

    @Override
    public int setLayoutResourceID() {  return R.layout.activity_chat_room;  }

    @Override
    public void initView(View view) {

    }
}
