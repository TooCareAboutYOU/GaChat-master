package com.gachat.main.beans;

import java.io.Serializable;


public class CheckSmsCodeBean implements Serializable {
    private static final long serialVersionUID = 7663614297486408563L;
    private String message;

    public CheckSmsCodeBean() {
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
