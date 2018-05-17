package com.gachat.main.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.AppUtils;
import com.gachat.generator.config.DaoQuery;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.ui.login.activity.LoginActivity;
import com.gachat.main.util.JumpToActivityUtil;
import com.gachat.main.util.manager.ActivityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    
    private String[] permissions =new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> mPermissionList;
    private Handler mHandler;
    private AlertDialog mPermissionDialog;

    @Override
    public int setLayoutResourceID() { return R.layout.activity_splash; }

    @Override
    public void initView() {
        WindowManager.LayoutParams params=getWindow().getAttributes();
//        params.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;  //始终隐藏，触摸屏幕时也不出现
        params.systemUiVisibility= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;  //隐藏了，触摸屏幕时出现
        getWindow().setAttributes(params);
        mHandler= new Handler();
        mPermissionList=new ArrayList<>();
    }

    @Override
    public void initOperate(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionList.clear();
            for (String permission : permissions) {
                if ((ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)) {
                    mPermissionList.add(permission);
                }
            }
            if (mPermissionList.isEmpty()) {
                delayTime();
            }else {
                String[] permis = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(SplashActivity.this, permis, 1);
            }
        }else {
            delayTime();
        }
    }

    //后续会去掉数据库记录登录用户登录状态
    void delayTime(){
        mHandler.postDelayed(() -> {
            if (DaoQuery.queryUserlistSize() > 0) {
                if (Objects.requireNonNull(DaoQuery.queryUserbean()).getIsLogin()) {
                    JumpToActivityUtil.jumpNoParams(SplashActivity.this, MainActivity.class, true);
                }
            }else {
                JumpToActivityUtil.jumpNoParams(SplashActivity.this, LoginActivity.class, true);
            }
        },500);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:{
                 int size=grantResults.length;
                 boolean PREMISS=false,PASSED=false;
                 Log.i(TAG, "onRequestPermissionsResult: 1"+"\t\tsize："+size);
                 for (int i = 0; i < size; i++) {
                     if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                         //判断是否勾选禁止后不再询问
                         boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissions[i]);
                         if (showRequestPermission) {  //重新申请权限
                             Log.i(TAG, "onRequestPermissionsResult: 重新申请");
//                                 ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
                             onStart();
                             return;
                         } else {    //已经禁止
                             PREMISS=true;
                         }
                     }else {
                         PREMISS=false;
                         PASSED=true;
                     }
                 }

                if (PREMISS) {
                    if (mPermissionDialog != null) {
                        mPermissionDialog=null;
                    }
                     showPermissionDialog();
                 }
                if (PASSED) {  delayTime();  }
                break;
            }
        }
    }


    /**
     * 不再提示权限 时的展示对话框
     */
    private void showPermissionDialog() {
        Log.i(TAG, "showPermissionDialog: ");
        mPermissionDialog = new AlertDialog.Builder(this)
                .setMessage("已禁用权限，请手动授予")
                .setPositiveButton("设置", (dialog, which) -> {
                    Uri packageURI = Uri.parse("package:" + AppUtils.getAppPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivity(intent);
                    dialog.cancel();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    onStart();
                    dialog.cancel();
                })
                .create();
        mPermissionDialog.setCanceledOnTouchOutside(false);
        mPermissionDialog.show();
    }
    

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        if (mPermissionList !=null) {
            mPermissionList.clear();
            mPermissionList=null;
        }
        if (mPermissionDialog != null) {
            mPermissionDialog=null;
        }
        if (mHandler != null) {
            mHandler=null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (!Constant.IsFirstIn()) {
                Log.i("onKeyDown", "onKeyDown: SplashActivity");
                ActivityManager.getInstance().exitSystem();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
