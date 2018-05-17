package com.gachat.main.beans;


import java.io.Serializable;

public class LoginBean implements Serializable {

        private String token;
        private UserBean user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"token\":\"")
                .append(token).append('\"');
        sb.append(",\"user\":")
                .append(user);
        sb.append('}');
        return sb.toString();
    }
}
