package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.view.View;

import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

/**
 *再次遇见
 */

public class MeetAgainDialogFragment extends BaseDialogFragment {

    @SuppressLint("StaticFieldLeak")
    private static MeetAgainDialogFragment instance;
    public MeetAgainDialogFragment (){}
    public static synchronized MeetAgainDialogFragment getInstance() {
         if (instance == null) {
             instance = new MeetAgainDialogFragment();
         }
     return instance;
    }


    @Override
    public int setResLayoutId() {
        return R.layout.chat_meetagain_dialog;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.cancel).setOnClickListener(v -> dismiss());
    }

}
