package com.funnycode.myblog.service;

import com.funnycode.myblog.pojo.Admin;
import com.funnycode.myblog.pojo.User;

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

    boolean regist(Admin register);

    String adminexists(String adminname);

    String nicknameexists(String nickname);

    String getRoleByAdminname(String adminname);
}
