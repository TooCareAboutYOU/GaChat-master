package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.FeedBackBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.FeedBacksModel;
import com.gachat.main.ui.login.activity.LoginActivity;


public class FeedBacksPresenter {

    private FeedBacksModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public FeedBacksPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new FeedBacksModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void sendFeedBacks(String content,String mobile){
        this.mModel.sendFeedBacks(content,mobile, new OnPresenterListener.OnModelListener<BaseBean<FeedBackBean>>() {
            @Override
            public void onSuccess(BaseBean<FeedBackBean> bean) {
                if (bean != null) {
                    if (FeedBacksPresenter.this.mView != null) {
                        Log.i(LoginActivity.TAG, "FeedBacksPresenter onSuccess: "+bean.toString());
                        FeedBacksPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (FeedBacksPresenter.this.mView != null) {
                    Log.i(LoginActivity.TAG, "FeedBacksPresenter onFailed");
                    FeedBacksPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
