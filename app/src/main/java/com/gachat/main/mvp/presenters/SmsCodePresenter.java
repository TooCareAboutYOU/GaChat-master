package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.SmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.SmsCodeModel;


public class SmsCodePresenter {

    private SmsCodeModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public SmsCodePresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new SmsCodeModel();
        Log.i("RegisterPhoneFragment", "Register  Presenter: ");
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void getSmsCode(String phoneNum){
        this.mModel.getSmsCode(phoneNum, new OnPresenterListener.OnModelListener<BaseBean<SmsCodeBean>>() {
            @Override
            public void onSuccess(BaseBean<SmsCodeBean> bean) {
                if (bean != null) {
                    if (SmsCodePresenter.this.mView != null) {
                        SmsCodePresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (SmsCodePresenter.this.mView != null) {
                    SmsCodePresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
