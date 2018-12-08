package com.funnycode.myblog.mapper;

import com.funnycode.myblog.pojo.PO.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author gaoshucc
 * @create 2018-11-22 19:31
 */
@Mapper
@Component("adminMapper")
public interface AdminMapper {
    /**
     * 判断管理员账号是否已存在
     *
     * @param adminname 注册账号
     * @return int 受影响的行数
     */
    int adminexists(String adminname);

    /**
     * 判断管理员昵称是否已存在
     *
     * @param nickname 注册账号的昵称
     * @return int 受影响的行数
     */
    int nicknameexists(String nickname);

    /**
     * 通过Adminname获取Admin
     *
     * @param username 管理员账号
     * @return Admin 管理员对象
     */
    Admin getAdminByAdminname(String username);

    /**
     * 注册
     *
     * @param register 注册者
     * @return int 受影响的行数
     */
    int regist(Admin register);

    /**
     * 通过管理员账号获得角色
     *
     * @param adminname 管理员账号
     * @return String 角色
     */
    String getRoleByAdminname(String adminname);
}
