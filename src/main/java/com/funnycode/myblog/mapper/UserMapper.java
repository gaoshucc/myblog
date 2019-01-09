package com.funnycode.myblog.mapper;

import com.funnycode.myblog.pojo.PO.Note;
import com.funnycode.myblog.pojo.PO.Position;
import com.funnycode.myblog.pojo.PO.User;
import com.funnycode.myblog.pojo.VO.FavoriteVO;
import com.funnycode.myblog.pojo.VO.FavoritesDetailVO;
import com.funnycode.myblog.pojo.VO.FolloweeVO;
import com.funnycode.myblog.pojo.VO.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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
    /**
     * 更新用户信息
     * @param editUser
     * @return int 影响的数据行数
     */
    int updateUserInfo(User editUser);
    /**
     * 点赞手记
     * @param loginUserId,noteId 用户ID，手记ID
     * @return boolean 是否点赞成功，成功为true，失败为false
     */
    int likeNote(String loginUserId, String noteId);
    /**
     * 是否已点赞手记
     * @param loginUserId,noteId 用户ID，手记ID
     * @return Integer 受影响行数
     */
    Integer hasLike(String loginUserId, String noteId);
    /**
     * 取消点赞手记
     * @param loginUserId 用户ID
     * @param noteId 手记ID
     * @return int 受影响行数
     */
    int cancelLikeNote(String loginUserId, String noteId);
    /**
     * 获得作者信息（包括手记、问答）
     * @param authorId 作者ID
     * @return User 用户对象
     */
    User findAuthorByAuthorId(String authorId);
    /**
     * 根据ID获取关注状态
     * @param loginUserId 用户ID
     * @param attentionId 关注的人ID
     * @return 关注状态
     */
    Integer findAttentionStatusById(String loginUserId, String attentionId);
    /**
     * 新增关注关系
     * @param loginUserId 用户ID
     * @param attentionId 关注的人ID
     * @return 影响数据行数
     */
    Integer addFolloweeById(String loginUserId, String attentionId);
    /**
     * 更新关注状态
     * @param loginUserId 用户ID
     * @param attentionId 关注的人ID
     * @param status 关注状态
     * @return 影响数据行数
     */
    Integer updateAttentionStatusById(String loginUserId, String attentionId,Integer status);
    /**
     * 收藏手记
     * @param loginUserId 用户ID
     * @param noteId 手记ID
     * @return int 受影响行数
     */
    Boolean collectNote(String loginUserId, String noteId, String collectTime);
    /**
     * 是否已收藏手记
     * @param loginUserId 用户ID
     * @param noteId 手记ID
     * @return int 受影响行数
     */
    int hasCollect(String loginUserId, String noteId);
    /**
     * 取消收藏手记
     * @param loginUserId 用户ID
     * @param noteId 手记ID
     * @return int 受影响行数
     */
    int cancelCollectNote(String loginUserId, String noteId);
    /**
     * 获取收藏文章（按时间排序前6个）
     * @param loginUserId 用户ID
     * @return List<FavoriteVO> 收藏文章的列表
     */
    List<FavoriteVO> findFavoritesLimitByUserId(String loginUserId,Integer start,Integer num);
    /**
     * 获取关注的人
     * @param loginUserId 用户ID
     * @return Set<FolloweeVO> 关注的人集合
     */
    Set<FolloweeVO> findFolloweeList(String loginUserId);
    /**
     * 获取收藏文章
     * @param loginUserId 用户ID
     * @return List<FavoritesDetailVO> 收藏文章的列表
     */
    List<FavoritesDetailVO> findFavoritesByUserId(String loginUserId);
    /**
     * 根据登录用户ID获取收藏夹文章数
     * @param loginUserId
     * @return Integer 登录用户收藏夹文章数
     */
    Integer findMyFavoritesCount(String loginUserId);
}
