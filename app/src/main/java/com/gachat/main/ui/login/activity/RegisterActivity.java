package com.gachat.main.ui.login.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.ui.login.fragment.RegisterPerfectInfoFragment;
import com.gachat.main.ui.login.fragment.RegisterPhoneNumFragment;

public class RegisterActivity extends BaseActivity {

//    private FrameLayout mFlFragmentContainer;

   public FragmentTransaction mTransaction;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        setBackIcon(R.drawable.icon_back);
//        mFlFragmentContainer=findViewById(R.id.fl_fragmentContainer);
    }

    @Override
    protected void initOperate(Bundle savedInstanceState) {
        initRegisterFragment();
    }

    private void initRegisterFragment() {
        setFromActivity(true);
        mTransaction=getSupportFragmentManager().beginTransaction();
        mTransaction.add(R.id.fl_fragmentContainer, RegisterPhoneNumFragment.getInstance());
        mTransaction.commit();
    }

    public void gotoPerfectInfoFragment(){
        mTransaction=getSupportFragmentManager().beginTransaction();
        mTransaction.addToBackStack("RegisterPerfectInfoFragment");
        mTransaction.replace(R.id.fl_fragmentContainer, RegisterPerfectInfoFragment.getInstance());
        mTransaction.commit();
    }

    private boolean isFromActivity;

    public boolean isFromActivity() {  return isFromActivity;  }
    public void setFromActivity(boolean fromActivity) {  isFromActivity = fromActivity;  }
}
