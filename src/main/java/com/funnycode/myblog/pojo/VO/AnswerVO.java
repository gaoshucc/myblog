package com.funnycode.myblog.pojo.VO;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-07 11:14
 */
public class AnswerVO {
    private String answerId;
    private UserVO user;
    private List<AnswerVO> son;
    private String answerContent;
    private String answerTime;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public List<AnswerVO> getSon() {
        return son;
    }

    public void setSon(List<AnswerVO> son) {
        this.son = son;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
}
