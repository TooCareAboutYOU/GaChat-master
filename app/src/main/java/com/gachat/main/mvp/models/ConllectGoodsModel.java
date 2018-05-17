package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class ConllectGoodsModel extends BaseModel {

    private static final String TAG = "";

    private Subscription mSubscription;

    public void sendGoods(String username,String mobile,String address ,String user_asset ,OnPresenterListener.OnModelListener<BaseBean<MessageBean>> listener){
        mSubscription = UserAPI.sendGoods(username,mobile,address,user_asset, new Observer<BaseBean<MessageBean>>() {
            @Override
            public void onCompleted() {
                Log.w(TAG, "ConllectGoodsModel onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    if (listener != null) {
                        Log.w(TAG, "ConllectGoodsModel onError");
                        listener.onFailed(e);
                    }
                });

            }

            @Override
            public void onNext(BaseBean<MessageBean> bean) {
                new Handler().post(() -> {
                    if (listener != null) {
                        Log.w(TAG, "ConllectGoodsModel  onNext"+bean.toString());
                        listener.onSuccess(bean);
                    }
                });
            }
        });
        if (mSubscription != null) {
            Log.w(TAG, "ConllectGoodsModel add mSubscription: ");
            addCompositeSubscription(mSubscription);
        }
    }

    public void close(){
        if (mSubscription != null){
            Log.w(TAG, "ConllectGoodsModel close mSubscription: ");
            delCompositeSubscription();
        }
    }
}
