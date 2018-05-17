package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.dnion.DollRoomSignaling;
import com.dnion.RenderProxy;
import com.dnion.SharedRTCEnv;
import com.dnion.VADollAPI;
import com.dnion.VADollSignaling;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.api.UserAPI;
import com.gachat.main.application.ApplicationHelper;
import com.gachat.main.base.BaseActivity;
import com.gachat.main.base.BaseBean;
import com.gachat.main.beans.RemoteVideoBean;
import com.gachat.main.beans.UserBean;
import com.gachat.main.event.SdkDollEvent;
import com.gachat.main.mvp.models.UpdateUserData;
import com.nineoldandroids.animation.ObjectAnimator;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observer;

public class DollRoomActivity extends BaseActivity{

    private static final String TAG = "DollRoomActivity";

    private FrameLayout mFlFullScreenView;
    private TextView mTvRoomMembers;
    private TextView mtvCatchName;
//    private ImageView mImgSet;
//    private ImageView mImgCamera;
    private ImageView mImgRecharge;
    private RelativeLayout mRlControl;
    private TextView mTvNeedSurplus;
    private TextView mTvSurplusDiamonds;
    private RelativeLayout mRlUserInfo;
    private TextView mTvTitleControl;
    private TextView mTvUserMember;
    private RelativeLayout mRlActionCatch;
    private ImageView mActionLeft;
    private ImageView mActionTop;
    private ImageView mActionRight;
    private ImageView mActionBottom;
    private RelativeLayout orientControl;
//    RelativeLayout mActionCatch;
    private TextView mTvTimer;

    private SdkDollEventCallback callback;
    private boolean isJoinRoom = false;
    private boolean isJoinQueue=false;
    private boolean isStartGame=false;
    private boolean isRunning=false;
    public int needDiamonds=0;
    private static int catchTime=0;  //抓取动态时间
    private int cacheTime=0;  //缓存抓取时间

    private List<RemoteVideoBean> mVideoBeans;
    private boolean isVideo0=false;

//    private Timer mTimer ;
//    private TimerTask mTimerTask;

    public String roomId;

    public static String ROOM_ID="ROOM_ID";
    public static String USER_ID="USER_ID";
    public static String USER_NAME="USER_NAME";
    public static String DIAMONDS="DIAMONDS";
    public static String TOKEN="TOKEN";
    public static String GOODS_IMG="GOODS_IMG";
    public static String GOODS_NAME="GOODS_NAME";
    public static String GOODS_DESC="GOODS_DESC";

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_doll_room;
    }

    @Override
    protected void initView() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        Log.i("initView", "屏幕宽: "+width+"\t\t高："+height);

        setBackIcon(R.drawable.ic_launcher_background);
        setToolbarBackgroundColor(Color.TRANSPARENT);
        mVideoBeans=new ArrayList<>();

        mFlFullScreenView=findViewById(R.id.fl_FullScreenView);

        mTvTimer=findViewById(R.id.tv_timer);
        mTvRoomMembers=findViewById(R.id.tv_roomMembers);
        mtvCatchName=findViewById(R.id.tv_CatchName);
//        mImgSet=findViewById(R.id.img_set);
//        mImgCamera=findViewById(R.id.img_camera);
        mImgRecharge=findViewById(R.id.img_recharge);
        mRlControl=findViewById(R.id.rl_control);
        mTvNeedSurplus=findViewById(R.id.tv_needsurplus);
        mTvSurplusDiamonds=findViewById(R.id.tv_surplusDiamonds);
        mRlUserInfo=findViewById(R.id.rl_userInfo);
        mTvTitleControl=findViewById(R.id.tv_titleControl);
        mTvUserMember=findViewById(R.id.tv_usermember);

        mRlActionCatch=findViewById(R.id.rl_ActionCatch);
        mActionLeft=findViewById(R.id.iv_actionLeft);
        mActionTop=findViewById(R.id.iv_actionTop);
        mActionRight=findViewById(R.id.iv_actionRight);
        mActionBottom=findViewById(R.id.iv_actionBottom);
        orientControl=findViewById(R.id.orientation_control);
