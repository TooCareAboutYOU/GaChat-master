package com.gachat.main.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.dnion.VAChatAPI;
import com.dnion.VADollAPI;
import com.gachat.main.Config;
import com.gachat.main.Constant;
import com.gachat.main.R;
import com.gachat.main.base.BaseFragment;
import com.gachat.main.ui.MainActivity;
import com.gachat.main.ui.chat.ChatRoomActivity;
import com.gachat.main.util.JumpToActivityUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GaChatFragment extends BaseFragment {

    private static final String TAG = "GaChatFragment";

    private View topView;
    private ImageView userLogo;
    private TextView mTvAccount;
    private TextView mTvRank;
    private TextView mTvDiamondCount;
    private TextView mTvCatchDollCount;
    private TextView mTvGiftCount;
    private ImageView mGifView;
    private ImageView mMatching;

    private MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_ga_chat;
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void initView(View view) {
        Log.i(TAG, "GaChatFragment initView: ");
        topView=view.findViewById(R.id.kachat_bg);
        userLogo=view.findViewById(R.id.img_UserLogo);
        mTvAccount=view.findViewById(R.id.tv_account);
        mTvRank=view.findViewById(R.id.tv_rank);
        mTvDiamondCount=view.findViewById(R.id.tv_diamondCount);
        mTvCatchDollCount=view.findViewById(R.id.tv_catchDollCount);
        mTvGiftCount=view.findViewById(R.id.tv_giftCount);
        mGifView=view.findViewById(R.id.iv_GifView);
        mMatching=view.findViewById(R.id.img_matching);


//        RxView.clicks(mMatching)
//                .throttleWithTimeout(1, TimeUnit.SECONDS)
//                .subscribe(o -> {
//                    if (Constant.getChatConnect()) {
//                        Logger.d("开始跳转。。。。。");
////                        JumpToActivityUtil.jumpNoParams(mActivity, ChatRoomActivity.class, false);
//                        Intent intent=new Intent(mActivity,ChatRoomActivity.class);
//                        startActivity(intent);
////                JumpToActivityUtil.jumpNoParams(getActivity(), ChatsActivity.class, false);
//                    } else {
//                        ToastUtils.showShort("连接异常，重连中...");
//                        connectSDK();
//                    }
//                });

        view.findViewById(R.id.img_matching).setOnClickListener(v -> {
            if (Constant.getChatConnect()) {
                Logger.d("开始跳转。。。。。");
                JumpToActivityUtil.jumpNoParams(getActivity(), ChatRoomActivity.class, false);
//                JumpToActivityUtil.jumpNoParams(getActivity(), ChatsActivity.class, false);
            }
            else {
                ToastUtils.showShort("连接异常，重连中...");
                if (mActivity.UserData() != null) {
                    String uid = mActivity.UserData().getUid() + "";
                    String token = mActivity.UserData().getToken();
                    Log.i("DollRoomActivity", "Home uid：" + uid + "\t\ttoken：" + token);
                    VAChatAPI.getInstance().connect(Config.CHAT_SDK_URL, uid, token);
                }
            }

        });

//        Glide.with(mActivity.getApplicationContext()).asGif().load(R.drawable.icon_waitpair).into(mGifView);

    }

    private void connectSDK() {
        if (mActivity.UserData() != null) {
            String uid = mActivity.UserData().getUid() + "";
            String token = mActivity.UserData().getToken();
//            Log.i("ApplicationHelper", "chat connecting ,uid=="+uid+"\t\ttoken=="+token);
            VAChatAPI.getInstance().connect(Config.CHAT_SDK_URL,uid,token);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (!Constant.getChatConnect()) {
            connectSDK();
        }

        if (isVisible) {
            Log.i("isVisible", "onFragmentVisibleChange: GaChatFragment 显示");
            if (mActivity.UserData() != null) {
                mTvAccount.setText(mActivity.UserData().getUsername());
                mTvRank.setText(mActivity.UserData().getRank());
                mTvDiamondCount.setText(mActivity.UserData().getDiamond() + "");
                mTvCatchDollCount.setText(mActivity.UserData().getClaw_doll_time() + "");
                mTvGiftCount.setText(mActivity.UserData().getGift() + "");
                topView.setBackgroundResource(mActivity.UserData().getGender().equals("male") ? R.drawable.kachat_home_bg_boy : R.drawable.kachat_home_bg_girl);
                userLogo.setImageResource(mActivity.UserData().getGender().equals("male") ? R.drawable.kachathome_logo_boy : R.drawable.kachathome_logo_girl);
            }
        }else {
            Log.i("isVisible", "onFragmentVisibleChange: GaChatFragment 隐藏");
        }
    }

    @Override
    public void onDestroyView() {
        topView=null;
        mTvRank=null;
        userLogo=null;
        mTvAccount=null;
        mTvGiftCount=null;
        mTvDiamondCount=null;
        mTvCatchDollCount=null;
        super.onDestroyView();
    }
}
