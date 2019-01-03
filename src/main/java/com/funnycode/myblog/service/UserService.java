package com.funnycode.myblog.service;

import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.Position;
import com.funnycode.myblog.pojo.PO.User;

import java.util.List;

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
    /**
     * 通过userId获得用户
     * @param userId 用户ID
     * @return User 用户
     */
    User getUserByUserId(String userId);
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
    /**
     * 更新用户信息
     * @param editUser
     * @return boolean 是否更新成功，成功为true，失败为false
     */
    boolean updateUserInfo(User editUser);
    /**
     * 点赞手记
     * @param loginUserId,noteId 用户ID，手记ID
     * @return boolean 是否点赞成功，成功为true，失败为false
     */
    Boolean likeNote(String loginUserId, String noteId);
    /**
     * 是否已点赞手记
     * @param loginUserId,noteId 用户ID，手记ID
     * @return boolean 是否已点赞，已点赞为true，未点赞为false
     */
    Boolean hasLike(String loginUserId, String noteId);
    /**
     * 取消点赞手记
     * @param loginUserId,noteId 用户ID，手记ID
     * @return boolean 是否取消点赞成功，成功为true，失败为false
     */
    Boolean cancelLikeNote(String loginUserId, String noteId);
    /**
     * 获得作者信息（包括手记、问答）
     * @param authorId 作者ID
     * @return User 用户对象
     */
    User findAuthorByAuthorId(String authorId);
    /**
     * 关注ta
     * @param loginUserId,followeeId 当前用户ID,关注的人ID
     * @return Boolean 是否添加成功
     */
    Boolean addFolloweeByFolloweeId(String loginUserId, String followeeId);
    /**
     * 取消关注ta
     * @param loginUserId,followeeId 当前用户ID,关注的人ID
     * @return Boolean 是否取消关注成功
     */
    Boolean removeFolloweeByFolloweeId(String loginUserId, String followeeId);
}
