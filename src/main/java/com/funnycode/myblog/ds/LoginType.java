package com.funnycode.myblog.ds;

/**
 * @author gaoshucc
 * @create 2018-11-22 16:18
 */
public enum LoginType {
    USER("User"), ADMIN("Admin");

    private String loginType;

    LoginType(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return this.loginType.toString();
    }
}
