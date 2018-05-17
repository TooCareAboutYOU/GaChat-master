package com.gachat.main.ui.doll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

import java.lang.ref.WeakReference;

/**
 * 余额不足
 */

public class BalanceDialogFragment extends BaseDialogFragment {

    TextView mTvTimer;

    @SuppressLint("StaticFieldLeak")
    private static BalanceDialogFragment instance;
    public BalanceDialogFragment (){}
    public static synchronized BalanceDialogFragment getInstance() {
         if (instance == null) {
             instance = new BalanceDialogFragment();
         }
     return instance;
    }

    private TextView mTextView;

    private WeakReference<DollRoomActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((DollRoomActivity) context);
    }

    @Override
    public int setResLayoutId() {
        return R.layout.doll_balance_dialog;
    }

    @Override
    public void initView(View view) {
        mTvTimer=view.findViewById(R.id.tv_timer);
        view.findViewById(R.id.cancel).setOnClickListener(v -> {
            mTimer.cancel();
            dismiss();
        });
        mTextView=view.findViewById(R.id.tv_gotoRecharge);
        mTextView.setOnClickListener(v -> {
            mTimer.cancel();
            RechargeDialogFragment.getInstance().show(mActivity.get().getSupportFragmentManager(),"recharge");
            dismiss();
        });

        mTimer.start();

    }


    CountDownTimer mTimer=new CountDownTimer(15000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String str="离开("+(millisUntilFinished/1000)+"s)";
            mTvTimer.setText(str);
        }

        @Override
        public void onFinish() {
            dismiss();
        }
    };

    @Override
    public void onDestroyView() {
        mTextView=null;
        mTimer=null;
        super.onDestroyView();
    }
}
