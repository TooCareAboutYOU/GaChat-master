package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.RepairModel;
import com.gachat.main.ui.login.activity.LoginActivity;


public class RepairPresenter {

    private static final String TAG = "LoginActivity";

    private RepairModel mModel;
    private OnPresenterListener.OnViewListener<BaseBean<MessageBean>> mView;

    public RepairPresenter(OnPresenterListener.OnViewListener<BaseBean<MessageBean>> view) {
        this.mView = view;
        this.mModel=new RepairModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void sendRepair(int roomid,int reason){
        Log.i(LoginActivity.TAG, "checkSmsCode: ");
        this.mModel.sendRepair(roomid,reason, new OnPresenterListener.OnModelListener<BaseBean<MessageBean>>() {
            @Override
            public void onSuccess(BaseBean<MessageBean> bean) {
                if (bean != null) {
                    if (RepairPresenter.this.mView != null) {
                        Log.i(TAG, "RepairPresenter onSuccess: "+bean.toString());
                        RepairPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (RepairPresenter.this.mView != null) {
                    Log.i(TAG, "RepairPresenter onFailed");
                    RepairPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
