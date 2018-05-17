package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.api.QueryType;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.GiftListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.GiftListModel;


public class GiftListPresenter {

    private static final String TAG = "GetDolls";
    
    private GiftListModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public GiftListPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new GiftListModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void getUserGiftlist(QueryType type){
        this.mModel.getUserGiftlist(type, new OnPresenterListener.OnModelListener<BaseBean<GiftListBean>>() {
            @Override
            public void onSuccess(BaseBean<GiftListBean> bean) {
                if (bean != null) {
                    if (GiftListPresenter.this.mView != null) {
                        Log.i(TAG, "GiftListPresenter onSuccess: "+bean.toString());
                        GiftListPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (GiftListPresenter.this.mView != null) {
                    Log.i(TAG, "GiftListPresenter onFailed");
                    GiftListPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
