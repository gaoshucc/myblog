package com.funnycode.myblog.pojo.PO;

import java.io.Serializable;
import java.util.List;
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
    private String regtime;
    private Position position;
    private String profilePath;
    private String lastLogintime;
    private Integer status;

    private List<Note> notes;
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
        Position position = new Position(0);
        this.userId = UUID.randomUUID().toString().replace("-","");
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.gender = null;
        this.motto = "学无止境，乐在其中";
        this.position = position;
        this.experience = 100;
        this.role = "user";
        this.profilePath = "nologin.png";
        this.lastLogintime = null;
        this.regtime = regtime;
        this.status = 1;
        this.notes = null;
        this.questions = null;
    }

    public User(String userId, String username, String password, String nickname, Integer experience, String role, Integer gender, String motto, String regtime, Position position, String profilePath, String lastLogintime, Integer status, List<Note> notes, List<Question> questions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.experience = experience;
        this.role = role;
        this.gender = gender;
        this.motto = motto;
        this.regtime = regtime;
        this.position = position;
        this.profilePath = profilePath;
        this.lastLogintime = lastLogintime;
        this.status = status;
        this.notes = notes;
        this.questions = questions;
    }

    public User() {
    }
}
