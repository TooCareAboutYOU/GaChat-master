package com.gachat.main.mvp.models;

import android.os.Handler;

import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.DollBannerBean;
import com.gachat.main.beans.DollListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.network.BaseModel;

import rx.Observer;
import rx.Subscription;

public class GetDollPageDataModel extends BaseModel {

    private static final String TAG = "CatchDollFragment";

    private Subscription mSubscription;

    public void getDollBanners(OnPresenterListener.OnModelListener<BaseBean<DollBannerBean>> listener){
        mSubscription = UserAPI.getDollBanners(new Observer<BaseBean<DollBannerBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<DollBannerBean> bean) {
                new Handler().post(() -> {
                    if (listener != null) {
//                        Log.i(TAG, "onNext: "+bean);
                        listener.onSuccess(bean);
                    }
                });
            }
        });
        if (mSubscription != null) {
            addCompositeSubscription(mSubscription);
        }
    }


    public void getDollList(int page ,OnPresenterListener.OnModelListener<BaseBean<DollListBean>> listener){
        mSubscription= UserAPI.getDollList(page, new Observer<BaseBean<DollListBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                new Handler().post(() -> {
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                });
            }

            @Override
            public void onNext(BaseBean<DollListBean> bean) {
                new Handler().post(() -> {
                    if (listener != null) {
//                        Log.i(TAG, "onNext: "+bean);
                        listener.onSuccess(bean);
                    }
                });
            }
        });

        if (mSubscription != null) {
            addCompositeSubscription(mSubscription);
        }
    }

    public void close(){
        if (mSubscription != null){
            delCompositeSubscription();
        }
    }

}
