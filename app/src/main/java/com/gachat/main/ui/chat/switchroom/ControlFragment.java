package com.gachat.main.ui.chat.switchroom;

import android.annotation.SuppressLint;
import android.view.View;

import com.gachat.main.R;
import com.gachat.main.base.BaseFragment;

/**
 * 聊天室控制层
 */

public class ControlFragment extends BaseFragment {

    @SuppressLint("StaticFieldLeak")
    private static ControlFragment instance;
    public ControlFragment (){}
    public static synchronized ControlFragment getInstance() {
         if (instance == null) {
             instance = new ControlFragment();
         }
     return instance;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_control_chatroom;
    }

    @Override
    public void initView(View view) {

    }
}
