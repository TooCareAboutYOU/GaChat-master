package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnion.VADollAPI;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

import java.lang.ref.WeakReference;

/**
 * 开始游戏
 */

@SuppressLint("ValidFragment")
public class StartGameDialogFragment extends BaseDialogFragment  implements DialogInterface.OnKeyListener {

    private static final String TAG = "StartGameFragment";
    
    private TextView mTvTimer;

    @SuppressLint("StaticFieldLeak")
    private static StartGameDialogFragment instance;
    @SuppressLint("ValidFragment")
    public StartGameDialogFragment (){ }
    public static synchronized StartGameDialogFragment getInstance() {
         if (instance == null) {
             instance = new StartGameDialogFragment();
         }
        return instance;
    }

    private boolean isRunning=false;
    private StartCountDownTimer startCountDownTimer;

    private WeakReference<DollRoomActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((DollRoomActivity) context);
    }

    @Override
    public int setResLayoutId() {  return R.layout.doll_startgame_dialog;  }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        getDialog().setOnKeyListener(this);
        mTvTimer=view.findViewById(R.id.tv_timer);
        mTvTimer.setText(Constant.getGameReadyTime()+"s");

        startCountDownTimer=new StartCountDownTimer(Constant.getGameReadyTime()*1000,1000);
        startCountDownTimer.start();


        view.findViewById(R.id.rl_startgame).setOnClickListener(v -> {
            Log.i(TAG, "开始游戏");
            startCountDownTimer.cancel();
            VADollAPI.getInstance().startGame();
            dismiss();
        });

        ImageView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            Log.i(TAG, "取消游戏: ");
            startCountDownTimer.onFinish();
            startCountDownTimer.cancel();
        });
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        Log.i(TAG, "onKey: "+keyCode);
        if (keyCode ==KeyEvent.KEYCODE_BACK){
            if (isRunning) {
                Log.i(TAG, "onKey: back");
                if (startCountDownTimer != null) {
                    startCountDownTimer.onFinish();
                    startCountDownTimer.cancel();
                    startCountDownTimer=null;
                }
            }
            return true;
        }else {
            return false;
        }
    }

    private class StartCountDownTimer extends CountDownTimer{
        StartCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isRunning=true;
            String str=(millisUntilFinished/1000)+"s";
            mTvTimer.setText(str);
            Log.i(TAG, "开始游戏倒计时: " + str);
        }

        @Override
        public void onFinish() {
            Log.i(TAG, "开始游戏倒计时 完成: ");
            isRunning=false;
            mActivity.get().Reset();
            dismiss();
        }
    }
}
