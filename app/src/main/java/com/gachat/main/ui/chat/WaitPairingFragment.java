package com.gachat.main.ui.chat;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dnion.VAChatAPI;
import com.gachat.main.R;
import com.gachat.main.base.BaseDialogFragment;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * 匹配中ing
 */
public class WaitPairingFragment extends BaseDialogFragment {

    public static final String TAG = "WaitPairingFragment";

    @SuppressLint("StaticFieldLeak")
    private static WaitPairingFragment instance=null;
    public WaitPairingFragment(){}
    public static synchronized WaitPairingFragment getInstance(){
        if(instance==null){
            instance=new WaitPairingFragment();
        }
        return instance;
    }

    private boolean isExist=false;

    private ImageView mBack;

    private WeakReference<ChatRoomActivity> mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=new WeakReference<>((ChatRoomActivity) context);
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void show(FragmentManager manager, String tag) {
        Log.i(TAG, "show: 1");
        if (!isExist) {
            Log.i(TAG, "show: 2");
            super.show(manager, tag);
            isExist = !isExist;
            Log.i(TAG, "show: "+isExist);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.i(TAG, "onDismiss: 1 "+isExist);
        if (isExist) {
            Log.i(TAG, "onDismiss: 2");
            super.dismiss();
        }
    }

    @Override
    public int setResLayoutId() {  return R.layout.fragment_wait_pairing;  }

    @Override
    public void initView(View view) {
        mBack=view.findViewById(R.id.iv_back);
        mBack.setOnClickListener(v -> {
            mActivity.get().mImgStartChat.setVisibility(View.VISIBLE);
            VAChatAPI.getInstance().cancelPair();
            dismiss();
        });
    }

    private DisplayMetrics metrics;
    @Override
    public void onStart() {
        super.onStart();
        metrics=new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Objects.requireNonNull(getDialog().getWindow()).setLayout(metrics.widthPixels,metrics.heightPixels);//getDialog().getWindow().getAttributes().height
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        getFocus();
//    }
//
//    //仅用于fragment页面上没有其他获取焦点的View的情况下
//    private void getFocus() {
//        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener((v, keyCode, event) -> {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                mActivity.get().runOnUiThread(() -> {
//                    getDialog().dismiss();
//                    mActivity.get().mImgStartChat.setVisibility(View.VISIBLE);
//
//                });
//                return true;
//            }
//            return false;
//        });
//    }

    @Override
    public void onDestroyView() {
        mBack=null;
        metrics=null;
        isExist=false;
        super.onDestroyView();
    }
}
