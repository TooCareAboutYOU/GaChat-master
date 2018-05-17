package com.gachat.main.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.dnion.DollRoomSignaling;
import com.dnion.SharedRTCEnv;
import com.dnion.VAChatAPI;
import com.dnion.VAChatDelegate;
import com.dnion.VAChatSignaling;
import com.dnion.VADollAPI;
import com.dnion.VADollDelegate;
import com.dnion.VADollSignaling;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gachat.generator.helper.GreenDaoHelper;
import com.gachat.main.Config;
import com.gachat.main.Constant;
import com.gachat.main.event.SdkChatEvent;
import com.gachat.main.event.SdkDollEvent;
import com.gachat.main.util.MyUtils;
import com.gachat.main.util.SharedPreferencesHelper;
import com.gachat.main.util.manager.CrashHandlerManager;
import com.gachat.network.HttpManager;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;


public class ApplicationHelper {

    private static final String TAG = "ApplicationHelper";

    private  Application mApplication;
    private  RefWatcher sRefWatcher;

    private static ApplicationHelper instance = new ApplicationHelper();
    public static ApplicationHelper getInstance(){  return instance; }


    public void init(Application app){
        mApplication=app;
        mSdkDollEvent=null;
        mSdkChatEvent=null;
        initNetWork();
        initDataBase();
        initLeakCanary();
        initImageLoader();
        initLog();
        initUtils();
        initEvent();
        initSDK();
    }

    private  void initNetWork() { HttpManager.init(mApplication.getApplicationContext(), Config.BASE_URL);  }

    private  void initDataBase() {
        SharedPreferencesHelper.init(mApplication.getApplicationContext(),"GaChat-Data", Context.MODE_PRIVATE);
        GreenDaoHelper.init(mApplication.getApplicationContext(),"GaChat.db","123");
    }

    //内存泄漏检测
    private  void initLeakCanary(){ sRefWatcher=LeakCanary.install(mApplication);  }
    public  RefWatcher getRefWatcher(){ return sRefWatcher; }

    // https://www.fresco-cn.org/docs/configure-image-pipeline.html
    private  void initImageLoader() {
        Fresco.initialize(mApplication);
    }

    // TODO: 2018/4/3 正式上线关闭LOG打印
    private  void initLog() {
        Logger.init("JumpUtils")//自定义日志TAG
              .logLevel(LogLevel.FULL);//测试阶段设置日志输出
    }

    // https://www.jianshu.com/p/72494773aace
    private  void initUtils(){
        Utils.init(mApplication);
    }

    private  void initEvent(){
        EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .throwSubscriberException(true)
                .build();
    }
//    设置异常处理
    private  void initCrash(){ CrashHandlerManager.getInstance().init(mApplication); }


    private static SdkDollEvent mSdkDollEvent;
    public void setSdkDollListener(SdkDollEvent listener){
        mSdkDollEvent=listener;
    }

    private static SdkChatEvent mSdkChatEvent;
    public void setSdkChatEventListener(SdkChatEvent listener){  mSdkChatEvent=listener;  }