//        mActionCatch=findViewById(R.id.iv_catch);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initOperate(Bundle savedInstanceState) {
        setBackIcon(R.drawable.ic_launcher_background);
        setToolbarBackgroundColor(Color.TRANSPARENT);

        callback=new SdkDollEventCallback();
        ApplicationHelper.getInstance().setSdkDollListener(callback);

        findViewById(R.id.img_back).setOnClickListener(v -> onBackPressed());

        mActionTop.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandTop);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandTopDown);
                        break;
                    }
                }
            return false;
        });

        mActionLeft.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:{
                    VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandLeft);
                    break;
                }
                case MotionEvent.ACTION_UP:
                    VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandLeftDown);
                    break;
            }
            return false;
        });

        mActionRight.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:{
                    VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandRight);
                    break;
                }
                case MotionEvent.ACTION_UP:
                    VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandRightDown);
                    break;
            }
            return false;
        });

        mActionBottom.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:{
                    VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandBottom);
                    break;
                }
                case MotionEvent.ACTION_UP:
                    VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandBottomDown);
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) mFlFullScreenView.getLayoutParams();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height=(params.width*4)/3;
        mFlFullScreenView.setLayoutParams(params);

        Bundle bundle=getIntent().getExtras();
        if (bundle != null) {
            roomId=bundle.getString(ROOM_ID);
//            String uid = bundle.getString(USER_ID);
            String s=bundle.getString(DIAMONDS);
            String str="剩余钻石："+ s + "钻";
            mTvSurplusDiamonds.setText(str);
//            String token = bundle.getString(TOKEN);
            String name=bundle.getString(USER_NAME);
            if (Constant.getDollConnect()) {
                Log.i(TAG, "start join roomId: "+roomId+"\t name"+name);
                VADollAPI.getInstance().joinDollRoom(roomId,name);
            }
        }
    }

    @OnClick({R.id.img_set, R.id.img_camera, R.id.iv_roomInfo, R.id.img_recharge, R.id.rl_control, R.id.iv_catch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                RepairDialogFragment.getInstance().show(getSupportFragmentManager(),"repair");
                break;
            case R.id.img_camera:
                int size=mVideoBeans.size();
                if (size == 2) {
                    isVideo0 = !isVideo0;
                    if (isVideo0) {
                        mVideoBeans.get(0).getSurfaceView().setVisibility(View.VISIBLE);
                        mVideoBeans.get(1).getSurfaceView().setVisibility(View.GONE);
                        ObjectAnimator animator=ObjectAnimator.ofFloat(orientControl,"rotation",90f,0f);
//                        animator.setDuration(500);
                        animator.start();
                    } else {
                        mVideoBeans.get(0).getSurfaceView().setVisibility(View.GONE);
                        mVideoBeans.get(1).getSurfaceView().setVisibility(View.VISIBLE);
                        ObjectAnimator animator=ObjectAnimator.ofFloat(orientControl,"rotation",0f,90f);
//                        animator.setDuration(500);
                        animator.start();
                    }
                }
                break;
            case R.id.img_recharge:
                RechargeDialogFragment.getInstance().show(getSupportFragmentManager(),"recharge");
                break;
            case R.id.iv_roomInfo:
                GoodsInfoDialogFragment.getInstance().show(getSupportFragmentManager(),"goodsinfo");
                break;
            case R.id.rl_control:
                if (isJoinRoom) {
                    if (UserData().getDiamond() >= needDiamonds) {
                        if (isStartGame) { // 当房间没有人，进来直接开始游戏
                            VADollAPI.getInstance().startQueue();
                            isStartGame =!isStartGame;
                        }else {
                            //队列人数不为 0 时isStartGame
                            if (isJoinQueue) { //  离开队列
                                Reset();
                            } else {        //加入队列
                                VADollAPI.getInstance().startQueue();
                                mTvTitleControl.setText("取消排队");
                                mRlControl.setBackgroundResource(R.drawable.icon_doll_state_red);
                                isJoinQueue = !isJoinQueue;
                            }
                        }
                    }else {
                        BalanceDialogFragment.getInstance().show(getSupportFragmentManager(),"balance");
                    }
                }else {
                    ToastUtils.showShort("房间异常");
                }
                break;
            case R.id.iv_catch:
                VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandCatch);
                break;
        }
    }

    public void Reset() {
        isJoinQueue=false;
        catchTime=cacheTime;
        mRlActionCatch.setVisibility(View.GONE);
        mRlControl.setVisibility(View.VISIBLE);
        mRlUserInfo.setVisibility(View.VISIBLE);
        mImgRecharge.setVisibility(View.VISIBLE);
        mImgRecharge.setVisibility(View.VISIBLE);
        mTvTitleControl.setText("申请排队");
        VADollAPI.getInstance().leaveQueue();
        mRlControl.setBackgroundResource(R.drawable.icon_doll_state_blue);
    }

    private StartCountDownTimer mStartCountDownTimer;
    private void initCatchTimer(){
        mStartCountDownTimer=new StartCountDownTimer( catchTime * 1000,1000);
        mStartCountDownTimer.start();
    }


    private class StartCountDownTimer extends CountDownTimer {
        StartCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            isRunning=true;
            mTvTimer.setText((millisUntilFinished/1000)+"s");
        }

        @Override
        public void onFinish() {
            Log.i(TAG, "开始游戏倒计时 完成: ");
            isRunning=false;
            Reset();
        }
    }


    private class SdkDollEventCallback implements SdkDollEvent {
        @Override
        public void onConnectionSuccess() {  runOnUiThread(() -> Log.i(TAG, "onConnectionSuccess: "));  }

        @Override
        public void onConnectionFailed(String s) {  runOnUiThread(() -> Log.i(TAG, "onConnectionFailed: ")); }

        @Override
        public void onDisconnected(String s) { runOnUiThread(() -> Log.i(TAG, "onDisconnected: ")); }

        @SuppressLint("SetTextI18n")
        @Override
        public void onJoinRoom(DollRoomSignaling.DOLLRoomInfo info) {
            runOnUiThread(() -> {
                if (info != null) {
                    isJoinRoom = true;

                    Constant.setGameReadyTime((int)info.time_ready);
                    Constant.setAgainTime((int)info.time_tryagain);

                    cacheTime=(int)info.time_game;  //重置时间
                    catchTime=(int)info.time_game;  //游戏抓取时间
                    mTvTimer.setText(catchTime+"s");

                    needDiamonds=(int)info.golden;

//                    String str2="房间人数："+info.totalnum+"人";
//                    mTvRoomMembers.setText(str2);

                    String str="需要钻："+info.golden + "/次";
                    mTvNeedSurplus.setText(str);
                }
            });
        }

        @Override
        public void onJoinRoomFailed(String s, String s1) {
            runOnUiThread(() -> {
                isJoinRoom = false;
                Toast.makeText(DollRoomActivity.this, "加入房间失败："+s1, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onLeaveRoom() {  runOnUiThread(() -> Log.i(TAG, "onLeaveRoom: "));  }

        @Override
        public void onReadyGame() {
            runOnUiThread(() -> {
                Log.i(TAG, "onReadyGame: ");
                mTvTitleControl.setText("开始抓");
                mRlControl.setBackgroundResource(R.drawable.icon_doll_state_yellow);
                StartGameDialogFragment.getInstance().show(getSupportFragmentManager(),"startgame");
            });
        }

        @Override
        public void onStartGame(long golden) {
            runOnUiThread(() -> {
                updateDiamonds();
//                String str="剩余钻石："+ golden + "钻";
//                mTvSurplusDiamonds.setText(str);
                isJoinQueue=true;
                mImgRecharge.setVisibility(View.GONE);
                mRlControl.setVisibility(View.GONE);
                mRlUserInfo.setVisibility(View.GONE);
                initCatchTimer();
                mRlActionCatch.setVisibility(View.VISIBLE);
                Log.i(TAG, "onStartGame  catchTime："+catchTime+"\t\t\t"+golden);
            });
        }

        @Override
        public void onStartGameFailed(long l, String s) {  runOnUiThread(() -> Log.i(TAG, "onStartGameFailed: "));  }

        @Override
        public void onRefreshRoomInfo(VADollSignaling.RoomInfo roomInfo) {  runOnUiThread(() -> Log.i(TAG, "onRefreshRoomInfo: "+roomInfo.membersCount));  }

        @Override
        public void onUserJoin(VADollSignaling.UserInfo userInfo) {  runOnUiThread(() -> Log.i(TAG, "onUserJoin: "));  }

        @Override
        public void onUserUpdate(VADollSignaling.UserInfo userInfo) {  runOnUiThread(() -> Log.i(TAG, "onUserUpdate: "));  }

        @Override
        public void onUserLeave(VADollSignaling.UserInfo userInfo) {  runOnUiThread(() -> Log.i(TAG, "onUserLeave: "));  }

        @Override
        public void onGotUserVideo(String s) {
            runOnUiThread(() -> {
                Log.i("UserVideo", "onGotUserVideo: "+s);
                RenderProxy fullRenderProxy = SharedRTCEnv.getInstance().createRenderProxy(DollRoomActivity.this);
                fullRenderProxy.setAspectMode(RenderProxy.AspectMode.aspectFill);
                SurfaceView mSurfaceView = fullRenderProxy.getDisplay();
                String videoI=s.substring(0,6);
                mFlFullScreenView.addView(mSurfaceView);
                if (videoI.equals("video0")) {
                    mSurfaceView.setVisibility(View.VISIBLE);
                    isVideo0=true;
                } else {
                    mSurfaceView.setVisibility(View.GONE);
                }
                mVideoBeans.add(new RemoteVideoBean(videoI,mSurfaceView));
                VADollAPI.getInstance().setRemoteDisplay(mSurfaceView, s);
            });
        }

        @Override
        public void onLostUserVideo(String s) {
            runOnUiThread(() -> {
                Log.i("UserVideo", "onLostUserVideo: "+s);
                String videoI=s.substring(0,6);
                int size=mVideoBeans.size();
                int i;
                for (i = 0; i < size; i++) {
                    if (videoI.equals(mVideoBeans.get(i).getStreamId())){
                        SurfaceView view=mVideoBeans.get(i).getSurfaceView();
                        mFlFullScreenView.removeView(view);
                        break;
                    }
                }
            });
        }

        @Override
        public void onCatchResult(int i) {
            runOnUiThread(() -> {
                Log.i(TAG, "抓取结果 onCatchResult: "+i);
                switch (i){
                    case 2:{ new CatchFailedDialogFragment().show(getSupportFragmentManager(),"catchfailed"); break; }
                    case 3:{ new CatchSucDialogFragment().show(getSupportFragmentManager(),"catchsuc");  break;   }
                }
//                if (mTimer !=null &&  mTimerTask != null) {
//                    Log.i(TAG, "抓取计时器重置: ");
//                    mTimer.purge();
//                    mTimerTask.cancel();
//                    mTimer.cancel();
//                    mTimerTask=null;
//                    mTimer=null;
//                    catchTime=cacheTime;
//                }
                if (mStartCountDownTimer != null) {
                    mStartCountDownTimer.cancel();
                    mStartCountDownTimer=null;
                }
//                catchTime=cacheTime;
            });
        }

        @Override
        public void onQueueInfoUpdate(DollRoomSignaling.DOLLRoomInfo info,DollRoomSignaling.DOLLVAUserInfo dollvaUserInfo) {
            runOnUiThread(() -> {
                Logger.d("onQueueInfoUpdate："+info);
                if (info != null) {
                    String str3 = info.shownum + "人排队";
                    mTvUserMember.setText(str3);

                    String str2="房间人数："+info.totalnum+"人";
                    mTvRoomMembers.setText(str2);

                    if (info.shownum == 0) {
                        mtvCatchName.setText("");
                        mTvTitleControl.setText("开始抓");
                        mRlControl.setBackgroundResource(R.drawable.icon_doll_state_yellow);
                        isStartGame = true;
                    } else {
                        if (!isJoinQueue) {
                            mTvTitleControl.setText("申请排队");
                            mRlControl.setBackgroundResource(R.drawable.icon_doll_state_blue);
                        }
                        isStartGame = false;
                    }

                    if (!TextUtils.isEmpty(dollvaUserInfo.username)) {
                        mtvCatchName.setText(dollvaUserInfo.username);
                    }
                }
            });
        }

        @Override
        public void onTextMessage(String s, String s1) {  runOnUiThread(() -> Log.i(TAG, "onUserUpdate: "));  }
    }

    //仅仅更新钻石字段
    private void updateDiamonds(){
        UserAPI.getUserDiamonds(new Observer<BaseBean<UserBean>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    Log.i(TAG, "run: "+e.getMessage());
                    Toast.makeText(DollRoomActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onNext(BaseBean<UserBean> bean) {
                runOnUiThread(() -> {
                    if (bean.getCode() == 0 && bean.getResult() != null) {
                        Log.i("UpdateUserData", "更新钻石--->>>>>> "+bean.getResult().getDiamond());

                        UpdateUserData.getInstance().update();

                        String str="剩余钻石："+bean.getResult().getDiamond() + "钻";
                        mTvSurplusDiamonds.setText(str);
                    }

                    if (bean.getCode() != 0 && bean.getError().getMessage().size() > 0){
                        for (String s : bean.getError().getMessage()) {
                           Log.i("UpdateUserData", s);
                            Toast.makeText(DollRoomActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        close();
        super.onDestroy();
        ApplicationHelper.getInstance().getRefWatcher().watch(this);
    }

    void close() {
        isJoinQueue=false;

        if (mStartCountDownTimer != null) {
            if (isRunning) {
                mStartCountDownTimer.onFinish();
            }
            mStartCountDownTimer.cancel();
            mStartCountDownTimer=null;
        }

        if (Constant.getDollConnect() && isJoinRoom) {
            Log.i(TAG, "leaveRoom");
            VADollAPI.getInstance().leaveRoom();
        }

        int size=mVideoBeans.size();
        for (int i = 0; i < size; i++) {
            SurfaceView view=mVideoBeans.get(i).getSurfaceView();
            mFlFullScreenView.removeView(view);
        }
        mVideoBeans.clear();
        mFlFullScreenView=null;
        callback=null;
    }


}
