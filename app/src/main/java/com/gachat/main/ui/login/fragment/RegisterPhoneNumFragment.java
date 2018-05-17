package com.gachat.main.ui.login.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseBean;
import com.gachat.main.base.BaseFragment;
import com.gachat.main.beans.CheckSmsCodeBean;
import com.gachat.main.beans.SmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.presenters.CheckSmsCodePresenter;
import com.gachat.main.mvp.presenters.SmsCodePresenter;
import com.gachat.main.ui.login.activity.RegisterActivity;
import com.gachat.main.util.MyUtils;
import com.gachat.main.util.SharedPreferencesHelper;

import java.lang.ref.WeakReference;

import butterknife.OnClick;


public class RegisterPhoneNumFragment extends BaseFragment {

    private static final String TAG = "MyRegisterActivity";

    EditText mEtInputPhoneNum;
    EditText mEtInputVerificationCode;
    TextView mTvRequestVerificationCode;
    EditText mEtInputNewPwd;
    CheckBox mCbRegisterKnowledge;
    TextView mTvRegisterKnowledge;
    RelativeLayout mRlNextStep;

    private boolean isRunning = false;

    private SmsCodePresenter mSmsCodePresenter;
    private CheckSmsCodePresenter mCheckSmsCodePresenter;

    @SuppressLint("StaticFieldLeak")
    private static RegisterPhoneNumFragment instance;
    public RegisterPhoneNumFragment (){}
    public static synchronized RegisterPhoneNumFragment getInstance() {
         if (instance == null) {
             instance = new RegisterPhoneNumFragment();
         }
        return instance;
    }

