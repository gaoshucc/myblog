package com.funnycode.myblog.pojo.PO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author gaoshucc
 * @create 2018-12-07 11:33
 */
public class Answer {
    private String answerId;
    private Question question;
    private User user;
    private String parentId;
    private String answerContent;
    private String answerTime;

    public Answer(String userId, String questId, String answerContent) {
        User user = new User();
        user.setUserId(userId);
        Question question = new Question();
        question.setQuestId(questId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.answerId = UUID.randomUUID().toString().replace("-", "");
        this.user = user;
        this.question = question;
        this.parentId = "0";
        this.answerContent = answerContent;
        this.answerTime = sdf.format(new Date());
    }

    public Answer(String userId, String questId, String byReplyId, String answerContent) {
        User user = new User();
        user.setUserId(userId);
        Question question = new Question();
        question.setQuestId(questId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.answerId = UUID.randomUUID().toString().replace("-", "");
        this.user = user;
        this.question = question;
        this.parentId = byReplyId;
        this.answerContent = answerContent;
        this.answerTime = sdf.format(new Date());
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
