package com.gachat.network;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2018/4/16.
 */

public class BaseModel {

    private CompositeSubscription mCompositeSubscription;


    protected CompositeSubscription getCompositeSubscription(){
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription=new CompositeSubscription();
        }
        return this.mCompositeSubscription;
    }

    public void addCompositeSubscription(Subscription subscription){
        if (this.mCompositeSubscription == null){
            this.mCompositeSubscription=new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    public void delCompositeSubscription(){
        if (this.mCompositeSubscription != null && this.mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
