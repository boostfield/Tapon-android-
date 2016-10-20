package com.caoyang.tapon.model;


import com.caoyang.tapon.model.base.BaseM;

import java.io.Serializable;

/**
 * 第仨房登陆信息
 */
public class ThirtyPartyInfoM extends BaseM implements Serializable {

    private String uid;
    private String nickName;
    private String token;
    private String sex;
    private String avatar;
    private String plat;

    public ThirtyPartyInfoM(String uid, String nickName, String token, String sex, String avatar, String plat) {
        this.uid = uid;
        this.nickName = nickName;
        this.token = token;
        this.sex = sex;
        this.avatar = avatar;
        this.plat = plat;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }
}

