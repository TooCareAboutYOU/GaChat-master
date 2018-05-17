package com.gachat.main.event;


public class MessageEventDoll {

    public static class onDollConnectState{
        int states;

        public onDollConnectState(int states) {
            this.states = states;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"states\":")
                    .append(states);
            sb.append('}');
            return sb.toString();
        }

        public int getStates() {
            return states;
        }
    }
}
