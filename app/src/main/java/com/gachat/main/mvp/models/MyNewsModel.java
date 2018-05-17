package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.NewsBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class MyNewsModel extends BaseModel {

    private static final String TAG = "MyNewsActivity";

    private Subscription mSubscription;

    public void getMyNews(OnPresenterListener.OnModelListener<BaseBean<NewsBean>> listener){

        mSubscription = UserAPI.getMyNews(new Observer<BaseBean<NewsBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "MyNewsModel onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    Log.i(TAG, "MyNewsModel onError");
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<NewsBean> bean) {
                new Handler().post(() -> {
                    Log.i(TAG, "MyNewsModel  onNext"+bean.toString());
                    if (listener != null) {
                        listener.onSuccess(bean);
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
            Log.i(TAG, "close mSubscription: ");
            delCompositeSubscription();
        }
    }
}
