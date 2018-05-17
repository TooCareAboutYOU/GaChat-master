package com.gachat.main.event;

import com.dnion.VAChatSignaling;

/**
 * Created by admin on 2018/3/31.
 */

public interface SdkChatEvent {
    void onChatConnectionSuccess();
    void onChatConnectionFailed(String s);
    void onChatDisconnected(String s);
    void onChatStart(VAChatSignaling.ChatInfo data);
    void onChatFinish();
    void onChatTerminate();
    void onChatCancel();
    void onGotDoll(long l);
    void onChatGotUserVideo();
    void onChatLostUserVideo();
}
