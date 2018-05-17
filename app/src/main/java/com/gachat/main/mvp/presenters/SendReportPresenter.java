package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.SendReportModel;


public class SendReportPresenter {

    private static final String TAG = "ReportDialogFragment";

    private SendReportModel mModel;
    private OnPresenterListener.OnViewListener<BaseBean<MessageBean>> mView;

    public SendReportPresenter(OnPresenterListener.OnViewListener<BaseBean<MessageBean>> view) {
        this.mView = view;
        this.mModel=new SendReportModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void sendReport(long toUserId,int reasonId){
        this.mModel.sendReport(toUserId,reasonId, new OnPresenterListener.OnModelListener<BaseBean<MessageBean>>() {
            @Override
            public void onSuccess(BaseBean<MessageBean> bean) {
                if (SendReportPresenter.this.mView != null) {
                    Log.e(TAG, TAG+" onSuccess: "+bean.toString());
                    SendReportPresenter.this.mView.onSuccess(bean);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (SendReportPresenter.this.mView != null) {
                    Log.e(TAG, TAG+" onFailed");
                    SendReportPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
