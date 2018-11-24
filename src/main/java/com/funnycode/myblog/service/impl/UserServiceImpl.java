package com.funnycode.myblog.service.impl;

import com.funnycode.myblog.mapper.UserMapper;
import com.funnycode.myblog.pojo.User;
import com.funnycode.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gaoshucc
 * @create 2018-11-15 22:56
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public String userexists(String username) {
        int exists = userMapper.userexists(username);

        return exists>0?"false":"true";
    }

    @Override
    public String nicknameexists(String nickname) {
        int exists = userMapper.nicknameexists(nickname);

        return exists>0?"false":"true";
    }

    @Override
    public boolean regist(User user) {
        int regSuccess = userMapper.regist(user);

        return regSuccess>0?true:false;
    }

    @Override
    public User login(User user) {
        User loginUser = userMapper.login(user);

        return loginUser;
    }

    @Override
    public User getUserByUsername(String username) {
        User user =  userMapper.getUserByUsername(username);
        return user;
    }

    @Override
    public String getPasswordByUsername(String username) {
        String password = userMapper.getPasswordByUsername(username);

        return password;
    }

    @Override
    public String getRoleByUsername(String username) {
        String role = userMapper.getRoleByUsername(username);

        return role;
    }
}
