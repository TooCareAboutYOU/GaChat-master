package com.gachat.main.ui.doll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dnion.DollRoomSignaling;
import com.dnion.RenderProxy;
import com.dnion.SharedRTCEnv;
import com.dnion.VADollAPI;
import com.dnion.VADollDelegate;
import com.dnion.VADollSignaling;
import com.gachat.main.R;

public class TestActivity extends AppCompatActivity { //implements VADollDelegate {

    private static final String TAG = "TesActivity";

    private LinearLayout mLayout;
    private RenderProxy fullRenderProxy;
    private SurfaceView mSurfaceView;
    private Button btnStartGame,btnLeft,btnTop,btnRight,btnDown,btnCatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        VADollAPI.getInstance().setListener(new DelegateCallBack());

        mLayout=findViewById(R.id.rl_container);

        findViewById(R.id.btn_connect).setOnClickListener(v -> {
//                VADollAPI.getInstance().connect(Config.DOLL_SDK_URL,"53","");
        });

        findViewById(R.id.btn_join).setOnClickListener(v -> VADollAPI.getInstance().joinDollRoom("806","哈哈"));

        findViewById(R.id.btn_joinQueue).setOnClickListener(v -> VADollAPI.getInstance().startQueue());

        btnStartGame=findViewById(R.id.btn_StartGame);

        findViewById(R.id.btn_StartGame).setOnClickListener(v -> VADollAPI.getInstance().startGame());

        findViewById(R.id.btn_left).setOnClickListener(v -> VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandLeft));
        findViewById(R.id.btn_top).setOnClickListener(v -> VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandTop));
        findViewById(R.id.btn_right).setOnClickListener(v -> VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandRight));
        findViewById(R.id.btn_down).setOnClickListener(v -> VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandBottom));
        findViewById(R.id.btn_catch).setOnClickListener(v -> VADollAPI.getInstance().dollMachineControl(DollRoomSignaling.DOLLMachineCommand.MachineCommandCatch));

    }

    public class DelegateCallBack implements VADollDelegate {
        @Override
        public void onConnectionSuccess() {
            Log.i(TAG, "onConnectionSuccess: ");
        }

        @Override
        public void onConnectionFailed(String s) {
            Log.i(TAG, "onConnectionFailed: ");
        }

        @Override
        public void onDisconnected(String s) { Log.i(TAG, "onDisconnected: "); }

        @Override
        public void onJoinRoom(DollRoomSignaling.DOLLRoomInfo dollRoomInfo) {  Log.i(TAG, "onJoinRoom: ");  }

        @Override
        public void onJoinRoomFailed(String s, String s1) { Log.i(TAG, "onJoinRoomFailed: "); }

        @Override
        public void onLeaveRoom() { Log.i(TAG, "onLeaveRoom: "); }

        @Override
        public void onReadyGame() {
            Log.i(TAG, "onReadyGame: ");
            runOnUiThread(() -> btnStartGame.setEnabled(true));
        }

        @Override
        public void onCatchResult(int i) { Log.i(TAG, "onCatchResult: "+i); }

        @Override
        public void onQueueInfoUpdate(DollRoomSignaling.DOLLRoomInfo dollRoomInfo, DollRoomSignaling.DOLLVAUserInfo dollvaUserInfo) {

        }


        @Override
        public void onStartGame(long l) { Log.i(TAG, "onStartGame: "); }

        @Override
        public void onStartGameFailed(long l, String s) { Log.i(TAG, "onStartGameFailed: "); }

        @Override
        public void onRefreshRoomInfo(VADollSignaling.RoomInfo roomInfo) { Log.i(TAG, "onRefreshRoomInfo: "); }

        @Override
        public void onUserJoin(VADollSignaling.UserInfo userInfo) { Log.i(TAG, "onUserJoin: "); }

        @Override
        public void onUserUpdate(VADollSignaling.UserInfo userInfo) { Log.i(TAG, "onUserUpdate: "); }

        @Override
        public void onUserLeave(VADollSignaling.UserInfo userInfo) { Log.i(TAG, "onUserLeave: "); }

        @Override
        public void onGotUserVideo(final String s) {
            Log.i(TAG, "onGotUserVideo 1: "+s);
            runOnUiThread(() -> {
                Log.i(TAG, "onGotUserVideo 2: "+s);
                fullRenderProxy = SharedRTCEnv.getInstance().createRenderProxy(TestActivity.this);
                mSurfaceView = fullRenderProxy.getDisplay();
                mSurfaceView.setZOrderOnTop(true);
                mLayout.addView(mSurfaceView);
                VADollAPI.getInstance().setRemoteDisplay(mSurfaceView, s);
            });
        }

        @Override
        public void onLostUserVideo(String s) { Log.i(TAG, "onLostUserVideo: "); }

        @Override
        public void onTextMessage(String s, String s1) { Log.i(TAG, "onTextMessage: "); }
    }


