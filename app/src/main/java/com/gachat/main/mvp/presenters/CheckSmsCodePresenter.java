package com.gachat.main.mvp.presenters;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.CheckSmsCodeBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.CheckSmsCodeModel;


public class CheckSmsCodePresenter {

    private CheckSmsCodeModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public CheckSmsCodePresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new CheckSmsCodeModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void checkSmsCode(String mobile,String capcha,String password){
        this.mModel.checkSmsCode(mobile,capcha,password, new OnPresenterListener.OnModelListener<BaseBean<CheckSmsCodeBean>>() {
            @Override
            public void onSuccess(BaseBean<CheckSmsCodeBean> bean) {
                if (bean != null) {
                    if (CheckSmsCodePresenter.this.mView != null) {
                        CheckSmsCodePresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (CheckSmsCodePresenter.this.mView != null) {
                    CheckSmsCodePresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
