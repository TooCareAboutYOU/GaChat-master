package com.gachat.main.util.manager;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;


public class FingerprintManagerUtils {

    private static final String TAG = "FingerprintManagerUtils";

    private static Context mContext;

     private volatile static FingerprintManagerUtils singleton;
     private FingerprintManagerUtils (){}
     public static synchronized FingerprintManagerUtils getInstance() {
         if (singleton == null) {
              synchronized (FingerprintManagerUtils.class) {
                   if (singleton == null) {
                       singleton = new FingerprintManagerUtils();
                   }
              }
          }
         return singleton;
     }

    public interface OnCallBackListenr{
        void onDeviceSupportFailed();
        void onInsecurity();
        void onEnrollFailed();
        void onAuthenticationStart();
        void onAuthenticationError(int errMsgId, CharSequence errString);
        void onAuthenticationFailed();
        void onAuthenticationHelp(int helpMsgId, CharSequence helpString);
        void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);
    }



    //这种使用的是v4的兼容包，推荐使用这种
    private static FingerprintManagerCompat mFingerManager;
    //这种是使用系统服务，但是必须要在sdk为23以上版本才行
//     private static FingerprintManager sManager;

    private static KeyguardManager mKeyguardManager;
    private CancellationSignal mCancellationSignal;

     //取值判断即可
    public static final String HAS_FINGERPRINT_API = "hasFingerPrintApi";
    public static final String SETTINGS = "settings";

    //判断是否存在指纹类
     public static void init(Context context){
         mContext=context.getApplicationContext();
         SharedPreferences sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
         if (sp.contains(HAS_FINGERPRINT_API)) { // 检查是否存在该值，不必每次都通过反射来检查
             return;
         }
         SharedPreferences.Editor editor = sp.edit();
         try {
             Class.forName("android.hardware.fingerprint.FingerprintManager"); // 通过反射判断是否存在该类
             editor.putBoolean(HAS_FINGERPRINT_API, true);
             Log.i(TAG, "init: ");
         } catch (ClassNotFoundException e) {
             editor.putBoolean(HAS_FINGERPRINT_API, false);
             e.printStackTrace();
         }
         editor.apply();
     }


    public void startAuth(final OnCallBackListenr listenr){
        if (mFingerManager == null) {
            mFingerManager = FingerprintManagerCompat.from(mContext);

            if (!mFingerManager.isHardwareDetected()) {
                //判断是否有硬件支持
                if (listenr != null) {
                    listenr.onDeviceSupportFailed();
                }
                return;
            }

            if (!mFingerManager.hasEnrolledFingerprints()) {
                //判断是否录入至少一个指纹
                if (listenr != null) {
                    listenr.onEnrollFailed();
                }
                return;
            }

            mKeyguardManager= (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
            if (!mKeyguardManager.isKeyguardSecure()) {    //判断设备是否处于安全保护中
                if (listenr != null) {
                    listenr.onInsecurity();
                }
                return;
            }

            if (mCancellationSignal == null) {
                mCancellationSignal = new CancellationSignal();
            }
            if (listenr != null) {
                listenr.onAuthenticationStart();
                mFingerManager.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        if (listenr != null) {
                            listenr.onAuthenticationError(errMsgId,errString);
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        if (listenr != null) {
                            listenr.onAuthenticationHelp(helpMsgId,helpString);
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        if (listenr != null) {
                            listenr.onAuthenticationSucceeded(result);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        if (listenr != null) {
                            listenr.onAuthenticationFailed();
                        }
                    }
                }, null);
            }

        }
    }

    public void cancel(){
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
        }
    }

    private class MyAuthenticationCallback extends FingerprintManagerCompat.AuthenticationCallback{
        public MyAuthenticationCallback() {
            Log.i(TAG, "MyAuthenticationCallback: ");
            if (mContext !=null) {
                Toast.makeText(mContext, "请输入指纹", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.i(TAG, "onAuthenticationError: ");
            Toast.makeText(mContext, "解锁异常："+errMsgId+"\t\t"+errString.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.i(TAG, "onAuthenticationHelp: ");
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Log.i(TAG, "onAuthenticationSucceeded: "+result);
            Toast.makeText(mContext, "解锁成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationFailed() {
            Log.i(TAG, "onAuthenticationFailed: ");
            //            handler.sendMessageDelayed(new Message(), 1000 * 30);  //  测试30s才能重新启用验证
            Toast.makeText(mContext, "解锁失败,30s后可重新输入", Toast.LENGTH_SHORT).show();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: 重启指纹模块");
            mFingerManager.authenticate(null, 0, null,new MyAuthenticationCallback(), handler);
        }
    };

}
