package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.DollBannerBean;
import com.gachat.main.beans.DollListBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.GetDollPageDataModel;


public class GetDollPageDataPresenter {

    private static final String TAG = "CatchDollFragment";

    private GetDollPageDataModel mModel;
    private OnPresenterListener.OnViewListener mView;

    public GetDollPageDataPresenter(OnPresenterListener.OnViewListener view) {
        this.mView = view;
        this.mModel=new GetDollPageDataModel();
    }

    public void detachView(){
        this.mModel.close();
        this.mView=null;
    }

    public void getDollBannersData(){
        this.mModel.getDollBanners(new OnPresenterListener.OnModelListener<BaseBean<DollBannerBean>>() {
            @Override
            public void onSuccess(BaseBean<DollBannerBean> bean) {
                if (bean != null) {
                    if (GetDollPageDataPresenter.this.mView != null) {
                        GetDollPageDataPresenter.this.mView.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (GetDollPageDataPresenter.this.mView != null) {
                    GetDollPageDataPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }

    public void getDollList(int page){
        this.mModel.getDollList(page, new OnPresenterListener.OnModelListener<BaseBean<DollListBean>>() {
            @Override
            public void onSuccess(BaseBean<DollListBean> result) {
                if (result != null) {
                    if (GetDollPageDataPresenter.this.mView != null) {
                        GetDollPageDataPresenter.this.mView.onSuccess(result);
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (GetDollPageDataPresenter.this.mView != null) {
                    GetDollPageDataPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
