package com.gachat.main.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.logger.Logger;


public class JumpToActivityUtil {

    public static void jumpNoParams(Activity activity, Class<? extends Activity> tClass,boolean isfinish){
        Logger.d("进入跳转。。。。。");
        Intent intent=new Intent(activity,tClass);
        activity.startActivity(intent);
        if (isfinish) {
            Log.i("JumpUtils", "jumpNoParams: ");
            activity.finish();

     }
    }

    public static void jumpParams(Activity activity, Class<? extends Activity> tClass, Bundle bundle,boolean isfinish){
        Intent intent=new Intent(activity,tClass);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isfinish) {
            activity.finish();
        }
    }
}
