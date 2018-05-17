package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.LoginBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.LoginModel;


public class LoginPresenter {

    private static final String TAG = "LoginPresenter";

    private LoginModel mModel;
    private OnPresenterListener.OnViewListener<BaseBean<LoginBean>> mView;

    public LoginPresenter(OnPresenterListener.OnViewListener<BaseBean<LoginBean>> view) {
        this.mView = view;
        this.mModel=new LoginModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void getLogin(String mobile,String password){
        Log.e(TAG, "requestLogin: ");
        this.mModel.login(mobile,password, new OnPresenterListener.OnModelListener<BaseBean<LoginBean>>() {
            @Override
            public void onSuccess(BaseBean<LoginBean> bean) {
                if (LoginPresenter.this.mView != null) {
                    Log.e(TAG, "LoginPresenter onSuccess: "+bean.toString());
                    LoginPresenter.this.mView.onSuccess(bean);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (LoginPresenter.this.mView != null) {
                    Log.e(TAG, "LoginPresenter onFailed");
                    LoginPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
