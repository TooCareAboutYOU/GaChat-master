package com.gachat.network.fastjson;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 将一些重复的操作提取出来
 * 放到父类以免loader里面每个接口都有的重复代码
 */

public class ObjectLoader {
    protected <T> Observable<T> observe(Observable<T> observable){
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
