package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.FeedBackBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class FeedBacksModel extends BaseModel {

    private static final String TAG = "FeedBacksModel";

    private Subscription mSubscription;

    public void sendFeedBacks(String content,String mobile ,OnPresenterListener.OnModelListener<BaseBean<FeedBackBean>> listener){
        mSubscription = UserAPI.sendFeedBacks(content,mobile, new Observer<BaseBean<FeedBackBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "FeedBacksModel onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    Log.i(TAG, "FeedBacksModel onError");
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<FeedBackBean> bean) {
                new Handler().post(() -> {
                    Log.i(TAG, "FeedBacksModel  onNext"+bean.toString());
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
