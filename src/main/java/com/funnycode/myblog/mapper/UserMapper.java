package com.funnycode.myblog.mapper;

import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.Position;
import com.funnycode.myblog.pojo.PO.User;
import com.funnycode.myblog.pojo.VO.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-11-11 15:53
 */
@Mapper
@Component("userMapper")
public interface UserMapper {
    /**
     * 判断用户是否已存在
     * @param username
     * @return int
     */
    int userexists(String username);
    /**
     * 判断昵称是否已存在
     * @param nickname
     * @return int
     * */
    int nicknameexists(String nickname);
    /**
     * 用户注册
     * @param user
     * @return int
     */
    int regist(User user);
    /**
     * 用户登录
     * @param user
     * @return int
     */
    User login(User user);
    /**
     * 通过用户名查找用户
     * @param username
     * @return User
     */
    User getUserByUsername(String username);
    /**
     * 通过用户名获得用户密码
     * @param username
     * @return String
     */
    String getPasswordByUsername(String username);
    /**
     * 通过用户名获得用户角色
     * @param username
     * @return String
     */
    String getRoleByUsername(String username);
    /**
     * 通过用户名更新用户最后登录时间
     * @param user 用户名,最后登录时间
     * @return void
     */
    void updateUserLastLogintimeByUsername(User user);
    /**
     * 通过用户id查找用户
     * @param userId 用户id
     * @return User
     */
    User getUserByUserId(String userId);
    /**
     * 通过用户id查找该手记的作者
     * @param userId 用户id
     * @return User 手记作者
     */
    User findBloggerByUserId(String userId);
    /**
     * 通过用户id查找该问题的提问者
     * @param userId 用户id
     * @return User 提问者
     */
    User findQuizzerByUserId(String userId);
    /**
     * 保存并发布手记
     * @param note 手记
     * @return Integer 所插入表的受影响行数
     */
    Integer saveNote(Note note);
    /**
     * 更新用户经验值
     * @param userId 用户ID
     * @return Integer 受影响行数
     */
    Integer updateUserExperienceByUserId(String userId);
    /**
     * 查找评论者
     * @param userId 用户ID
     * @return UserVO 返回UserVO
     */
    UserVO findObserver(String userId);
    /**
     * 通过userId获得用户可编辑的信息项
     * @param userId 用户ID
     * @return User 用户
     */
    User findEditableUserInfo(String userId);
    /**
     * 获得所有职位
     * @param
     * @return List<Position> 职位集合
     */
    List<Position> findAllPosition();
}
