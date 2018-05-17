package com.gachat.main.mvp.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.gachat.generator.config.DaoInsert;
import com.gachat.generator.config.DaoQuery;
import com.gachat.generator.config.DaoUpdate;
import com.gachat.main.Constant;
import com.gachat.main.api.UserAPI;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.LoginBean;
import com.gachat.main.beans.UserBean;
import com.gachat.main.util.SharedPreferencesHelper;
import com.gachat.network.BaseModel;
import com.orhanobut.logger.Logger;

import java.util.Objects;

import rx.Observer;
import rx.Subscription;


public class UpdateUserData extends BaseModel {

    private static final String TAG = "UpdateUserData";

    private UpdateUserData(){}
    public static UpdateUserData getInstance(){  return UpdateUserDataHolder.instance;  }

    private static class UpdateUserDataHolder{
        @SuppressLint("StaticFieldLeak")
        public static final UpdateUserData instance=new UpdateUserData();
    }

    private Subscription mSubscription;

    public void update(){

        if (DaoQuery.queryUserlistSize() == 0) return;


        if (NetworkUtils.isConnected()) {
            mSubscription = UserAPI.getUserData(myObserver);
            if (mSubscription != null) {
                addCompositeSubscription(mSubscription);
            }
        }else {
            Logger.e("网络异常");
        }
    }

    private Observer<BaseBean<UserBean>> myObserver = new Observer<BaseBean<UserBean>>() {
        @Override
        public void onCompleted() {
            Log.i(TAG, "onCompleted: ");
        }

        @Override
        public void onError(Throwable e) {
            Logger.e(e.getMessage());
        }

        @Override
        public void onNext(BaseBean<UserBean> bean) {
            if (bean.getCode() == 0){
                if (bean.getResult() != null) {
                    updateInfo(bean);
                }
            }else if (bean.getError() != null){
                if (bean.getError().getMessage().size() > 0) {
                    for (String s : bean.getError().getMessage()) {
                        Logger.i(s);
                    }
                }
            }
        }
    };

    public void close(){
        if (mSubscription != null) {
            delCompositeSubscription();
        }
    }

    private static void updateInfo(BaseBean<UserBean> bean){
        com.gachat.generator.model.UserBean dbBean=DaoQuery.queryUserbeanByUid(bean.getResult().getUid());
        if (dbBean == null){
            return;
        }
        dbBean.setUsername(bean.getResult().getUsername());
        dbBean.setGender(bean.getResult().getGender());
        dbBean.setAge(bean.getResult().getAge());
        dbBean.setDiamond(bean.getResult().getDiamond());
        dbBean.setCharacter(bean.getResult().getCharacter());
        dbBean.setRank(bean.getResult().getRank());
        dbBean.setClaw_doll_time(bean.getResult().getClaw_doll_time());
        dbBean.setGift(bean.getResult().getGift());

        DaoUpdate.updateUser(dbBean,bean.getResult().getUid());

        Log.d(TAG, "数据库保存数据："+ Objects.requireNonNull(DaoQuery.queryUserlist()).get(0).toString());
    }


    public void insertUserData(BaseBean<LoginBean> result){
        com.gachat.generator.model.UserBean bean=new com.gachat.generator.model.UserBean(
                null,
                true,
                "JWT "+result.getResult().getToken(),
                result.getResult().getUser().getUsername(),
                result.getResult().getUser().getGender(),
                result.getResult().getUser().getDiamond(),
                result.getResult().getUser().getUid(),
                result.getResult().getUser().getAge(),
                result.getResult().getUser().getCharacter(),
                result.getResult().getUser().getRank(),
                result.getResult().getUser().getClaw_doll_time(),
                result.getResult().getUser().getGift());
        DaoInsert.insertUser(bean);
        SharedPreferencesHelper.getInstance().setBooleanValue(Constant.LOGIN_STATUE,Constant.IsTrue);
    }

//    public static String TOKEN="TOKEN";
//    public static String USERNAME="USERNAME";
//    public static String GENDER="GENDER";
//    public static String DIAMOND="DIAMOND";
//    public static String UID="UID";
//    public static String AGE="AGE";
//    public static String CHARACTER="CHARACTER";
//    public static String RANK="RANK";
//    public static String CLAW_DOLL_TIME="CLAW_DOLL_TIME";
//    public static String GIFT="GIFT";
//
//
//    public void  saveUserData(BaseBean<LoginBean> result){
//        com.gachat.generator.model.UserBean dbBean=new com.gachat.generator.model.UserBean();
//        SharedPreferencesHelper.getInstance().setStringValue(TOKEN,"JWT "+result.getResult().getToken());
//        SharedPreferencesHelper.getInstance().setStringValue(USERNAME,result.getResult().getUser().getUsername());
//        SharedPreferencesHelper.getInstance().setStringValue(GENDER,result.getResult().getUser().getGender());
//        SharedPreferencesHelper.getInstance().setIntValue(DIAMOND,result.getResult().getUser().getDiamond());
//        SharedPreferencesHelper.getInstance().setStringValue(RANK,result.getResult().getUser().getRank());
//        SharedPreferencesHelper.getInstance().setIntValue(UID,result.getResult().getUser().getUid());
//        SharedPreferencesHelper.getInstance().setIntValue(AGE,result.getResult().getUser().getAge());
//        SharedPreferencesHelper.getInstance().setIntValue(CHARACTER,result.getResult().getUser().getCharacter());
//        SharedPreferencesHelper.getInstance().setIntValue(CLAW_DOLL_TIME,result.getResult().getUser().getClaw_doll_time());
//        SharedPreferencesHelper.getInstance().setIntValue(GIFT,result.getResult().getUser().getGift());
//    }
}
