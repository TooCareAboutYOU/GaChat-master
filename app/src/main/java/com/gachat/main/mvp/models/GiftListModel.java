package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.QueryType;
import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.GiftListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class GiftListModel extends BaseModel {

    private static final String TAG = "GetDolls";

    private Subscription mSubscription;

    public void getUserGiftlist(QueryType type, OnPresenterListener.OnModelListener<BaseBean<GiftListBean>> listener){
        mSubscription = UserAPI.getUserGiftlist(type, new Observer<BaseBean<GiftListBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "GiftListModel onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    Log.i(TAG, "GiftListModel onError");
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<GiftListBean> bean) {
                new Handler().post(() -> {
                    Log.i(TAG, "GiftListModel  onNext"+bean.toString());
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
