package com.funnycode.myblog.pojo.PO;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author gaoshucc
 * @create 2018-11-22 19:10
 */
public class Admin implements Serializable{

    private String adminId;
    private String adminname;
    private String password;
    private String nickname;

    private String role;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Admin(String adminname, String newPassword, String nickname) {
        this.adminId = UUID.randomUUID().toString().replace("-","");
        this.adminname = adminname;
        this.password = newPassword;
        this.nickname = nickname;
        this.role = "admin";
    }

    public Admin(String adminId, String adminname, String password, String nickname, String role) {
        this.adminId = adminId;
        this.adminname = adminname;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public Admin() {
    }
}
