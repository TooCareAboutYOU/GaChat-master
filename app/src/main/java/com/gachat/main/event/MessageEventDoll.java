package com.gachat.main.event;


import com.dnion.DollRoomSignaling;
import com.dnion.VADollSignaling;

public class MessageEventDoll {

    public static class onDollConnectState{
        int states;
        String msg;

        public onDollConnectState(int states, String msg) {
            this.states = states;
            this.msg = msg;
        }
        public int getStates() {  return states;  }
        public String getMsg() {  return msg;  }
    }

    public static class onJoinRoom{
        DollRoomSignaling.DOLLRoomInfo info;

        public onJoinRoom(DollRoomSignaling.DOLLRoomInfo info) {
            this.info = info;
        }
        public DollRoomSignaling.DOLLRoomInfo getInfo() {  return info;  }
    }

    public static class onJoinRoomFailed{
        String msg1;
        String msg2;

        public onJoinRoomFailed(String msg1, String msg2) {
            this.msg1 = msg1;
            this.msg2 = msg2;
        }

        public String getMsg1() {
            return msg1;
        }

        public String getMsg2() {
            return msg2;
        }
    }

    public static class onLeaveRoom{
        public onLeaveRoom() { }
    }

    public static class onReadyGame{
        public onReadyGame() {  }
    }

    public static class onCatchResult{
        int result;

        public onCatchResult(int result) {
            this.result = result;
        }

        public int getResult() {  return result;  }
    }

    public static class onQueueInfoUpdate{
        DollRoomSignaling.DOLLRoomInfo roomInfo;
        DollRoomSignaling.DOLLVAUserInfo userInfo;

        public onQueueInfoUpdate(DollRoomSignaling.DOLLRoomInfo roomInfo, DollRoomSignaling.DOLLVAUserInfo userInfo) {
            this.roomInfo = roomInfo;
            this.userInfo = userInfo;
        }
        public DollRoomSignaling.DOLLRoomInfo getRoomInfo() {  return roomInfo;  }
        public DollRoomSignaling.DOLLVAUserInfo getUserInfo() {  return userInfo;  }
    }

    public static class onStartGame{
        long golden;

        public onStartGame(long golden) {
            golden = golden;
        }
        public long getGolden() {  return golden;  }
    }

    public static class onStartGameFailed{
        long mLong;
        String msg;

        public onStartGameFailed(long aLong, String msg) {
            mLong = aLong;
            this.msg = msg;
        }
        public long getLong() {  return mLong;  }
        public String getMsg() {  return msg;  }
    }

    public static class onRefreshRoomInfo{
        VADollSignaling.RoomInfo roomInfo;

        public onRefreshRoomInfo(VADollSignaling.RoomInfo roomInfo) {
            this.roomInfo = roomInfo;
        }
        public VADollSignaling.RoomInfo getRoomInfo() {  return roomInfo;  }
    }

    public static class onUserJoin{
        VADollSignaling.UserInfo userInf;

        public onUserJoin(VADollSignaling.UserInfo userInf) {
            this.userInf = userInf;
        }

        public VADollSignaling.UserInfo getUserInf() {  return userInf;  }
    }

    public static class onUserUpdate{
        VADollSignaling.UserInfo userInf;

        public onUserUpdate(VADollSignaling.UserInfo userInf) {
            this.userInf = userInf;
        }

        public VADollSignaling.UserInfo getUserInf() {  return userInf;  }
    }

    public static class onUserLeave{
        VADollSignaling.UserInfo userInf;

        public onUserLeave(VADollSignaling.UserInfo userInf) {
            this.userInf = userInf;
        }

        public VADollSignaling.UserInfo getUserInf() {  return userInf;  }
    }

    public static class onGotUserVideo{
        String msg;

        public onGotUserVideo(String msg) {
            this.msg = msg;
        }
        public String getMsg() {  return msg;  }
    }

    public static class onLostUserVideo{
        String msg;

        public onLostUserVideo(String msg) {
            this.msg = msg;
        }
        public String getMsg() {  return msg;  }
    }

    public static class onTextMessage{
        String msg1;
        String msg2;

        public onTextMessage(String msg1, String msg2) {
            this.msg1 = msg1;
            this.msg2 = msg2;
        }

        public String getMsg1() {
            return msg1;
        }

        public String getMsg2() {
            return msg2;
        }
    }


}
