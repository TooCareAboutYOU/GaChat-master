package com.gachat.main.beans;

import java.io.Serializable;



public class UserBean implements Serializable{

    private String username;
    private String gender;
    private int diamond;
    private int uid;
    private int age;
    private int character;   //性格
    private String rank;
    private int claw_doll_time;
    private int gift;

    public UserBean() { }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"username\":\"")
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getClaw_doll_time() {
        return claw_doll_time;
    }

    public void setClaw_doll_time(int claw_doll_time) {
        this.claw_doll_time = claw_doll_time;
    }

    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }


}
