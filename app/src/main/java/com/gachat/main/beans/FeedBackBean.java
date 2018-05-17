package com.gachat.main.beans;

import java.io.Serializable;

/**
 *反馈意见
 */

public class FeedBackBean implements Serializable {

    private int feedback_id;
    private String create_time;
    private int user;
    private String content;
    private String mobile;

    public FeedBackBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"feedback_id\":")
                .append(feedback_id);
        sb.append(",\"create_time\":\"")
                .append(create_time).append('\"');
        sb.append(",\"user\":")
                .append(user);
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"mobile\":\"")
                .append(mobile).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
