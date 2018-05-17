package com.gachat.main.mvp.presenters;

import android.util.Log;

import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.MessageBean;
import com.gachat.main.mvp.impls.OnPresenterListener;
import com.gachat.main.mvp.models.SendGiftModel;


public class SendGiftPresenter {

    private static final String TAG = "SendGiftModel";

    private SendGiftModel mModel;
    private OnPresenterListener.OnViewListener<BaseBean<MessageBean>> mView;

    public SendGiftPresenter(OnPresenterListener.OnViewListener<BaseBean<MessageBean>> view) {
        this.mView = view;
        this.mModel=new SendGiftModel();
    }

    public void detachView(){
        mModel.close();
        this.mView=null;
    }

    public void sendGift(long toUserId,int dollId){
        this.mModel.sendGift(toUserId,dollId, new OnPresenterListener.OnModelListener<BaseBean<MessageBean>>() {
            @Override
            public void onSuccess(BaseBean<MessageBean> bean) {
                if (SendGiftPresenter.this.mView != null) {
                    Log.e(TAG, TAG+" onSuccess: "+bean.toString());
                    SendGiftPresenter.this.mView.onSuccess(bean);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (SendGiftPresenter.this.mView != null) {
                    Log.e(TAG, TAG+" onFailed");
                    SendGiftPresenter.this.mView.onFailed(throwable);
                }
            }
        });
    }
}
