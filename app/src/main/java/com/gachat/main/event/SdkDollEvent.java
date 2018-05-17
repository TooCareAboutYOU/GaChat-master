package com.gachat.main.event;

import com.dnion.DollRoomSignaling;
import com.dnion.VADollSignaling;

public interface SdkDollEvent {
    void onConnectionSuccess();
    void onConnectionFailed(String s);
    void onDisconnected(String s);
    void onJoinRoom(DollRoomSignaling.DOLLRoomInfo info);
    void onJoinRoomFailed(String s, String s1);
    void onLeaveRoom();
    void onReadyGame();
    void onCatchResult(int i);
    void onQueueInfoUpdate(DollRoomSignaling.DOLLRoomInfo dollRoomInfo,DollRoomSignaling.DOLLVAUserInfo dollvaUserInfo);
    void onStartGame(long l);
    void onStartGameFailed(long l, String s);
    void onRefreshRoomInfo(VADollSignaling.RoomInfo roomInfo);
    void onUserJoin(VADollSignaling.UserInfo userInfo);
    void onUserUpdate(VADollSignaling.UserInfo userInfo);
    void onUserLeave(VADollSignaling.UserInfo userInfo);
    void onGotUserVideo(String s);
    void onLostUserVideo(String s);
    void onTextMessage(String s, String s1);
}
