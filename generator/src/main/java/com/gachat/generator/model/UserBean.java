package com.gachat.generator.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class UserBean {
    @Id(autoincrement = true)
    private Long id;
    private boolean isLogin;
    private String token;
    private String username;
    private String gender;
    private int diamond;
    private int uid;
    private int age;
    private int character;   //性格
    private String rank;
    private int claw_doll_time;
    private int gift;
    @Generated(hash = 498267936)
    public UserBean(Long id, boolean isLogin, String token, String username,
            String gender, int diamond, int uid, int age, int character,
            String rank, int claw_doll_time, int gift) {
        this.id = id;
        this.isLogin = isLogin;
        this.token = token;
        this.username = username;
        this.gender = gender;
        this.diamond = diamond;
        this.uid = uid;
        this.age = age;
        this.character = character;
        this.rank = rank;
        this.claw_doll_time = claw_doll_time;
        this.gift = gift;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getDiamond() {
        return this.diamond;
    }
    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }
    public int getUid() {
        return this.uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getCharacter() {
        return this.character;
    }
    public void setCharacter(int character) {
        this.character = character;
    }
    public String getRank() {
        return this.rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public int getClaw_doll_time() {
        return this.claw_doll_time;
    }
    public void setClaw_doll_time(int claw_doll_time) {
        this.claw_doll_time = claw_doll_time;
    }
    public int getGift() {
        return this.gift;
    }
    public void setGift(int gift) {
        this.gift = gift;
    }
    public boolean getIsLogin() {
        return this.isLogin;
    }
    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"isLogin\":")
                .append(isLogin);
        sb.append(",\"token\":\"")
                .append(token).append('\"');
        sb.append(",\"username\":\"")
                .append(username).append('\"');
        sb.append(",\"gender\":\"")
                .append(gender).append('\"');
        sb.append(",\"diamond\":")
                .append(diamond);
        sb.append(",\"uid\":")
                .append(uid);
        sb.append(",\"age\":")
                .append(age);
        sb.append(",\"character\":")
                .append(character);
        sb.append(",\"rank\":\"")
                .append(rank).append('\"');
        sb.append(",\"claw_doll_time\":")
                .append(claw_doll_time);
        sb.append(",\"gift\":")
                .append(gift);
        sb.append('}');
        return sb.toString();
    }
}
