package com.gachat.main.ui.login.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.LoginBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.UpdateUserData;
import com.gachat.main.mvp.presenters.LoginPresenter;
import com.gachat.main.ui.MainActivity;
import com.gachat.main.util.JumpToActivityUtil;
import com.gachat.main.util.MyUtils;
import com.gachat.main.util.manager.ActivityManager;

import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements OnPresenterListener.OnViewListener<BaseBean<LoginBean>> {

    public static final String TAG = "LoginActivity";

    private EditText mEtPhoneNum;
    private EditText mEtPwd;
    private LinearLayout mLlLogin;

    private LoginPresenter mPresenter;

    @Override
    public int setLayoutResourceID() { return R.layout.activity_login; }

    @Override
    public void initView() {
        Log.i(TAG, "initView: ");
        mPresenter=new LoginPresenter(this);
        mEtPhoneNum=findViewById(R.id.et_phoneNum);
        mEtPwd=findViewById(R.id.et_pwd);
    }
    @SuppressLint("CheckResult")
    @Override
    public void initOperate(Bundle savedInstanceState) {
        mLlLogin=findViewById(R.id.ll_login);
    }

    @OnClick({R.id.tv_forgetPwd,R.id.tv_register,R.id.ll_login})
    void ClickLister(View v){
        switch (v.getId()){
            case R.id.tv_forgetPwd:{
                JumpToActivityUtil.jumpNoParams(LoginActivity.this,ForgetPasswordActivity.class,false);
                break;
            }
            case R.id.tv_register:{
                JumpToActivityUtil.jumpNoParams(LoginActivity.this,RegisterActivity.class,false);
                break;
            }
            case R.id.ll_login:{
                requestLogin();
                break;
            }
        }
    }

    private void requestLogin() {
        String mobile=mEtPhoneNum.getText().toString().trim();
        String pwd=mEtPwd.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShort("手机号不能为空");
            return;
        }

        if (!MyUtils.checkMobileNumber(mobile)) {
            ToastUtils.showShort("手机号码错误!");
            mEtPhoneNum.setText("");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            ToastUtils.showShort("密码不能为空");
            return;
        }
        mLlLogin.setEnabled(false);
        mPresenter.getLogin(mobile,pwd);
    }

    @Override
    public void onSuccess(BaseBean<LoginBean> result) {
        Log.i(TAG, "activity onSuccess: "+result.toString());
        if (result.getCode() == 0 && result.getResult() != null) {
            if (result.getResult().getUser() != null){
//                UpdateUserData.getInstance().insertUserData(result);
                UpdateUserData.getInstance().saveUserData(result);
                JumpToActivityUtil.jumpNoParams(LoginActivity.this, MainActivity.class, true);

            }
        }else {
            if (result.getError() != null && result.getError().getMessage().size() > 0){
                for (String s : result.getError().getMessage()) {
                    ToastUtils.showShort(s);
                }
            }
        }
        mLlLogin.setEnabled(true);
    }

    @Override
    public void onFailed(Throwable e) {
        Log.i(TAG, "activity  onFailed: "+e.getMessage());
        mLlLogin.setEnabled(true);
        ToastUtils.showShort(e.getMessage());
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (!Constant.IsFirstIn()) {
                ActivityManager.getInstance().exitSystem();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
