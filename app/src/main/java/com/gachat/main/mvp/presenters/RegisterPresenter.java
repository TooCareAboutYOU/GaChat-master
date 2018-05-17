package com.gachat.main.mvp.presenters;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.RegisterBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.RegisterModel;


public class RegisterPresenter {

    private RegisterModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public RegisterPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new RegisterModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void checkSmsCode(String mobile,String password,String gender,int age,int character,String username){
        this.mModel.getRegister(mobile,password,gender,age,character,username, new OnPresenterListener.OnModelListener<BaseBean<RegisterBean>>() {
            @Override
            public void onSuccess(BaseBean<RegisterBean> bean) {
                if (bean != null) {
                    if (RegisterPresenter.this.mView != null) {
                        RegisterPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (RegisterPresenter.this.mView != null) {
                    RegisterPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }


}
