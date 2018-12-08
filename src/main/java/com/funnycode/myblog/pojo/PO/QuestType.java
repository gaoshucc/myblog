package com.funnycode.myblog.pojo.PO;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-06 20:51
 */
public class QuestType {
    private Integer typeId;
    private String typeName;
    private List<Question> questions;

    public QuestType() {
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
