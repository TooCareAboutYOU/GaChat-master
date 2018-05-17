package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.CheckSmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class CheckSmsCodeModel extends BaseModel {

    private static final String TAG = "CheckSmsCodeModel";
    private Subscription mSubscription;
    
    public void checkSmsCode(String mobile,String capcha,String password ,OnPresenterListener.OnModelListener<BaseBean<CheckSmsCodeBean>> listener){
        mSubscription = UserAPI.checkSmsCode(mobile, capcha, password, new Observer<BaseBean<CheckSmsCodeBean>>() {
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
            public void onNext(BaseBean<CheckSmsCodeBean> checkSmsCodeBeanBaseBean) {
                new Handler().post(() -> {
                    Log.i(TAG, "onNext run: "+checkSmsCodeBeanBaseBean.toString());
                    if (listener != null) {
                        listener.onSuccess(checkSmsCodeBeanBaseBean);
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
