package com.gachat.main.beans;

import java.io.Serializable;

/**
 * Created by zs on 2018/4/23.
 */
public class MessageBean implements Serializable {
    private String message;

    public MessageBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"message\":\"")
                .append(message).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
