package com.gachat.main.mvp.models;

import android.os.Handler;
import android.util.Log;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.RechargeListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class GetRechargePriceListModel extends BaseModel {

    private static final String TAG = "";

    private Subscription mSubscription;

    public void getRechargePriceListModel(OnPresenterListener.OnModelListener<BaseBean<RechargeListBean>> listener){
        mSubscription = UserAPI.getRechargePriceList(new Observer<BaseBean<RechargeListBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "GetRechargePriceListModel onCompleted: ");

            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    Log.i(TAG, "GetRechargePriceListModel onError");
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<RechargeListBean> bean) {
                new Handler().post(() -> {
                    Log.i(TAG, "GetRechargePriceListModel  onNext"+bean.toString());
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
