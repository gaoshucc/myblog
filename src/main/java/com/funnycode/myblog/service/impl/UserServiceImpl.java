package com.funnycode.myblog.service.impl;

import com.funnycode.myblog.mapper.UserMapper;
import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.Position;
import com.funnycode.myblog.pojo.PO.User;
import com.funnycode.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @Override
    public String updateUserLastLogintimeByUsername(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastLogintime = sdf.format(new Date());
        user.setLastLogintime(lastLogintime);
        userMapper.updateUserLastLogintimeByUsername(user);

        return lastLogintime;
    }

    @Override
    public User getUserByUserId(String userId) {
        return userMapper.getUserByUserId(userId);
    }

    @Override
    public User findEditableUserInfo(String userId) {
        return userMapper.findEditableUserInfo(userId);
    }

    @Override
    public List<Position> findAllPosition() {
        return userMapper.findAllPosition();
    }

    @Override
    public boolean updateUserInfo(User editUser) {
        int updateUserInfoSuccess = userMapper.updateUserInfo(editUser);
        return updateUserInfoSuccess>0?true:false;
    }

    @Override
    public Boolean likeNote(String loginUserId, String noteId) {
        return userMapper.likeNote(loginUserId,noteId)>0?true:false;
    }

    @Override
    public Boolean hasLike(String loginUserId, String noteId) {
        return userMapper.hasLike(loginUserId,noteId)>0?true:false;
    }

    @Override
    public Boolean cancelLikeNote(String loginUserId, String noteId) {
        return userMapper.cancelLikeNote(loginUserId,noteId)>0?true:false;
    }

    @Override
    public User findAuthorByAuthorId(String authorId) {
        return userMapper.findAuthorByAuthorId(authorId);
    }

    @Override
    public Boolean addFolloweeByFolloweeId(String loginUserId, String followeeId) {
        return userMapper.addFolloweeByFolloweeId(loginUserId,followeeId)>0?true:false;
    }

    @Override
    public Boolean removeFolloweeByFolloweeId(String loginUserId, String followeeId) {
        return userMapper.removeFolloweeByFolloweeId(loginUserId,followeeId)>0?true:false;
    }

    @Override
    public boolean saveNote(Note note, String userId) {
        Integer num = userMapper.saveNote(note);
        Integer updateExperience = userMapper.updateUserExperienceByUserId(userId);

        return (num>0&&updateExperience>0)?true:false;
    }
}
