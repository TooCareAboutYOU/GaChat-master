package com.gachat.main.ui.chat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.dnion.RenderProxy;
import com.dnion.SharedRTCEnv;
import com.dnion.VAChatAPI;
import com.gachat.main.Config;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.event.MessageEventChat;
import com.gachat.main.ui.MainActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

public class ChatRoomActivity extends BaseActivity{

    private static final String TAG = "ChatRoomActivity";

    private FrameLayout mFlFullScreenView;
    private FrameLayout mFlSmallScreenView;
    public ImageView mImgStartChat;

    private LinearLayout mllUserRoomInfo;
    private TextView mTvUsername;
    private TextView mTvCatchTimes;
    private TextView mTvGetDollTimes;

    private ImageView mImgIsShow;
    private ImageView mImgReport;
    private RelativeLayout mTimerContainer;
    private TextView mTvTimer;

    private RenderProxy fullRenderProxy,smallProxy;
    private SurfaceView localSFView,remoteSFView;
//    private MyHandler mHandler=new MyHandler();

    private boolean isChating=false;

//    private static final int CONNECT_SUCCESS=0;
//    private static final int CONNECT_FAILED=1;
//    private static final int DISCONNECTED=2;
//    private static final int CHAT_START=3;
//    private static final int CHAT_FINISH=4;
//    private static final int CHAT_TERMINATE=5;
//    private static final int CHAT_CANCEL=6;
//    private static final int GOT_DOLL=7;
//    private static final int CHAT_GOT_USER_VIDEO=8;
//    private static final int CHAT_LOST_USER_VIDEO=9;

    private int uid;
    private String token;

    public void showLikedDialogFragment(){
        LikedDialogFragment.getInstance().show(getSupportFragmentManager(),"liked");
    }

    public void showReportDialogFragment(){
        ReportDialogFragment.getInstance().show(getSupportFragmentManager(),"report");
    }

    public void showWaitPairingFragment(){
        Log.i(WaitPairingFragment.TAG, "showWaitPairingFragment: ");
        WaitPairingFragment.getInstance().show(getSupportFragmentManager(),"waiting");
    }

    public void showCommentDialogFragment(){
        CommentDialogFragment.getInstance().show(getSupportFragmentManager(),"comment");
    }


    @Override
    protected int setLayoutResourceID() {  return R.layout.activity_chat_room;  }

    @Override
    protected void initView() {
        Logger.d("进入房间。。。。。");
        setBackIcon(R.drawable.ic_launcher_background);
        setToolbarBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.img_back).setOnClickListener(v -> onBackPressed());

        mFlFullScreenView=findViewById(R.id.fl_FullScreenView);
        mFlSmallScreenView=findViewById(R.id.fl_SmallScreenView);
        mImgStartChat=findViewById(R.id.img_StartChat);

        mllUserRoomInfo=findViewById(R.id.ll_UserRoomInfo);
        mTvUsername=findViewById(R.id.tv_Username);
        mTvCatchTimes=findViewById(R.id.tv_CatchTimes);
        mTvGetDollTimes=findViewById(R.id.tv_GetDollTimes);

