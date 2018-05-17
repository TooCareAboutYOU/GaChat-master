package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.ConllectGoodsModel;


public class ConllectGoodsPresenter {

    private static final String TAG = "LoginActivity";

    private ConllectGoodsModel mModel;
    private OnPresenterListener.OnViewListener<BaseBean<MessageBean>> mView;

    public ConllectGoodsPresenter(OnPresenterListener.OnViewListener<BaseBean<MessageBean>> view) {
        this.mView = view;
        this.mModel=new ConllectGoodsModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void sendGoods(String username,String mobile,String address ,String user_asset){
        Log.e(TAG, "requestLogin: ");
        this.mModel.sendGoods(username,mobile,address,user_asset, new OnPresenterListener.OnModelListener<BaseBean<MessageBean>>() {
            @Override
            public void onSuccess(BaseBean<MessageBean> bean) {
                if (ConllectGoodsPresenter.this.mView != null) {
                    Log.e(TAG, "LoginPresenter onSuccess: "+bean.toString());
                    ConllectGoodsPresenter.this.mView.onSuccess(bean);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (ConllectGoodsPresenter.this.mView != null) {
                    Log.e(TAG, "LoginPresenter onFailed");
                    ConllectGoodsPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