    private void initSDK(){

        SharedRTCEnv.CreateSharedRTCEnv(mApplication.getApplicationContext());
        VADollAPI.CreateVADollAPI(mApplication.getApplicationContext());
        VAChatAPI.CreateVAChatAPI(mApplication.getApplicationContext(),true);

        VAChatAPI.getInstance().setListener(new VAChatDelegate() {
            @Override
            public void onConnectionSuccess() {
                Logger.i("onConnectionSuccess: VAChatAPI "+ MyUtils.isMainThread());
                Constant.setChatConnect(true);
            }

            @Override
            public void onConnectionFailed(String s) {
                Logger.i("onConnectionFailed: VAChatAPI "+ MyUtils.isMainThread());
                Constant.setChatConnect(false);
            }

            @Override
            public void onDisconnected(String reason) {
                Logger.i("onDisconnected: VAChatAPI "+ MyUtils.isMainThread());
                Constant.setChatConnect(false);
            }

            @Override
            public void onChatStart(VAChatSignaling.ChatInfo data) {
                Logger.i("onChatStart: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Log.i(TAG, "onChatStart: ");
                    mSdkChatEvent.onChatStart(data);
                }
            }

            @Override
            public void onChatFinish() {
                Logger.i("onChatFinish: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Log.i(TAG, "onChatFinish: ");
                    mSdkChatEvent.onChatFinish();
                }
            }

            @Override
            public void onChatTerminate() {
                Logger.i("onChatTerminate: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Log.i(TAG, "onChatTerminate: ");
                    mSdkChatEvent.onChatTerminate();
                }
            }

            @Override
            public void onChatCancel() {
                Logger.i("onChatCancel: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Logger.i( "onChatCancel: ");
                    mSdkChatEvent.onChatCancel();
                }
            }

            @Override
            public void onGotDoll(long l) {
                Logger.i("onGotDoll: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Logger.i(  "onGotDoll: ");
                    mSdkChatEvent.onGotDoll(l);
                }
            }

            @Override
            public void onGotUserVideo() {
                Logger.i("onGotUserVideo: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Logger.i( "onGotUserVideo: ");
                    mSdkChatEvent.onChatGotUserVideo();
                }
            }

            @Override
            public void onLostUserVideo() {
                Logger.i("onLostUserVideo: VAChatAPI "+ MyUtils.isMainThread());

                if (mSdkChatEvent != null) {
                    Log.i(TAG, "onLostUserVideo: ");
                    mSdkChatEvent.onChatLostUserVideo();
                }

            }
        });

        VADollAPI.getInstance().setListener(new VADollDelegate() {
            @Override
            public void onConnectionSuccess() {
                Logger.d("onConnectionSuccess: VADollAPI "+ MyUtils.isMainThread());

                Constant.setDollConnect(Constant.IsTrue);
//                EventBus.getDefault().post(new MessageEventDoll.onDollConnectState(0));
            }

            @Override
            public void onConnectionFailed(String s) {
                Logger.d("onConnectionFailed: VADollAPI "+ MyUtils.isMainThread());

                Log.e(TAG, "onConnectionFailed: "+s);
                Constant.setDollConnect(Constant.IsFalse);
//                EventBus.getDefault().post(new MessageEventDoll.onDollConnectState(1));
            }

            @Override
            public void onDisconnected(String s) {
                Logger.d("onDisconnected: VADollAPI "+ MyUtils.isMainThread());

                Log.d(TAG, "onDisconnected: "+s);
                Constant.setDollConnect(Constant.IsFalse);
//                EventBus.getDefault().post(new MessageEventDoll.onDollConnectState(2));
            }

            @Override
            public void onJoinRoom(DollRoomSignaling.DOLLRoomInfo info) {
                Logger.d("onJoinRoom: VADollAPI "+ MyUtils.isMainThread());
                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onJoinRoom: ");
                    mSdkDollEvent.onJoinRoom(info);
                }
            }

            @Override
            public void onJoinRoomFailed(String s, String s1) {
                Logger.d("onJoinRoomFailed: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onJoinRoomFailed: ");
                    mSdkDollEvent.onJoinRoomFailed(s, s1);
                }
            }

            @Override
            public void onLeaveRoom() {
                Logger.d("onLeaveRoom: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onLeaveRoom: ");
                    mSdkDollEvent.onLeaveRoom();
                }
            }

            @Override
            public void onReadyGame() {
                Logger.d("onReadyGame: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null){
                    Log.i(TAG, "onReadyGame: ");
                     mSdkDollEvent.onReadyGame();
                }
            }

            @Override
            public void onCatchResult(int i) {
                Logger.d("onCatchResult: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onCatchResult: ");
                    mSdkDollEvent.onCatchResult(i);
                }
            }

            @Override
            public void onQueueInfoUpdate(DollRoomSignaling.DOLLRoomInfo dollRoomInfo, DollRoomSignaling.DOLLVAUserInfo dollvaUserInfo) {
                Logger.d("onQueueInfoUpdate: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onQueueInfoUpdate: ");
                    mSdkDollEvent.onQueueInfoUpdate(dollRoomInfo,dollvaUserInfo);
                }
            }

            @Override
            public void onStartGame(long l) {
                Logger.d("onStartGame: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onStartGame: ");
                    mSdkDollEvent.onStartGame(l);
                }
            }

            @Override
            public void onStartGameFailed(long l, String s) {
                Logger.d("onStartGameFailed: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onStartGameFailed: ");
                    mSdkDollEvent.onStartGameFailed(l, s);
                }
            }

            @Override
            public void onRefreshRoomInfo(VADollSignaling.RoomInfo roomInfo) {
                Logger.d("onRefreshRoomInfo: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onRefreshRoomInfo: ");
                    mSdkDollEvent.onRefreshRoomInfo(roomInfo);
                }
            }

            @Override
            public void onUserJoin(VADollSignaling.UserInfo userInfo) {
                Logger.d("onUserJoin: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onUserJoin: ");
                    mSdkDollEvent.onUserJoin(userInfo);
                }
            }

            @Override
            public void onUserUpdate(VADollSignaling.UserInfo userInfo) {
                Logger.d("onUserUpdate: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onUserUpdate: ");
                    mSdkDollEvent.onUserUpdate(userInfo);
                }
            }

            @Override
            public void onUserLeave(VADollSignaling.UserInfo userInfo) {
                Logger.d("onUserLeave: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onUserLeave: ");
                    mSdkDollEvent.onUserLeave(userInfo);
                }
            }

            @Override
            public void onGotUserVideo(String s) {
                Logger.d("onGotUserVideo: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onGotUserVideo: ");
                    mSdkDollEvent.onGotUserVideo(s);
                }
            }

            @Override
            public void onLostUserVideo(String s) {
                Logger.d("onLostUserVideo: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onLostUserVideo: ");
                    mSdkDollEvent.onLostUserVideo(s);
                }
            }

            @Override
            public void onTextMessage(String s, String s1) {
                Logger.d("onTextMessage: VADollAPI "+ MyUtils.isMainThread());

                if (mSdkDollEvent != null) {
                    Log.i(TAG, "onTextMessage: ");
                    mSdkDollEvent.onTextMessage(s, s1);
                }
            }
        });

    }

}
