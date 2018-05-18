package com.gachat.main.event;

import android.util.Log;

import com.dnion.VAChatAPI;
import com.dnion.VADollAPI;
import com.gachat.main.Constant;
import com.gachat.main.mvp.models.UpdateUserData;
import com.gachat.main.util.SharedPreferencesHelper;

/**
 *
 */
public class ExitEvent {

    /**
     * 退出APP
     */
    public static void exitApp(){
        publicMethod();
    }

    /**
     * 退出登录
     */
    public static void exitLogin(){
//        DaoDelete.deleteUserAll();
        SharedPreferencesHelper.getInstance().clear();
        publicMethod();
    }

    private static void publicMethod(){
//        GreenDaoHelper.close();
        UpdateUserData.getInstance().close();
        Log.i("ApplicationHelper", "sdk disconnect ");
        Constant.setChatConnect(false);
        Constant.setDollConnect(false);
        VAChatAPI.getInstance().disconnect();
        VADollAPI.getInstance().disconnect();
    }

}
