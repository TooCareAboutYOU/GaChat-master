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

public class SmsCodeResetPwdModel extends BaseModel {

    private static final String TAG = "SmsCodeResetPwdModel";
    
    private Subscription mSubscription;
    
    public void getSmsCodeResetPwd(String phoneNum, OnPresenterListener.OnModelListener<BaseBean<SmsCodeBean>> listener){
        mSubscription = UserAPI.getSmsCodeResetPwd(phoneNum, new Observer<BaseBean<SmsCodeBean>>() {
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
            public void onNext(BaseBean<SmsCodeBean> SmsCodeBean) {
                new Handler().post(() -> {
                    Log.i(TAG, "onNext run: "+SmsCodeBean.toString());
                    if (listener != null) {
                        listener.onSuccess(SmsCodeBean);
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
