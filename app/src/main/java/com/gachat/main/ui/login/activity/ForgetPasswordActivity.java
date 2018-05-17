package com.gachat.main.ui.login.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.CheckSmsCodeBean;
import com.gachat.main.beans.SmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.ResetPwdPresenter;
import com.gachat.main.mvp.presenters.SmsCodeResetPwdPresenter;
import com.gachat.main.util.JumpToActivityUtil;
import com.gachat.main.util.MyUtils;

import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class ForgetPasswordActivity extends BaseActivity {

    public static final String TAG = "ForgetPasswordActivity";

    private EditText mEtInputPhoneNum;
    private EditText mEtInputVerificationCode;
    private TextView mTvRequestVerificationCode;
    private EditText mEtInputNewPwd;
//    private LinearLayout mTvFinished;

    private boolean isRunning=false;
    private long count=60;
    private long period=1;

    private SmsCodeResetPwdPresenter mSmsCodeResetPwdPresenter;
    private ResetPwdPresenter mResetPwdPresenter;

    @Override
    public int setLayoutResourceID() { return R.layout.activity_forget_password; }

    @Override
    public void initView() {
        setBackIcon(R.drawable.icon_back);
        mSmsCodeResetPwdPresenter=new SmsCodeResetPwdPresenter(new GetSmsCode());
        mResetPwdPresenter=new ResetPwdPresenter(new ResetPwdStatus());
        mEtInputPhoneNum=findViewById(R.id.et_inputPhoneNum);
        mEtInputVerificationCode=findViewById(R.id.et_inputVerificationCode);
        mTvRequestVerificationCode=findViewById(R.id.tv_requstVerificationCode);
        mEtInputNewPwd=findViewById(R.id.et_inputNewPwd);
//        mTvFinished=findViewById(R.id.tv_finished);
    }

    @Override
    public void initOperate(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_requstVerificationCode,R.id.tv_finished})
    void onclick(View v){
        switch (v.getId()){
            case R.id.tv_requstVerificationCode:{
                getCode();
                break;
            }
            case R.id.tv_finished:{
                register();
                break;
            }
        }
    }

    private void getCode() {
        String phoneNum=mEtInputPhoneNum.getText().toString().trim();
        if (phoneNum.isEmpty()) {
            ToastUtils.showShort("手机号码不能为空");
            return;
        }

        if (!MyUtils.checkMobileNumber(phoneNum)) {
            ToastUtils.showShort("手机号码错误!");
            return;
        }
        timer();
        mSmsCodeResetPwdPresenter.getSmsCodeResetPwd(phoneNum);

        timer.start();
        mTvRequestVerificationCode.setEnabled(false);
        mTvRequestVerificationCode.setTextColor(getResources().getColor(R.color.colorGrayDark));
    }

    void register() {
        String phoneNum = mEtInputPhoneNum.getText().toString().trim();
        String smsCode = mEtInputVerificationCode.getText().toString().trim();
        String pwd = mEtInputNewPwd.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("手机号码不能为空");
            return;
        } else if (!MyUtils.checkMobileNumber(phoneNum)) {
            Toast.makeText(this, "手机号码错误!", Toast.LENGTH_SHORT).show();
            return;
        }else  if (TextUtils.isEmpty(smsCode)) {
            ToastUtils.showShort("验证码不能为空");
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }else if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtils.showShort(R.string.txt_newPassWord_hint);
            mEtInputNewPwd.setText("");
            return;
        }
        if (mResetPwdPresenter == null) {
            return;
        }

        mResetPwdPresenter.resetPwd(phoneNum,smsCode,pwd);
    }


    private class GetSmsCode implements OnPresenterListener.OnViewListener<BaseBean<SmsCodeBean>> {
        @Override
        public void onSuccess(BaseBean<SmsCodeBean> result) {
            Log.i(TAG, "GetSmsCode  onSuccess: " + result.toString());
            runOnUiThread(() -> {
                Log.i(TAG, "onSuccess: 0");
                switch (result.getCode()) {
                    case 0:{
                            if (result.getResult() != null) {
                                mEtInputVerificationCode.setText(result.getResult().getCaptcha());
                            }
                        break;
                    }
                    case 10010:{
                        Log.i(TAG, "onSuccess: 1");
                        if (result.getError() != null && result.getError().getMessage().size() > 0) {
                            Log.i(TAG, "onSuccess: 2"+result.getError().getMessage().get(0));
                            for (String s : result.getError().getMessage()) {
                                ToastUtils.showShort(s);
                            }
                        }
                        break;
                    }
                }
            });

        }
        @Override
        public void onFailed(Throwable throwable) {
            runOnUiThread(() -> Toast.makeText(ForgetPasswordActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private class ResetPwdStatus implements OnPresenterListener.OnViewListener<BaseBean<CheckSmsCodeBean>> {
        @Override
        public void onSuccess(BaseBean<CheckSmsCodeBean> result) {
            Log.i(TAG, "ResetPwdStatus onSuccess: "+result);
            if (result.getResult() != null) {
                runOnUiThread(() -> {
                    switch (result.getCode()) {
                        case 0:{   JumpToActivityUtil.jumpNoParams(ForgetPasswordActivity.this,LoginActivity.class,true);   break;   }
                        case 10010:{
                            if (result.getError() != null && result.getError().getMessage().size() > 0) {
                                for (String s : result.getError().getMessage()) {
                                    ToastUtils.showShort(s);
                                }
                            }
                            break;
                        }
                    }
                });
            }
        }
        @Override
        public void onFailed(Throwable throwable) {
            runOnUiThread(() -> Toast.makeText(ForgetPasswordActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void timer(){
        Observable.interval(period, TimeUnit.SECONDS)
                .take(count+1)
                .map(aLong -> count-aLong)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mTvRequestVerificationCode.setEnabled(false))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: "+d.isDisposed());
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(Long num) {
                        Log.i(TAG, "onNext: "+num);
                        mTvRequestVerificationCode.setText(num+"s");
                        isRunning=true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                        mTvRequestVerificationCode.setEnabled(true);
                        mTvRequestVerificationCode.setTextColor(getResources().getColor(R.color.colorBlueLight));
                        mTvRequestVerificationCode.setText(R.string.txt_retryVCode);
                        isRunning=false;
                    }
                }
        );
    }


    CountDownTimer timer=new CountDownTimer(60000,1000) {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            mTvRequestVerificationCode.setText((millisUntilFinished/1000)+"s");
            isRunning=true;
        }

        @Override
        public void onFinish() {
            mTvRequestVerificationCode.setEnabled(true);
            mTvRequestVerificationCode.setTextColor(getResources().getColor(R.color.colorBlueLight));
            mTvRequestVerificationCode.setText(R.string.txt_retryVCode);
            isRunning=false;
        }
    };

    @Override
    protected void onPause() {
        if (isRunning) {
            timer.cancel();
        }

        if (mSmsCodeResetPwdPresenter != null) {
            mSmsCodeResetPwdPresenter.detachView();
            mSmsCodeResetPwdPresenter=null;
        }

        if (mResetPwdPresenter != null) {
            mResetPwdPresenter.detachView();
            mResetPwdPresenter=null;
        }
        super.onPause();
    }
}
