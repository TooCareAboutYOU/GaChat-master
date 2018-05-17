package com.gachat.main.beans;

import java.io.Serializable;
import java.util.List;


public class ErrorBean implements Serializable {
    public List<String> message;

    public ErrorBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"message\":")
                .append(message);
        sb.append('}');
        return sb.toString();
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