    private WeakReference<RegisterActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = new WeakReference<>((RegisterActivity) context);
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.layout_register_phone_num_step1;
    }

    @Override
    public void initView(View view) {
        setToolbarTitle(R.string.txt_register);
        setBackIcon(R.drawable.icon_back);
        setBackPrevious();
        mEtInputPhoneNum=view.findViewById(R.id.et_inputPhoneNum);
        mEtInputVerificationCode=view.findViewById(R.id.et_inputVerificationCode);
        mTvRequestVerificationCode=view.findViewById(R.id.tv_requstVerificationCode);
        mEtInputNewPwd=view.findViewById(R.id.et_inputNewPwd);
        mCbRegisterKnowledge=view.findViewById(R.id.cb_registerKnowledge);
        mTvRegisterKnowledge=view.findViewById(R.id.tv_registerKnowledge);
        mRlNextStep=view.findViewById(R.id.rl_nextStep);
        

        if (mActivity.get().isFromActivity()) {
            Log.i(TAG, "initView: ");
            mEtInputPhoneNum.setText("");
            mEtInputVerificationCode.setText("");
            mEtInputNewPwd.setText("");
            mCbRegisterKnowledge.setChecked(false);
            mRlNextStep.setBackgroundResource(R.drawable.login_btn_login_disabled);
        }

        mSmsCodePresenter = new SmsCodePresenter(new GetSmsCode());
        mCheckSmsCodePresenter = new CheckSmsCodePresenter(new CheckSmsCode());
        mTvRegisterKnowledge.setText("<<注册须知>>");
        mRlNextStep.setEnabled(false);
        mCbRegisterKnowledge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "onCheckedChanged: ");
                String phoneNum = mEtInputPhoneNum.getText().toString().trim();
                String smsCode = mEtInputVerificationCode.getText().toString().trim();
                String pwd = mEtInputNewPwd.getText().toString().trim();
                if (check(phoneNum, smsCode, pwd)) {
                    mCbRegisterKnowledge.setChecked(false);
                    return;
                }
                mRlNextStep.setBackgroundResource(R.drawable.login_btn_login_selected);
                mRlNextStep.setEnabled(true);
            }else {
                mRlNextStep.setBackgroundResource(R.drawable.login_btn_login_disabled);
                mRlNextStep.setEnabled(false);
            }

        });
    }

    @OnClick({R.id.tv_requstVerificationCode, R.id.rl_nextStep})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_requstVerificationCode: {
                getSmsCode();
                break;
            }
            case R.id.rl_nextStep: {
               checkSmsCode();
                break;
            }
        }
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            mTvRequestVerificationCode.setText((millisUntilFinished / 1000) + "s");
            isRunning = true;
        }

        @Override
        public void onFinish() {
            mTvRequestVerificationCode.setEnabled(true);
            mTvRequestVerificationCode.setTextColor(getResources().getColor(R.color.colorBlueLight));
            mTvRequestVerificationCode.setText(R.string.txt_retryVCode);
            isRunning = false;
        }
    };

    private void getSmsCode() {
        String phoneNum = mEtInputPhoneNum.getText().toString().trim();
        if (phoneNum.isEmpty()) {
            ToastUtils.showShort("手机号码不能为空");
            return;
        }

        if (!MyUtils.checkMobileNumber(phoneNum)) {
            mEtInputPhoneNum.setText("");
            ToastUtils.showShort("手机号码错误");
            return;
        }

        if (mSmsCodePresenter == null) {
            mSmsCodePresenter=new SmsCodePresenter(new GetSmsCode());
            return;
        }
        mSmsCodePresenter.getSmsCode(phoneNum);

        timer.start();
        mTvRequestVerificationCode.setEnabled(false);
        mTvRequestVerificationCode.setTextColor(getResources().getColor(R.color.colorGrayDark));

    }

    void checkSmsCode() {
        String phoneNum = mEtInputPhoneNum.getText().toString().trim();
        String smsCode = mEtInputVerificationCode.getText().toString().trim();
        String pwd = mEtInputNewPwd.getText().toString().trim();

        if (check(phoneNum, smsCode, pwd)) return;

        if (mCheckSmsCodePresenter == null) {
            mCheckSmsCodePresenter=new CheckSmsCodePresenter(new CheckSmsCode());
        }

        mCheckSmsCodePresenter.checkSmsCode(phoneNum,smsCode,pwd);
    }

    private boolean check(String phoneNum, String smsCode, String pwd) {
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("手机号码不能为空");
            return true;
        } else if (!MyUtils.isMobileNO(phoneNum)) {
            ToastUtils.showShort("手机号码错误");
            return true;
        }else  if (TextUtils.isEmpty(smsCode)) {
            ToastUtils.showShort("验证码不能为空");
            return true;
        } else if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("密码不能为空");
            return true;
        }else if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtils.showShort(R.string.txt_newPassWord_hint);
            mEtInputNewPwd.setText("");
            return true;
        }
        return false;
    }


    private class GetSmsCode implements OnPresenterListener.OnViewListener<BaseBean<SmsCodeBean>> {
        @Override
        public void onSuccess(BaseBean<SmsCodeBean> result) {
                Log.i(TAG, "SmsCodeBean  onSuccess: " + result.toString());
                if (result.getCode() == 0 &&  result.getResult() != null){
                    mEtInputVerificationCode.setText("");
                    mEtInputVerificationCode.setText(result.getResult().getCaptcha());
                }

                if (result.getError() != null) {
                    int size = result.getError().getMessage().size();
                    if (result.getCode() == 10001 ) {  // 该手机号码已注册
                        for (int i = 0; i < size; i++) {
                            ToastUtils.showShort(result.getError().getMessage().get(i));
                        }
                        mEtInputVerificationCode.setText("");
                        timer.onFinish();
                    }
                    if (result.getCode() == 10008){ //  该手机号还没获取验证码
                        for (int i = 0; i < size; i++) {
                            ToastUtils.showShort(result.getError().getMessage().get(i));
                        }
                    }
                }
        }

        @Override
        public void onFailed(Throwable throwable) {
            ToastUtils.showShort(throwable.getMessage());
        }
    }

    private class CheckSmsCode implements OnPresenterListener.OnViewListener<BaseBean<CheckSmsCodeBean>> {
        @Override
        public void onSuccess(BaseBean<CheckSmsCodeBean> result) {
            Log.i(TAG, "CheckSmsCodeBean onSuccess: " + result.toString());
            if (result.getCode() == 0 && result.getResult() != null){
                SharedPreferencesHelper.getInstance().setStringValue(Constant.USER_MOBILE,mEtInputPhoneNum.getText().toString().trim());
                SharedPreferencesHelper.getInstance().setStringValue(Constant.USER_PASSWORD,mEtInputNewPwd.getText().toString().trim());
                mActivity.get().gotoPerfectInfoFragment();
            }
        }

        @Override
        public void onFailed(Throwable throwable) {
            Log.i(TAG, "CheckSmsCodeBean onFailed: " + throwable.getMessage());
            ToastUtils.showShort(throwable.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView: PhoneNum");
        mActivity.get().setFromActivity(false);
        if (isRunning) {
            timer.cancel();
        }
        if (mSmsCodePresenter != null) {
            mSmsCodePresenter.detachView();
            mSmsCodePresenter=null;
        }
        if (mCheckSmsCodePresenter != null){
            mCheckSmsCodePresenter.detachView();
            mCheckSmsCodePresenter=null;
        }
        super.onDestroyView();
        mActivity.get().mTransaction.remove(this);
    }
}