//    public class CallBackEvent implements SdkDollEvent{
//        @Override
//        public void onConnectionSuccess() {
//            Log.i(TAG, "onConnectionSuccess: ");
//        }
//
//        @Override
//        public void onConnectionFailed(String s) {
//            Log.i(TAG, "onConnectionFailed: ");
//        }
//
//        @Override
//        public void onDisconnected(String s) {
//            Log.i(TAG, "onDisconnected: ");
//        }
//
//        @Override
//        public void onJoinRoom(DollRoomSignaling.DOLLRoomInfo dollRoomInfo) {
//            Log.i(TAG, "onJoinRoom: ");
//        }
//
//        @Override
//        public void onJoinRoomFailed(String s, String s1) {
//            Log.i(TAG, "onJoinRoomFailed: ");
//        }
//
//        @Override
//        public void onLeaveRoom() {
//            Log.i(TAG, "onLeaveRoom: ");
//        }
//
//        @Override
//        public void onReadyGame() {
//            Log.i(TAG, "onReadyGame: ");
//            runOnUiThread(() -> btnStartGame.setEnabled(true));
//        }
//
//        @Override
//        public void onCatchResult(int i) {
//            Log.i(TAG, "onCatchResult: ");
//        }
//
//        @Override
//        public void onStartGame(long l) {
//            Log.i(TAG, "onStartGame: ");
//        }
//
//        @Override
//        public void onStartGameFailed(long l, String s) {
//            Log.i(TAG, "onStartGameFailed: ");
//        }
//
//        @Override
//        public void onRefreshRoomInfo(VADollSignaling.RoomInfo roomInfo) {
//            Log.i(TAG, "onRefreshRoomInfo: ");
//        }
//
//        @Override
//        public void onUserJoin(VADollSignaling.UserInfo userInfo) {
//            Log.i(TAG, "onUserJoin: ");
//        }
//
//        @Override
//        public void onUserUpdate(VADollSignaling.UserInfo userInfo) {
//            Log.i(TAG, "onUserUpdate: ");
//        }
//
//        @Override
//        public void onUserLeave(VADollSignaling.UserInfo userInfo) {
//            Log.i(TAG, "onUserLeave: ");
//        }
//
//        @Override
//        public void onGotUserVideo(final String s) {
//            Log.i(TAG, "onGotUserVideo 1: "+s);
//            runOnUiThread(() -> {
//                Log.i(TAG, "onGotUserVideo 2: "+s);
//                fullRenderProxy = SharedRTCEnv.getInstance().createRenderProxy(TestActivity.this);
//                mSurfaceView = fullRenderProxy.getDisplay();
//                mSurfaceView.setZOrderOnTop(true);
//                mLayout.addView(mSurfaceView);
//                VADollAPI.getInstance().setRemoteDisplay(mSurfaceView, s);
//            });
//        }
//
//        @Override
//        public void onLostUserVideo(String s) {
//            Log.i(TAG, "onLostUserVideo: ");
//        }
//
//        @Override
//        public void onTextMessage(String s, String s1) {
//            Log.i(TAG, "onTextMessage: ");
//        }
//    }
}