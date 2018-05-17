package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.LoginBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class LoginModel extends BaseModel {

    private static final String TAG = "LoginModel";

    private Subscription mSubscription;

    public void login(String mobile,String password ,OnPresenterListener.OnModelListener<BaseBean<LoginBean>> listener){
        mSubscription = UserAPI.getLogin(mobile, password, new Observer<BaseBean<LoginBean>>() {
            @Override
            public void onCompleted() {
                Log.w(TAG, "LoginModel onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    if (listener != null) {
                        Log.w(TAG, "LoginModel onError");
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<LoginBean> bean) {
                new Handler().post(() -> {
                    if (listener != null) {
                        Log.w(TAG, "LoginModel  onNext"+bean.toString());
                        listener.onSuccess(bean);
                    }
                });
            }
        });
        if (mSubscription != null) {
            Log.w(TAG, "LoginModel add mSubscription: ");
            addCompositeSubscription(mSubscription);
        }
    }

    public void close(){
        if (mSubscription != null){
            Log.w(TAG, "LoginModel close mSubscription: ");
            delCompositeSubscription();
        }
    }
}
