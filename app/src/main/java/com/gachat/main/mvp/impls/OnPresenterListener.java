package com.gachat.main.mvp.impls;


public interface OnPresenterListener {

    interface OnModelListener<T> {
        void onSuccess(T result);
        void onFailed(Throwable throwable);
    }

    interface OnViewListener<T>{
        void onSuccess(T result);
        void onFailed(Throwable throwable);
    }

}
