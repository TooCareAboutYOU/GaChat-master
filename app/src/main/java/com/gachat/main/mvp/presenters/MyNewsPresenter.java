package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.NewsBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.MyNewsModel;


public class MyNewsPresenter {

    private static final String TAG = "MyNewsActivity";

    private MyNewsModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public MyNewsPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new MyNewsModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void getMyNews(){
        this.mModel.getMyNews(new OnPresenterListener.OnModelListener<BaseBean<NewsBean>>() {
            @Override
            public void onSuccess(BaseBean<NewsBean> bean) {
                if (bean != null) {
                    if (MyNewsPresenter.this.mView != null) {
                        Log.i(TAG, "MyNewsPresenter onSuccess: "+bean.toString());
                        MyNewsPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (MyNewsPresenter.this.mView != null) {
                    Log.i(TAG, "MyNewsPresenter onFailed");
                    MyNewsPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
