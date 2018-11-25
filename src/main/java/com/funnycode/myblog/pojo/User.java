package com.funnycode.myblog.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author gaoshucc
 * @create 2018-11-11 15:49
 */
public class User implements Serializable {
    private String userId;
    private String username;
    private String password;
    private String nickname;
    private Integer status;
    private String lastLogintime;
    private String role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastLogintime() {
        return lastLogintime;
    }

    public void setLastLogintime(String lastLogintime) {
        this.lastLogintime = lastLogintime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String username, String password, String nickname) {
        this.userId = UUID.randomUUID().toString().replace("-","");
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.status = 1;
        this.lastLogintime = null;
        this.role = "user";
    }

    public User(String userId, String username, String password, String nickname, Integer status, String lastLogintime, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.lastLogintime = lastLogintime;
        this.role = role;
    }

    public User() {
    }
}
