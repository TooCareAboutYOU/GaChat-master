package com.gachat.main.event;


import com.dnion.VAChatSignaling;

public class MessageEventChat {

    public static class onChatConnectState {
        int states;
        String msg;
        public onChatConnectState(int states, String msg) {
            this.states = states;
            this.msg = msg;
        }
        public int getStates() {   return states;   }
        public String getMsg() {  return msg;  }
    }

    public static class onChatStart{
        VAChatSignaling.ChatInfo chatInfo;

        public onChatStart(VAChatSignaling.ChatInfo chatInfo) {
            this.chatInfo = chatInfo;
        }

        public VAChatSignaling.ChatInfo getChatInfo() {  return chatInfo;  }
    }

    public static class onChatFinish{
        public onChatFinish() {
        }
    }

    public static class onChatTerminate{
        public onChatTerminate() {
        }
    }

    public static class onChatCancel{
        public onChatCancel() {
        }
    }

    public static class onGotDoll{
        long dollId;

        public onGotDoll(long dollId) {
            this.dollId = dollId;
        }

        public long getDollId() {   return dollId;   }
    }

    public static class onGotUserVideo{
        public onGotUserVideo() {
        }
    }

    public static class onLostUserVideo{
        public onLostUserVideo() {
        }
    }

}