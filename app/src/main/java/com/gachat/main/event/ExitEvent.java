package com.gachat.main.event;

import android.util.Log;

import com.dnion.VAChatAPI;
import com.dnion.VADollAPI;
import com.gachat.generator.config.DaoDelete;
import com.gachat.generator.helper.GreenDaoHelper;
import com.gachat.main.Constant;
import com.gachat.main.mvp.models.UpdateUserData;
import com.gachat.main.util.SharedPreferencesHelper;
import com.gachat.main.util.manager.ActivityManager;

/**
 *
 */
public class ExitEvent {


    /**
     * 退出APP
     */
    public static void exitApp(){
        publicMethod();
        ActivityManager.getInstance().exitSystem();
    }

    /**
     * 退出登录
     */
    public static void exitLogin(){
        DaoDelete.deleteUserAll();
        SharedPreferencesHelper.getInstance().clear();
        publicMethod();
    }

    private static void publicMethod(){
        GreenDaoHelper.close();
        UpdateUserData.getInstance().close();
        Log.i("ApplicationHelper", "sdk disconnect ");
        Constant.setChatConnect(false);
        Constant.setDollConnect(false);
        VAChatAPI.getInstance().disconnect();
        VADollAPI.getInstance().disconnect();
    }

}
