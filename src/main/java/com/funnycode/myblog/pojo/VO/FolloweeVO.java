package com.funnycode.myblog.pojo.VO;

/**
 * @author gaoshucc
 * @create 2019-01-05 23:22
 */
public class FolloweeVO {
    private String userId;
    private String nickname;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "FolloweeVO{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
