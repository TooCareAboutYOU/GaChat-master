package com.gachat.main.ui.user;

import android.os.Bundle;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;

public class DisclaimerActivity extends BaseActivity {


    @Override
    protected int setLayoutResourceID() {return R.layout.activity_disclaimer;}

    @Override
    protected void initView() {
        setBackIcon(R.drawable.icon_back);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {
    }
}
