package com.gachat.main.mvp.presenters;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.CheckSmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.ResetPwdModel;


public class ResetPwdPresenter {

    private ResetPwdModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public ResetPwdPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new ResetPwdModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void resetPwd(String mobile,String capcha,String password){
        this.mModel.resetpwd(mobile,capcha,password, new OnPresenterListener.OnModelListener<BaseBean<CheckSmsCodeBean>>() {
            @Override
            public void onSuccess(BaseBean<CheckSmsCodeBean> bean) {
                if (bean != null) {
                    if (ResetPwdPresenter.this.mView != null) {
                        ResetPwdPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (ResetPwdPresenter.this.mView != null) {
                    ResetPwdPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
