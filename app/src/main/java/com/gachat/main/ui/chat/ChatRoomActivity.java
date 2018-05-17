package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dnion.RenderProxy;
import com.dnion.SharedRTCEnv;
import com.dnion.VAChatAPI;
import com.dnion.VAChatSignaling;
import com.gachat.main.Config;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.application.ApplicationHelper;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.event.SdkChatEvent;
import com.gachat.main.ui.MainActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class ChatRoomActivity extends BaseActivity implements SdkChatEvent{

    private static final String TAG = "ChatRoomActivity";


    @BindView(R.id.fl_FullScreenView)
    FrameLayout mFlFullScreenView;
    @BindView(R.id.fl_SmallScreenView)
    FrameLayout mFlSmallScreenView;
    @BindView(R.id.img_StartChat)
    ImageView mImgStartChat;

    @BindView(R.id.ll_UserRoomInfo)
    LinearLayout mllUserRoomInfo;
    @BindView(R.id.tv_Username)
    TextView mTvUsername;
    @BindView(R.id.tv_CatchTimes)
    TextView mTvCatchTimes;
    @BindView(R.id.tv_GetDollTimes)
    TextView mTvGetDollTimes;

    @BindView(R.id.img_IsShow)
    ImageView mImgIsShow;
    @BindView(R.id.img_report)
    ImageView mImgReport;
    @BindView(R.id.rl_timer)
    RelativeLayout mTimerContainer;
//    @BindView(R.id.tv_timer)
    TextView mTvTimer;

    private RenderProxy fullRenderProxy,smallProxy;
    private SurfaceView localSFView,remoteSFView;
    private MyHandler mHandler=new MyHandler();

    private boolean isChating=false;

    private static final int CONNECT_SUCCESS=0;
    private static final int CONNECT_FAILED=1;
    private static final int DISCONNECTED=2;
    private static final int CHAT_START=3;
    private static final int CHAT_FINISH=4;
    private static final int CHAT_TERMINATE=5;
    private static final int CHAT_CANCEL=6;
    private static final int GOT_DOLL=7;
    private static final int CHAT_GOT_USER_VIDEO=8;
    private static final int CHAT_LOST_USER_VIDEO=9;

    private int uid;
    private String token;

    public void showLikedDialogFragment(){
        LikedDialogFragment.getInstance().show(getSupportFragmentManager(),"liked");
    }

    public void showReportDialogFragment(){
        ReportDialogFragment.getInstance().show(getSupportFragmentManager(),"report");
    }

    public void showWaitPairingFragment(){
        WaitPairingFragment.getInstance().show(getSupportFragmentManager(),"waiting");
    }

    public void showCommentDialogFragment(){
        CommentDialogFragment.getInstance().show(getSupportFragmentManager(),"comment");
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_chat_room;
    }

    @Override
    protected void initView() {
        Logger.d("进入房间。。。。。");
        setBackIcon(R.drawable.ic_launcher_background);
        setToolbarBackgroundColor(Color.TRANSPARENT);
        mTvTimer=findViewById(R.id.tv_timer);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initOperate(Bundle savedInstanceState) {
        Logger.d("初始化成功。。。。。");

        findViewById(R.id.img_back).setOnClickListener(v -> onBackPressed());

        ApplicationHelper.getInstance().setSdkChatEventListener(this);

        controlView(false);

        if (UserData() != null) {
            uid=UserData().getUid();
            token=UserData().getToken();
            Log.i("ReportDialogFragment", "uid："+uid+"\ntoken: "+token);
            mTvUsername.setText(UserData().getUsername());
            String str1="抓娃娃"+UserData().getClaw_doll_time()+"次";
            String str2="收礼物"+UserData().getGift()+"次";
            mTvCatchTimes.setText(str1);
            mTvGetDollTimes.setText(str2);
        }

        mImgIsShow.setOnClickListener(v -> {
            mImgIsShow.setImageResource(mllUserRoomInfo.getVisibility() == View.VISIBLE ? R.drawable.icon_open_fullscreen : R.drawable.icon_close_fullscreen);
            mllUserRoomInfo.setVisibility(mllUserRoomInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        mImgReport.setOnClickListener(v -> showReportDialogFragment());

//        mImgStartChat.setOnClickListener(v -> {
//            showWaitPairingFragment();
//            VAChatAPI.getInstance().waitPair();
//        });
        RxView.clicks(mImgStartChat)
                .throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    showWaitPairingFragment();
                    VAChatAPI.getInstance().waitPair();
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fullRenderProxy = SharedRTCEnv.getInstance().createRenderProxy(getApplicationContext());
        fullRenderProxy.setAspectMode(RenderProxy.AspectMode.aspectFill);
        localSFView= fullRenderProxy.getDisplay();
        mFlFullScreenView.addView(localSFView);
        VAChatAPI.getInstance().startPreview(localSFView);
    }



    public void gotoDoll(){
        MainActivity.changepage();
        this.finish();
    }

    CountDownTimer timer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String str=(millisUntilFinished / 1000) + "s";
            mTvTimer.setText(str);
        }

        @Override
        public void onFinish() {
            Log.i(TAG, "timer  onFinish: ");
            String str="30s";
            mTvTimer.setText(str);
        }
    };


    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

                switch (msg.what) {
                    case CONNECT_SUCCESS: {
                        Log.i(TAG, "handleMessage ---->>> 连接成功");
                        isChating = true;
                        break;
                    }
                    case CONNECT_FAILED: {
                        Log.i(TAG, "handleMessage ---->>>  连接失败");
                        if (mImgStartChat != null) {
                            mImgStartChat.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                    case DISCONNECTED: {
                        Log.i(TAG, "handleMessage ---->>>   断开连接");
                        if (mImgStartChat != null) {
                            mImgStartChat.setVisibility(View.VISIBLE);
                        }
                        VAChatAPI.getInstance().connect(Config.CHAT_SDK_URL, uid + "", token);
                        timer.cancel();
                        timer.onFinish();
                        break;
                    }
                    case CHAT_GOT_USER_VIDEO: {
                        Log.i(TAG, "handleMessage ---->>>  获取远端视频");
                        break;
                    }
                    case CHAT_START: {
                        Log.i(TAG, "handleMessage ---->>>  开始聊天");

                        controlView(true);

                        timer.start();
                        isChating = true;
                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
                            mFlFullScreenView.removeView(localSFView);

                            smallProxy = SharedRTCEnv.getInstance().createRenderProxy(getApplicationContext());
                            smallProxy.setAspectMode(RenderProxy.AspectMode.aspectFill);
                            remoteSFView = smallProxy.getDisplay();
                            remoteSFView.setZOrderOnTop(false);
                            VAChatAPI.getInstance().setRemoteDisplay(remoteSFView);
                            mFlFullScreenView.addView(remoteSFView);

                            mFlSmallScreenView.removeAllViews();
                            mFlSmallScreenView.setVisibility(View.VISIBLE);
                            mFlSmallScreenView.addView(localSFView);
                        }
                        VAChatSignaling.ChatInfo info = (VAChatSignaling.ChatInfo) msg.obj;
                        Log.d("handleMessage", "handleMessage: " + "\n" +
                                info.calleePeer + "\n" +
                                info.callerPeer + "\n" +

                                info.calleeRate + "\n" +
                                info.callerRate + "\n" +

                                info.calleeUser + "\n" +
                                info.callerUser + "\n" +

                                info.chatId + "\n" +
                                info.chatPhase);
                        if (uid == info.calleeUser) {
                            Constant.setCalleeId(info.callerUser);
                        } else {
                            Constant.setCalleeId(info.calleeUser);
                        }
                        WaitPairingFragment.getInstance().dismiss();
                        break;
                    }
                    case CHAT_FINISH: {
                        Log.i(TAG, "handleMessage ---->>>  聊天完成");
                        isChating = false;
                        timer.cancel();
                        timer.onFinish();
                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
                            Log.i(TAG, "大屏加载本地视频");
                            mFlSmallScreenView.removeAllViews(); //removeView(localSFView);
                            mFlFullScreenView.removeView(remoteSFView);
                            mFlSmallScreenView.setVisibility(View.GONE);
                            mFlFullScreenView.addView(localSFView);
                        }
                        controlView(false);
                        showCommentDialogFragment();

                        break;
                    }
                    case CHAT_TERMINATE: {
                        controlView(false);
                        timer.cancel();
                        timer.onFinish();
                        isChating = false;
                        Log.i(TAG, "handleMessage ---->>>  断开连接");
                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
                            Log.i(TAG, "大屏加载本地视频");
                            if (remoteSFView != null) {
                                mFlSmallScreenView.removeAllViews();  //(localSFView);
                                mFlFullScreenView.removeView(remoteSFView);
                                mFlSmallScreenView.setVisibility(View.GONE);
                                mFlFullScreenView.addView(localSFView);
                            }
                        }

                        break;
                    }
                    case CHAT_CANCEL: {
                        Log.i(TAG, "handleMessage ---->>>  取消聊天");
                        timer.cancel();
                        timer.onFinish();
                        break;
                    }
                    case GOT_DOLL: {
                        Log.i(TAG, "handleMessage ---->>>  获得娃娃");
                        break;
                    }
                    case CHAT_LOST_USER_VIDEO: {
                        Log.i(TAG, "handleMessage ---->>>  失去远端视频");
                        controlView(false);
                        timer.cancel();
                        timer.onFinish();
                        isChating = false;
                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
                            Log.i(TAG, "大屏加载本地视频");
                            if (remoteSFView != null) {
                                mFlSmallScreenView.removeAllViews(); //removeView(localSFView);
                                mFlFullScreenView.removeView(remoteSFView);
                                mFlSmallScreenView.setVisibility(View.GONE);
                                mFlFullScreenView.addView(localSFView);
                            }
                        }
                        break;
                    }
                }

        }
    }


    private void controlView(boolean isShow){
        if (mllUserRoomInfo != null) {
            mllUserRoomInfo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

        if (mImgIsShow != null) {
            mImgIsShow.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

        if (mImgReport != null) {
            mImgReport.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

        if (mTimerContainer != null) {
            mTimerContainer.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

        if (mImgStartChat != null) {
            mImgStartChat.setVisibility(!isShow ? View.VISIBLE : View.GONE);
        }
    }




//    @SuppressLint("HandlerLeak")
//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what){
//                case CONNECT_SUCCESS:{
//                        Log.i(TAG, "handleMessage ---->>> 连接成功");
//                        isChating=true;
//                    break;
//                }
//                case CONNECT_FAILED:{
//                        Log.i(TAG, "handleMessage ---->>>  连接失败");
//                        if (mImgStartChat != null){
//                            mImgStartChat.setVisibility(View.VISIBLE);
//                        }
//                    break;
//                }
//                case DISCONNECTED:{
//                        Log.i(TAG, "handleMessage ---->>>   断开连接");
//                        if (mImgStartChat != null){
//                            mImgStartChat.setVisibility(View.VISIBLE);
//                        }
//                        VAChatAPI.getInstance().connect(Config.CHAT_SDK_URL,uid+"",token);
//                        timer.cancel();
//                        timer.onFinish();
//                    break;
//                }
//                case CHAT_GOT_USER_VIDEO:{
//                    Log.i(TAG, "handleMessage ---->>>  获取远端视频");
////                    WaitPairingFragment.dismizz();
////                    if (remoteSFView == null && mFlFullScreenView != null) {
////                        remoteSFView = SharedRTCEnv.getInstance().createRenderProxy(ChatRoomActivity.this).getDisplay();
////                        remoteSFView.setZOrderOnTop(false);
////                        VAChatAPI.getInstance().setRemoteDisplay(remoteSFView);
////                        if (remoteSFView != null) {
////                            Log.i(TAG, "远端加载大屏: ");
////                            mFlFullScreenView.removeView(localSFView);
////                            mFlFullScreenView.addView(remoteSFView);
////                        }
////                    }
//                    break;
//                }
//                case CHAT_START:{   //开始计时
//                        Log.i(TAG, "handleMessage ---->>>  开始聊天");
//                        WaitPairingFragment.getInstance().dismiss();
//                        timer.start();
//                        isChating=true;
//                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                            mFlFullScreenView.removeView(localSFView);
//
//                            RenderProxy smallProxy= SharedRTCEnv.getInstance().createRenderProxy(ChatRoomActivity.this);
//                            remoteSFView =smallProxy.getDisplay();
//                            remoteSFView.setZOrderOnTop(false);
//                            VAChatAPI.getInstance().setRemoteDisplay(remoteSFView);
//                            mFlFullScreenView.addView(remoteSFView);
//
//                            mFlSmallScreenView.removeAllViews();
//                            mFlSmallScreenView.setVisibility(View.VISIBLE);
//                            mFlSmallScreenView.addView(localSFView);
//                        }
//                        VAChatSignaling.ChatInfo info= (VAChatSignaling.ChatInfo) msg.obj;
//                        Log.d("handleMessage", "handleMessage: "+"\n"+
//                            info.calleePeer+"\n"+
//                            info.callerPeer+"\n"+
//
//                            info.calleeRate+"\n"+
//                            info.callerRate+"\n"+
//
//                            info.calleeUser+"\n"+
//                            info.callerUser+"\n"+
//
//                            info.chatId+"\n"+
//                            info.chatPhase);
//                    if(uid == info.calleeUser) {
//                        Constant.setCalleeId(info.callerUser);
//                    }else {
//                        Constant.setCalleeId(info.calleeUser);
//                    }
//
//                    break;
//                }
//                case CHAT_FINISH:{
//                        Log.i(TAG, "handleMessage ---->>>  聊天完成");
//                        isChating=false;
//                        timer.cancel();
//                        timer.onFinish();
//                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                            Log.i(TAG, "大屏加载本地视频");
//                            mFlSmallScreenView.removeAllViews(); //removeView(localSFView);
//                            mFlFullScreenView.removeView(remoteSFView);
//                            mFlSmallScreenView.setVisibility(View.GONE);
//                            mFlFullScreenView.addView(localSFView);
//                        }
//                        if (mImgStartChat != null) {
//                            mImgStartChat.setVisibility(View.VISIBLE);
//                        }
//                        showCommentDialogFragment();
//
//                    break;
//                }
//                case CHAT_TERMINATE:{
//                        timer.cancel();
//                        timer.onFinish();
//                        isChating=false;
//                            Log.i(TAG, "handleMessage ---->>>  断开连接");
//                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                            Log.i(TAG, "大屏加载本地视频");
//                            if (remoteSFView !=null) {
//                                mFlSmallScreenView.removeAllViews();  //(localSFView);
//                                mFlFullScreenView.removeView(remoteSFView);
//                                mFlSmallScreenView.setVisibility(View.GONE);
//                                mFlFullScreenView.addView(localSFView);
//                            }
//                        }
//                        if (mImgStartChat != null) {
//                            mImgStartChat.setVisibility(View.VISIBLE);
//                        }
//                    break;
//                }
//                case CHAT_CANCEL:{
//                        Log.i(TAG, "handleMessage ---->>>  取消聊天");
//                        timer.cancel();
//                        timer.onFinish();
//                    break;
//                }
//                case GOT_DOLL:{
//                        Log.i(TAG, "handleMessage ---->>>  获得娃娃");
//                    break;
//                }
//                case CHAT_LOST_USER_VIDEO:{
//                        Log.i(TAG, "handleMessage ---->>>  失去远端视频");
//                        timer.cancel();
//                        timer.onFinish();
//                        isChating=false;
//                        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                            Log.i(TAG, "大屏加载本地视频");
//                            if (remoteSFView != null) {
//                                mFlSmallScreenView.removeAllViews(); //removeView(localSFView);
//                                mFlFullScreenView.removeView(remoteSFView);
//                                mFlSmallScreenView.setVisibility(View.GONE);
//                                mFlFullScreenView.addView(localSFView);
//                            }
//                        }
//                        if (mImgStartChat != null) {
//                            mImgStartChat.setVisibility(View.VISIBLE);
//                        }
//                    break;
//                }
//            }
//        }
//    };

    public void close() {
        Log.i(TAG, "close: ");
        if (timer != null) {
            timer.cancel();
        }

        if (mFlFullScreenView.getChildCount() > 0) {
            Log.i(TAG, "释放: 大屏");
            mFlFullScreenView.removeAllViews();
        }

        if (mFlSmallScreenView.getChildCount() > 0) {
            Log.i(TAG, "释放: 小屏");
            mFlSmallScreenView.removeAllViews();
        }
        fullRenderProxy=null;
        smallProxy=null;
        remoteSFView=null;
        localSFView=null;
        if (isChating) {
            Log.i(TAG, "isChating  close: ");
            VAChatAPI.getInstance().cancelChat();  //提前终止 1
//            VAChatAPI.getInstance().setTorchSwitchHandler();//local主动换人  （类似cancel ->waitpar流程）  远端接收onChatTerminate
        }
        isChating=false;
        VAChatAPI.getInstance().stopPreview();
    }

    @Override
    protected void onDestroy() {
        close();
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        ApplicationHelper.getInstance().getRefWatcher().watch(this);
    }

//    public class MyDollCallBack implements SdkChatEvent {

    @Override
    public void onChatConnectionSuccess() {  mHandler.sendEmptyMessage(CONNECT_SUCCESS);  }

    @Override
    public void onChatConnectionFailed(String s) {  mHandler.sendEmptyMessage(CONNECT_FAILED);    }

    @Override
    public void onChatDisconnected(String s) {   mHandler.sendEmptyMessage(DISCONNECTED);   }

    @Override
    public void onChatStart(VAChatSignaling.ChatInfo data) {
        Message message = mHandler.obtainMessage();
        message.obj = data;
        message.what = CHAT_START;
        mHandler.sendMessage(message);
    }

    @Override
    public void onChatFinish() {    mHandler.sendEmptyMessage(CHAT_FINISH);  }  //时间到了

    @Override
    public void onChatTerminate() {  mHandler.sendEmptyMessage(CHAT_TERMINATE);  }  //被cancel remote

    @Override
    public void onChatCancel() {  mHandler.sendEmptyMessage(CHAT_CANCEL);  }  //2 local接收

    @Override
    public void onGotDoll(long params) {
        Message msg = mHandler.obtainMessage();
        msg.arg1 = (int) params;
        msg.what = GOT_DOLL;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onChatGotUserVideo() {  mHandler.sendEmptyMessage(CHAT_GOT_USER_VIDEO);  }

    @Override
    public void onChatLostUserVideo() {  mHandler.sendEmptyMessage(CHAT_LOST_USER_VIDEO);   }

//    }

}
