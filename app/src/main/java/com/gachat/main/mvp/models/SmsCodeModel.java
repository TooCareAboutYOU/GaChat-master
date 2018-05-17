package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.SmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class SmsCodeModel extends BaseModel {

    private static final String TAG = "SmsCodeModel";
    private Subscription mSubscription;
    
    public void getSmsCode(String phoneNum, OnPresenterListener.OnModelListener<BaseBean<SmsCodeBean>> listener) {
        mSubscription = UserAPI.getSmsCode(phoneNum, new Observer<BaseBean<SmsCodeBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    Log.i(TAG, "onError run: ");
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<SmsCodeBean> smsCodeBeanBaseBean) {
                new Handler().post(() -> {
                    if (listener != null) {
                        listener.onSuccess(smsCodeBeanBaseBean);
                    }
                });
            }
        });
        if (mSubscription != null) {
            Log.i(TAG, "add mSubscription: ");
            addCompositeSubscription(mSubscription);
        }
    }

    public void close(){
        if (mSubscription != null){
            Log.i(TAG, "del mSubscription: ");
            delCompositeSubscription();
        }
    }
}
