package com.funnycode.myblog.pojo.VO;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-04 0:38
 */
public class CommentVO {

    private String commentId;
    private UserVO user;
    private List<CommentVO> son;
    private String commentContent;
    private String commentTime;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public List<CommentVO> getSon() {
        return son;
    }

    public void setSon(List<CommentVO> son) {
        this.son = son;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
