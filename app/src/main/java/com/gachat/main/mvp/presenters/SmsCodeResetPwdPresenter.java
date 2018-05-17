package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.SmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.SmsCodeResetPwdModel;
import com.gachat.main.ui.login.activity.ForgetPasswordActivity;


public class SmsCodeResetPwdPresenter {

    private SmsCodeResetPwdModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public SmsCodeResetPwdPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new SmsCodeResetPwdModel();
        Log.i(ForgetPasswordActivity.TAG, "Register  Presenter: ");
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void getSmsCodeResetPwd(String phoneNum){
        this.mModel.getSmsCodeResetPwd(phoneNum, new OnPresenterListener.OnModelListener<BaseBean<SmsCodeBean>>() {
            @Override
            public void onSuccess(BaseBean<SmsCodeBean> bean) {
                if (bean != null) {
                    if (SmsCodeResetPwdPresenter.this.mView != null) {
                        SmsCodeResetPwdPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (SmsCodeResetPwdPresenter.this.mView != null) {
                    SmsCodeResetPwdPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
