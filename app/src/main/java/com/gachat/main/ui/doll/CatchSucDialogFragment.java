package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.dnion.VADollAPI;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

import java.lang.ref.WeakReference;

/**
 * 抓取成功
 */

public class CatchSucDialogFragment extends BaseDialogFragment implements DialogInterface.OnKeyListener{

    private static final String TAG = "CatchSucDialogFragment";

    TextView mTvTimer;

    @SuppressLint("StaticFieldLeak")
    private static CatchSucDialogFragment instance;
    public CatchSucDialogFragment (){

    }
    public static synchronized CatchSucDialogFragment getInstance() {
         if (instance == null) {
             instance = new CatchSucDialogFragment();
         }
     return instance;
    }

    private boolean isRunning=false;
    private StartCountDownTimer startCountDownTimer;

//    private CountDownTimer mTimer=new CountDownTimer(Constant.getAgainTime() * 1000,1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//            isRunning=true;
//            String str="离开("+(millisUntilFinished/1000)+"s)";
//            mTvTimer.setText(str);
//            Log.i(TAG, "onTick: "+str);
//        }
//
//        @Override
//        public void onFinish() {
//            Log.i(TAG, "onFinish: ");
//            isRunning=false;
//            mActivity.get().Reset();
//            dismiss();
//        }
//    };

    private WeakReference<DollRoomActivity> mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((DollRoomActivity) context);
    }

    @Override
    public int setResLayoutId() {
        return R.layout.doll_catchsuc_dialog;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView(View view) {
        getDialog().setOnKeyListener(this);

        mTvTimer=view.findViewById(R.id.tv_timer);
        mTvTimer.setText(Constant.getAgainTime()+"s");

        startCountDownTimer=new StartCountDownTimer(Constant.getAgainTime() * 1000,1000);

        mTvTimer.setOnClickListener(v -> {
            if (startCountDownTimer != null) {
                startCountDownTimer.cancel();
            }
            mActivity.get().Reset();
            dismiss();
        });
        view.findViewById(R.id.tv_catch_again).setOnClickListener(v -> {
            if (startCountDownTimer != null) {
                startCountDownTimer.cancel();
            }
            VADollAPI.getInstance().startGame();
            dismiss();
        });
        startCountDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        mTvTimer=null;
        super.onDestroyView();
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
                dismiss();
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
            String str="离开("+(millisUntilFinished/1000)+"s)";
            mTvTimer.setText(str);
            Log.i(TAG, "onTick: "+str);
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
