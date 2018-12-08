package com.funnycode.myblog.pojo.PO;

import java.util.UUID;

/**
 * @author gaoshucc
 * @create 2018-12-06 20:51
 */
public class Question {
    private String questId;
    private String questTitle;
    private QuestType questType;
    private User quizzer;
    private String createTime;
    private Integer status;
    private String question;

    public Question() {
    }

    public Question(String userId, String questionTitle, QuestType questType, String questMarkdownDoc) {
        User quizzer = new User();
        quizzer.setUserId(userId);

        this.questId = UUID.randomUUID().toString().replace("-","");
        this.questTitle = questionTitle;
        this.questType = questType;
        this.quizzer = quizzer;
        this.status = 1;
        this.question = questMarkdownDoc;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public User getQuizzer() {
        return quizzer;
    }

    public void setQuizzer(User quizzer) {
        this.quizzer = quizzer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
