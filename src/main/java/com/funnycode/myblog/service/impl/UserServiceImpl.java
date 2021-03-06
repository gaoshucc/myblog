package com.funnycode.myblog.service.impl;

import com.funnycode.myblog.mapper.UserMapper;
import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.Position;
import com.funnycode.myblog.pojo.PO.User;
import com.funnycode.myblog.pojo.VO.FavoriteVO;
import com.funnycode.myblog.pojo.VO.FavoritesDetailVO;
import com.funnycode.myblog.pojo.VO.FolloweeVO;
import com.funnycode.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author gaoshucc
 * @create 2018-11-15 22:56
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String userexists(String username) {
        int exists = userMapper.userexists(username);

        return exists > 0 ? "false" : "true";
    }

    @Override
    public String nicknameexists(String nickname) {
        int exists = userMapper.nicknameexists(nickname);

        return exists > 0 ? "false" : "true";
    }

    @Override
    public boolean regist(User user) {
        int regSuccess = userMapper.regist(user);

        return regSuccess > 0 ? true : false;
    }

    @Override
    public User login(User user) {
        User loginUser = userMapper.login(user);

        return loginUser;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.getUserByUsername(username);
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
        return updateUserInfoSuccess > 0 ? true : false;
    }

    @Override
    public Boolean likeNote(String loginUserId, String noteId) {
        return userMapper.likeNote(loginUserId, noteId) > 0 ? true : false;
    }

    @Override
    public Boolean hasLike(String loginUserId, String noteId) {
        return userMapper.hasLike(loginUserId, noteId) > 0 ? true : false;
    }

    @Override
    public Boolean cancelLikeNote(String loginUserId, String noteId) {
        return userMapper.cancelLikeNote(loginUserId, noteId) > 0 ? true : false;
    }

    @Override
    public Boolean collectNote(String loginUserId, String noteId, String collectTime) {
        return userMapper.collectNote(loginUserId, noteId, collectTime);
    }

    @Override
    public Boolean hasCollect(String loginUserId, String noteId) {
        return userMapper.hasCollect(loginUserId, noteId) > 0 ? true : false;
    }

    @Override
    public Boolean cancelCollectNote(String loginUserId, String noteId) {
        return userMapper.cancelCollectNote(loginUserId, noteId) > 0 ? true : false;
    }

    @Override
    public List<FavoriteVO> findFavoritesLimitByUserId(String loginUserId, Integer start, Integer num) {
        return userMapper.findFavoritesLimitByUserId(loginUserId, start, num);
    }

    @Override
    public List<FavoritesDetailVO> findFavoritesByUserId(String loginUserId) {
        return userMapper.findFavoritesByUserId(loginUserId);
    }

    @Override
    public Integer findMyFavoritesCount(String loginUserId) {
        return userMapper.findMyFavoritesCount(loginUserId);
    }

    @Override
    public User findAuthorByAuthorId(String authorId) {
        return userMapper.findAuthorByAuthorId(authorId);
    }

    @Override
    public Boolean updateAttentionStatus(String loginUserId, String attentionId) {
        Integer success;
        Integer status = null;
        //0代表无关系，1代表当前u1已关注u2,2代表u2已关注u1,3代表互相关注
        Integer attentionStatus = userMapper.findAttentionStatusById(loginUserId, attentionId);
        if (attentionStatus != null) {
            if (attentionStatus == 0) {
                status = 1;
            }
            if (attentionStatus == 1) {
                status = 0;
            }
            if (attentionStatus == 2) {
                status = 3;
            }
            if (attentionStatus == 3) {
                status = 2;
            }
            success = userMapper.updateAttentionStatusById(loginUserId, attentionId, status);
        } else {
            Integer reverseAttentionStatus = userMapper.findAttentionStatusById(attentionId, loginUserId);
            if (reverseAttentionStatus == null) {
                status = 1;
                userMapper.addFolloweeById(loginUserId, attentionId);
                success = userMapper.updateAttentionStatusById(loginUserId, attentionId, status);
            } else {
                if (reverseAttentionStatus == 0) {
                    status = 2;
                }
                if (reverseAttentionStatus == 1) {
                    status = 3;
                }
                if (reverseAttentionStatus == 2) {
                    status = 0;
                }
                if (reverseAttentionStatus == 3) {
                    status = 1;
                }
                success = userMapper.updateAttentionStatusById(attentionId, loginUserId, status);
            }
        }

        return success > 0 ? true : false;
    }

    @Override
    public Integer findAttentionStatusById(String loginUserId, String attentionId) {
        return userMapper.findAttentionStatusById(loginUserId, attentionId);
    }

    @Override
    public Set<FolloweeVO> findFolloweeList(String loginUserId) {
        return userMapper.findFolloweeList(loginUserId);
    }

    @Override
    public boolean saveNote(Note note, String userId) {
        Integer num = userMapper.saveNote(note);
        Integer updateExperience = userMapper.updateUserExperienceByUserId(userId);

        return (num > 0 && updateExperience > 0) ? true : false;
    }
}