        mImgIsShow=findViewById(R.id.img_IsShow);
        mImgReport=findViewById(R.id.img_report);
        mTimerContainer=findViewById(R.id.rl_timer);
        mTvTimer=findViewById(R.id.tv_timer);
        controlView(false);

//        ApplicationHelper.getInstance().setSdkChatEventListener(new MyDollCallBack());

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initOperate(Bundle savedInstanceState) {
        Logger.d("初始化成功。。。。。");

        if (UserData() != null) {
            uid=UserData().getUid();
            token=UserData().getToken();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatConnectState(MessageEventChat.onChatConnectState event){
        Log.i(TAG, "onChatConnectState: ");
        switch (event.getStates()) {
            case 0:
                isChating = true;
                break;
            case 1:
                if (mImgStartChat != null) {
                    mImgStartChat.setVisibility(View.VISIBLE);
                }
                ToastUtils.showShort(event.getMsg());
                break;
            case 2:
                if (mImgStartChat != null) {
                    mImgStartChat.setVisibility(View.VISIBLE);
                }
                VAChatAPI.getInstance().connect(Config.CHAT_SDK_URL, uid + "", token);
                timer.cancel();
                ToastUtils.showShort(event.getMsg());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatStart(MessageEventChat.onChatStart event){
        Log.i(TAG, "onChatStart: 聊天开始");
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

        if (uid == event.getChatInfo().calleeUser) {
            Constant.setCalleeId(event.getChatInfo().callerUser);
        } else {
            Constant.setCalleeId(event.getChatInfo().calleeUser);
        }
        WaitPairingFragment.getInstance().dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatFinish(MessageEventChat.onChatFinish event){
        Log.i(TAG, "onChatFinish: 聊天完成");
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatTerminate(MessageEventChat.onChatTerminate event){
        Log.i(TAG, "onChatTerminate ---->>>  断开连接");
        controlView(false);
        timer.cancel();
        timer.onFinish();
        isChating = false;
        if (mFlFullScreenView != null && mFlSmallScreenView != null) {
            Log.i(TAG, "大屏加载本地视频");
            if (remoteSFView != null) {
                mFlSmallScreenView.removeAllViews();  //(localSFView);
                mFlFullScreenView.removeView(remoteSFView);
                mFlSmallScreenView.setVisibility(View.GONE);
                mFlFullScreenView.addView(localSFView);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatCancel(MessageEventChat.onChatCancel event){
        Log.i(TAG, "onChatCancel: 聊天结束");
        timer.cancel();
        timer.onFinish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotDoll(MessageEventChat.onGotDoll event){
        Log.i(TAG, "onGotDoll: 获取娃娃");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatGotUserVideo(MessageEventChat.onGotUserVideo event){
        Log.i(TAG, "onChatGotUserVideo: 获得视频");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatLostUserVideo(MessageEventChat.onLostUserVideo event){
        Log.i(TAG, "onChatLostUserVideo: 失去视频");
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
    }

//    public class MyDollCallBack implements SdkChatEvent {
//
//        @Override
//        public void onChatConnectionSuccess() {
////            mHandler.sendEmptyMessage(CONNECT_SUCCESS);
//            Log.i(TAG, "handleMessage ---->>> 连接成功");
//            isChating = true;
//        }
//
//        @Override
//        public void onChatConnectionFailed(String s) {
////            mHandler.sendEmptyMessage(CONNECT_FAILED);
//
//            Log.i(TAG, "handleMessage ---->>>  连接失败");
//            if (mImgStartChat != null) {
//                mImgStartChat.setVisibility(View.VISIBLE);
//            }
//
//        }
//
//        @Override
//        public void onChatDisconnected(String s) {
////            mHandler.sendEmptyMessage(DISCONNECTED);
//
//            Log.i(TAG, "handleMessage ---->>>   断开连接");
//            if (mImgStartChat != null) {
//                mImgStartChat.setVisibility(View.VISIBLE);
//            }
//            VAChatAPI.getInstance().connect(Config.CHAT_SDK_URL, uid + "", token);
//            timer.cancel();
//            timer.onFinish();
//        }
//
//        @Override
//        public void onChatStart(VAChatSignaling.ChatInfo data) {
////            Message message = mHandler.obtainMessage();
////            message.obj = data;
////            message.what = CHAT_START;
////            mHandler.sendMessage(message);
//
//            Log.i(TAG, "handleMessage ---->>>  开始聊天");
//
//            controlView(true);
//
//            timer.start();
//            isChating = true;
//            if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                mFlFullScreenView.removeView(localSFView);
//
//                smallProxy = SharedRTCEnv.getInstance().createRenderProxy(getApplicationContext());
//                smallProxy.setAspectMode(RenderProxy.AspectMode.aspectFill);
//                remoteSFView = smallProxy.getDisplay();
//                remoteSFView.setZOrderOnTop(false);
//                VAChatAPI.getInstance().setRemoteDisplay(remoteSFView);
//                mFlFullScreenView.addView(remoteSFView);
//
//                mFlSmallScreenView.removeAllViews();
//                mFlSmallScreenView.setVisibility(View.VISIBLE);
//                mFlSmallScreenView.addView(localSFView);
//            }
//
//            if (uid == data.calleeUser) {
//                Constant.setCalleeId(data.callerUser);
//            } else {
//                Constant.setCalleeId(data.calleeUser);
//            }
//            WaitPairingFragment.getInstance().dismiss();
//
//
//        }
//
//        @Override
//        public void onChatFinish() {     //时间到了
////            mHandler.sendEmptyMessage(CHAT_FINISH);
//            Log.i(TAG, "handleMessage ---->>>  聊天完成");
//            isChating = false;
//            timer.cancel();
//            timer.onFinish();
//            if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                Log.i(TAG, "大屏加载本地视频");
//                mFlSmallScreenView.removeAllViews(); //removeView(localSFView);
//                mFlFullScreenView.removeView(remoteSFView);
//                mFlSmallScreenView.setVisibility(View.GONE);
//                mFlFullScreenView.addView(localSFView);
//            }
//            controlView(false);
//            showCommentDialogFragment();
//
//        }
//
//        @Override
//        public void onChatTerminate() {    //被cancel remote
////            mHandler.sendEmptyMessage(CHAT_TERMINATE);
//            controlView(false);
//            timer.cancel();
//            timer.onFinish();
//            isChating = false;
//            Log.i(TAG, "handleMessage ---->>>  断开连接");
//            if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                Log.i(TAG, "大屏加载本地视频");
//                if (remoteSFView != null) {
//                    mFlSmallScreenView.removeAllViews();  //(localSFView);
//                    mFlFullScreenView.removeView(remoteSFView);
//                    mFlSmallScreenView.setVisibility(View.GONE);
//                    mFlFullScreenView.addView(localSFView);
//                }
//            }
//        }
//
//        @Override
//        public void onChatCancel() {  //2 local接收
////            mHandler.sendEmptyMessage(CHAT_CANCEL);
//            Log.i(TAG, "handleMessage ---->>>  取消聊天");
//            timer.cancel();
//            timer.onFinish();
//        }
//
//        @Override
//        public void onGotDoll(long params) {
////            Message msg = mHandler.obtainMessage();
////            msg.arg1 = (int) params;
////            msg.what = GOT_DOLL;
////            mHandler.sendMessage(msg);
//
//            Log.i(TAG, "handleMessage ---->>>  获得娃娃");
//
//        }
//
//
//        @Override
//        public void onChatGotUserVideo() {
////            mHandler.sendEmptyMessage(CHAT_GOT_USER_VIDEO);
//            Log.i(TAG, "handleMessage ---->>>  获取远端视频");
//
//        }
//
//        @Override
//        public void onChatLostUserVideo() {
////            mHandler.sendEmptyMessage(CHAT_LOST_USER_VIDEO);
//            Log.i(TAG, "handleMessage ---->>>  失去远端视频");
//            controlView(false);
//            timer.cancel();
//            timer.onFinish();
//            isChating = false;
//            if (mFlFullScreenView != null && mFlSmallScreenView != null) {
//                Log.i(TAG, "大屏加载本地视频");
//                if (remoteSFView != null) {
//                    mFlSmallScreenView.removeAllViews(); //removeView(localSFView);
//                    mFlFullScreenView.removeView(remoteSFView);
//                    mFlSmallScreenView.setVisibility(View.GONE);
//                    mFlFullScreenView.addView(localSFView);
//                }
//            }
//
//        }
//
//    }

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
//        ApplicationHelper.getInstance().getRefWatcher().watch(this);
    }

}
