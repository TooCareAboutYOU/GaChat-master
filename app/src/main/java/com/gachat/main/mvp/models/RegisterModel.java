package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.RegisterBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class RegisterModel extends BaseModel {

    private static final String TAG = "SmsCodeModel";
    private Subscription mSubscription;
    
    public void getRegister(String mobile,String password,String gender,int age,int character,String username, OnPresenterListener.OnModelListener<BaseBean<RegisterBean>> listener){
        mSubscription = UserAPI.getRegister(mobile, password, gender, age, character, username, new Observer<BaseBean<RegisterBean>>() {
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
            public void onNext(BaseBean<RegisterBean> registerBeanBaseBean) {

                new Handler().post(() -> {
                    Log.i(TAG, "onNext run: "+registerBeanBaseBean.toString());
                    if (listener != null) {
                        listener.onSuccess(registerBeanBaseBean);
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
