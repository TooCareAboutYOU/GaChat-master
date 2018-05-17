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

public class RepairModel extends BaseModel {

    private static final String TAG = "LoginActivity";

    private Subscription mSubscription;

    public void sendRepair(int roomid,int reason ,OnPresenterListener.OnModelListener<BaseBean<MessageBean>> listener){
        mSubscription = UserAPI.sendRepair(roomid,reason, new Observer<BaseBean<MessageBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "RepairModel onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    Log.i(TAG, "RepairModel onError");
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<MessageBean> bean) {
                new Handler().post(() -> {
                    Log.i(TAG, "RepairModel  onNext"+bean.toString());
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
