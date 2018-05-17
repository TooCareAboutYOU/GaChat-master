package com.gachat.main.beans;

import java.io.Serializable;

/**
 * 短信获取验证码
 */

public class SmsCodeBean implements Serializable {

    private static final long serialVersionUID = -4904186723892010658L;
    private String captcha;
    private String mobile;
    private int expire;

    public SmsCodeBean() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"captcha\":\"")
                .append(captcha).append('\"');
        sb.append(",\"mobile\":\"")
                .append(mobile).append('\"');
        sb.append(",\"expire\":")
                .append(expire);
        sb.append('}');
        return sb.toString();
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
