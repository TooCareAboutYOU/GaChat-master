package com.gachat.main.util.manager;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ActivityManager {

//    private static final String TAG = "ActivityManager";

    private List<Activity> mActivityList=new ArrayList<Activity>();

    private static ActivityManager instance;
    public ActivityManager(){}

    private volatile static ActivityManager singleton;
    public static synchronized ActivityManager getInstance() {
        if (singleton == null) {
             synchronized (ActivityManager.class) {
                  if (singleton == null) {
                      singleton = new ActivityManager();
                  }
             }
         }
        return singleton;
    }

    public void addActivity(Activity activity){
        if (mActivityList == null) {
            mActivityList=new ArrayList<>();
        }
        if (!mActivityList.contains(activity)){
            mActivityList.add(activity);
        }
    }

    public void removeActivity(Activity activity){
        if (activity != null) {
            if (mActivityList.contains(activity)){
                mActivityList.remove(activity);
            }
            activity.finish();
            mActivityList.remove(activity);
        }
    }
    
    public void exitSystem(){
        if(mActivityList.size() > 0) {
            for (Activity activity : mActivityList) {
                if (activity.getClass().getSimpleName().equals("SplashActivity") || activity.getClass().getSimpleName().equals("LoginActivity") || activity.getClass().getSimpleName().equals("MainActivity")) {
                    Log.i("onKeyDown", "exitSystem: ");
                    activity.finish();
                    removeActivity(activity);
                    System.exit(0);
                }
            }
        }
    }

//    public void clearExceptMain(){
//        if (mActivityList.size() > 0) {
//            for (Activity activity : mActivityList) {
//                if (activity.getClass().getSimpleName().equals("MainActivity")){
//                    continue;
//                }else {
//                    activity.finish();
//                    removeActivity(activity);
//                }
//            }
//        }
//    }
}
