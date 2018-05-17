package com.gachat.main.event;


/**
 * 全局监听
 */
public interface SdkDollConnectEvent {
    void onConnectionSuccess();
    void onConnectionFailed(String s);
    void onDisconnected(String s);
}
