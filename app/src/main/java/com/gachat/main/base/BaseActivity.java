package com.gachat.main.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.gachat.main.R;
import com.gachat.main.beans.UserBean;
import com.gachat.main.mvp.models.UpdateUserData;
import com.gachat.main.util.manager.ActivityManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity { //implements NetWorkBroadCastReceiver.onNetWorkListener {

    protected void onCreateViewBefore(){}
    protected abstract int setLayoutResourceID();
    protected abstract void initView();
    protected abstract void initOperate(Bundle savedInstanceState);


//    private NetWorkBroadCastReceiver mNetWorkReciver;

    private Unbinder unbinder;

    public Toolbar getToolbar(){ return findViewById(R.id.toolbar_base); }
    public ImageView getBack(){ return findViewById(R.id.tootlbar_iv_back); }
    public TextView getToolbarTitle(){ return findViewById(R.id.tootlbar_iv_title); }


    public void setBackIcon(int backIcon){
        if (getToolbar() != null) {
            getBack().setImageResource(backIcon);
            getBack().setOnClickListener(v -> {onBackPressed();});
        }
    }

    public void setToolbarTitle(CharSequence title){
        if (getToolbarTitle() != null) {
            getToolbarTitle().setText(title);
        }else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    public void setToolbarTitle(int title){
        if (getToolbarTitle() != null) {
            getToolbarTitle().setText(title);
        }else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    public void setToolbarBackgroundColor(int colorResource){
        if (getToolbar() != null) {
            getToolbar().setBackgroundColor(colorResource);
        }
    }

    public void setToolbarBackgroundResource(int resId){
        if (getToolbar() != null) {
            getToolbar().setBackgroundResource(resId);
        }
    }

    public UserBean UserData() {
//        if (DaoQuery.queryUserlistSize() > 0) {
//            if (DaoQuery.queryUserbean() != null) {
//                return DaoQuery.queryUserbean();
//            }
//        }
//        return null;
        return UpdateUserData.getUserData();
    }


    @Override
    public void onContentChanged(){
        super.onContentChanged();
        initView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onCreateViewBefore();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceID());
        unbinder=ButterKnife.bind(this);
        ActivityManager.getInstance().addActivity(this);
        initOperate(savedInstanceState);
        if (getToolbarTitle() != null) {
            getToolbarTitle().setText(getTitle().toString());
        }
    }

//    private  LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onResume() {
        super.onResume();
//        Log.i(TAG, "BaseActivity onResume: ");
//        mNetWorkReciver=new NetWorkBroadCastReceiver();
//        IntentFilter intentFilter=new IntentFilter();
//        intentFilter.addAction(NetWorkBroadCastReceiver.ACTION);
//        mNetWorkReciver.setOnNetWorkListener(this);
        //本地广播
//        localBroadcastManager=LocalBroadcastManager.getInstance(this);
//        this.registerReceiver(mNetWorkReciver,intentFilter);
//        localBroadcastManager.registerReceiver(mNetWorkReciver,intentFilter);
//        localBroadcastManager.sendBroadcast(new Intent());
//        UpdateFunGO.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.i(TAG, "BaseActivity onPause: ");
//        localBroadcastManager.unregisterReceiver(mNetWorkReciver);
//        this.unregisterReceiver(mNetWorkReciver);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.i(TAG, "BaseActivity onStop: ");
//        UpdateFunGO.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
//        Log.i(TAG, "BaseActivity onDestroy: ");
        //activity销毁时，提示系统回收
        System.gc();
        ActivityManager.getInstance().removeActivity(this);
    }

//    @Override
//    public void onConnected(boolean isConnect) {
//        Log.d(TAG, "BaseActivity onConnected: "+isConnect);
//    }

}
