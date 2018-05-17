package com.gachat.main.event;

/**
 * 全局监听
 */

public interface SdkChatConnectEvent {
    void onChatConnectionSuccess();
    void onChatConnectionFailed(String s);
    void onChatDisconnected(String s);
}
