package com.gachat.main;

import com.gachat.main.util.SharedPreferencesHelper;

public class Constant {

    public static final boolean IsTrue=true;
    public static final boolean IsFalse=false;

    public static final String LOGIN_STATUE="login_state";
    public static boolean IsFirstIn(){  return SharedPreferencesHelper.getInstance().getBooleanValueByKey(Constant.LOGIN_STATUE);  }

    private static final String CONNECT_CHAT="connect_state_chat";
    public static boolean getChatConnect(){  return SharedPreferencesHelper.getInstance().getBooleanValueByKey(CONNECT_CHAT);  }
    public static void setChatConnect(boolean state){  SharedPreferencesHelper.getInstance().setBooleanValue(CONNECT_CHAT,state);}

    private static final String CONNECT_DOLL="connect_state_doll";
    public static boolean getDollConnect(){  return SharedPreferencesHelper.getInstance().getBooleanValueByKey(CONNECT_DOLL);  }
    public static void setDollConnect(boolean state){  SharedPreferencesHelper.getInstance().setBooleanValue(CONNECT_DOLL,state);}

    private static final String ReadyTime="readyTime";
    public static void setGameReadyTime(int readyTime){ SharedPreferencesHelper.getInstance().setIntValue(ReadyTime,readyTime); }
    public static int getGameReadyTime(){ return SharedPreferencesHelper.getInstance().getIntValueByKey(ReadyTime); }

    private static final String AgainTime="gameTime";
    public static void setAgainTime(int againTime){ SharedPreferencesHelper.getInstance().setIntValue(AgainTime,againTime); }
    public static int getAgainTime(){ return SharedPreferencesHelper.getInstance().getIntValueByKey(AgainTime); }

    private static final String CalleeUserId="calleeUserId";
    public static void setCalleeId(long calleeUserId){ SharedPreferencesHelper.getInstance().setLongValue(CalleeUserId,calleeUserId); }
    public static long getCalleeId(){ return SharedPreferencesHelper.getInstance().getLongValueByKey(CalleeUserId); }


    public static final String USER_MOBILE="user_mobile";
    public static final String USER_PASSWORD="user_password";


}
