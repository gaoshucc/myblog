package com.funnycode.myblog.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    private Integer experience;
    private String role;
    private Integer gender;
    private String motto;
    private Integer positionId;
    private String profilePath;
    private String regtime;
    private String lastLogintime;
    private Integer status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public User(String username, String password, String nickname, String regtime) {
        this.userId = UUID.randomUUID().toString().replace("-","");
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.gender = null;
        this.motto = "学无止境，乐在其中";
        this.positionId = 1;
        this.experience = 100;
        this.role = "user";
        this.profilePath = "nologin.png";
        this.lastLogintime = null;
        this.regtime = regtime;
        this.status = 1;
    }

    public User(String userId, String username, String password, String nickname, Integer experience, String role, Integer gender, String motto, Integer positionId, String profilePath, String regtime, String lastLogintime, Integer status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.experience = experience;
        this.role = role;
        this.gender = gender;
        this.motto = motto;
        this.positionId = positionId;
        this.profilePath = profilePath;
        this.regtime = regtime;
        this.lastLogintime = lastLogintime;
        this.status = status;
    }

    public User() {
    }
}
