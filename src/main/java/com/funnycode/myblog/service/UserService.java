package com.funnycode.myblog.service;

import com.funnycode.myblog.pojo.Note;
import com.funnycode.myblog.pojo.User;

/**
 * @author gaoshucc
 * @create 2018-11-15 22:55
 */
public interface UserService {
    /**
     * 判断用户是否已存在
     * @param username
     * @return String
     */
    String userexists(String username);
    /**
     * 判断昵称是否已存在
     * @param nickname 昵称
     * @return String
     */
    String nicknameexists(String nickname);
    /**
     * 注册
     * @param user
     * @return boolean
     */
    boolean regist(User user);
    /**
     * 登录
     * @param user
     * @return User
     */
    User login(User user);
    /**
     * 通过用户名查找用户
     * @param username
     * @return User
     */
    User getUserByUsername(String username);
    /**
     * 通过用户查找用户
     * @param username
     * @return String
     */
    String getPasswordByUsername(String username);
    /**
     * 通过用户名查找用户角色
     * @param username
     * @return String
     */
    String getRoleByUsername(String username);
    /**
     * 更新用户的最后登录时间
     * @param user
     * @return void
     */
    String updateUserLastLogintimeByUsername(User user);
    /**
     * 保存并发布手记
     * @param note 手记
     * @param userId
     * @return boolean 是否插入成功，成功返回true,失败返回false
     */
    boolean saveNote(Note note, String userId);

    User getUserByUserId(String userId);
}
