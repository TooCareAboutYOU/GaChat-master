package com.gachat.main.base.broadcastreciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.blankj.utilcode.util.NetworkUtils;

public class NetWorkBroadCastReceiver extends BroadcastReceiver {

    public static final String ACTION=ConnectivityManager.CONNECTIVITY_ACTION;


    public interface onNetWorkListener{
        void onConnected(boolean isConnect);
    }
    private onNetWorkListener mListener;
    public void setOnNetWorkListener(onNetWorkListener listener){this.mListener=listener; }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean newWorkState= NetworkUtils.isConnected();
            if (mListener != null) {
//                Toast.makeText(context, "连接网：" + newWorkState, Toast.LENGTH_SHORT).show();
                mListener.onConnected(newWorkState);
            }
        }
    }

}
