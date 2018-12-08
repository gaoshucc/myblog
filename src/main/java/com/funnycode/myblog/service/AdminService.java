package com.funnycode.myblog.service;

import com.funnycode.myblog.pojo.PO.Admin;

/**
 * @author gaoshucc
 * @create 2018-11-22 19:07
 */
public interface AdminService {
    /**
     * 通过Adminname获取Admin
     *
     * @param username 管理员账号
     * @return Admin 管理员对象
     */
    Admin getAdminByAdminname(String username);
    /**
     * 注册管理员
     *
     * @param register 注册管理员
     * @return Admin 注册成功返回true,失败返回false
     */
    boolean regist(Admin register);
    /**
     * 判断adminname是否存在
     *
     * @param adminname 管理员
     * @return String 昵称存在返回“true”,不存在返回“false”
     */
    String adminexists(String adminname);
    /**
     * 判断nickname是否存在
     *
     * @param nickname 用户昵称
     * @return String 昵称存在返回“true”,不存在返回“false”
     */
    String nicknameexists(String nickname);
    /**
     * 通过adminname获取角色
     *
     * @param adminname 管理员
     * @return String 角色
     */
    String getRoleByAdminname(String adminname);
}
