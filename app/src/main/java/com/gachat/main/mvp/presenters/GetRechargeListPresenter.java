package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.RechargeListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.GetRechargePriceListModel;
import com.gachat.main.ui.login.activity.LoginActivity;


public class GetRechargeListPresenter {

    private static final String TAG = "";

    private GetRechargePriceListModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public GetRechargeListPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new GetRechargePriceListModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void getRechargeListPresenter(){
        Log.i(LoginActivity.TAG, "checkSmsCode: ");
        this.mModel.getRechargePriceListModel(new OnPresenterListener.OnModelListener<BaseBean<RechargeListBean>>() {
            @Override
            public void onSuccess(BaseBean<RechargeListBean> bean) {
                if (bean != null) {
                    if (GetRechargeListPresenter.this.mView != null) {
                        Log.i(TAG, "GetRechargeListPresenter onSuccess: "+bean.toString());
                        GetRechargeListPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (GetRechargeListPresenter.this.mView != null) {
                    Log.i(TAG, "GetRechargeListPresenter onFailed");
                    GetRechargeListPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
