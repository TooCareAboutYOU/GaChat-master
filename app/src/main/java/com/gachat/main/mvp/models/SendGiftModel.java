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

public class SendGiftModel extends BaseModel {

    private static final String TAG = "SendGiftModel";

    private Subscription mSubscription;

    public void sendGift(long toUserId,int dollId ,OnPresenterListener.OnModelListener<BaseBean<MessageBean>> listener){
        mSubscription = UserAPI.sendGiftToUser(toUserId, dollId, new Observer<BaseBean<MessageBean>>() {
            @Override
            public void onCompleted() {
                Log.w(TAG, TAG+" onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    if (listener != null) {
                        Log.w(TAG, TAG+" onError");
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<MessageBean> bean) {
                new Handler().post(() -> {
                    if (listener != null) {
                        Log.w(TAG, TAG+" onNext"+bean.toString());
                        listener.onSuccess(bean);
                    }
                });
            }
        });
        if (mSubscription != null) {
            Log.w(TAG, TAG+" add mSubscription: ");
            addCompositeSubscription(mSubscription);
        }
    }

    public void close(){
        if (mSubscription != null){
            Log.w(TAG, TAG+" close mSubscription: ");
            delCompositeSubscription();
        }
    }
}
