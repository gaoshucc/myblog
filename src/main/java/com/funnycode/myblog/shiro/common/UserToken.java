package com.funnycode.myblog.shiro.common;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author gaoshucc
 * @create 2018-11-22 16:44
 */
public class UserToken extends UsernamePasswordToken {
    /**
     * 登录类型
     */
    private String loginType;

    public UserToken(String username, String password, boolean rememberMe, String loginType){
        super(username, password, rememberMe);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
