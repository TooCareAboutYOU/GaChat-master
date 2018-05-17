package com.gachat.main.beans;

import java.io.Serializable;


public class RegisterBean implements Serializable{

    private static final long serialVersionUID = -1563310168132231501L;
    private UserBean user;
    private String token;


    public RegisterBean() {
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"user\":")
                .append(user);
        sb.append(",\"token\":\"")
                .append(token).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
