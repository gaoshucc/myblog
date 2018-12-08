package com.funnycode.myblog.mapper;

import com.funnycode.myblog.pojo.PO.Answer;
import com.funnycode.myblog.pojo.PO.QuestType;
import com.funnycode.myblog.pojo.PO.Question;
import com.funnycode.myblog.pojo.PO.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-06 21:09
 */
@Mapper
@Component("questionMapper")
public interface QuestionMapper {
    /**
     * 获得所有问题类型
     * @param
     * @return List<QuestType> 类型集合
     */
    List<QuestType> findQuestCate();
    /**
     * 获得所有问题
     * @param
     * @return List<Question> 问题集合
     */
    List<Question> findAllQuestions();
    /**
     * 获得首页问题
     * @param
     * @return List<Question> 问题集合
     */
    List<Question> findAllQuestionsLimit(int start, int end);
    /**
     * 提出问题
     * @param question 问题
     * @return Integer 受影响的行数
     */
    Integer saveQuestion(Question question);
    /**
     * 查找问题内容
     * @param questId 问题ID
     * @return String 问题内容
     */
    String findQuestionContentByQuestId(String questId);
    /**
     * 查找问题
     * @param questId 问题ID
     * @return Question 问题
     */
    Question findQuestionByQuestId(String questId);
    /**
     * 保存回答
     * @param answer 回答
     * @return Integer 受影响的行数
     */
    Integer saveAnswer(Answer answer);
    /**
     * 根据问题ID获得该问题的所有回答
     * @param questId 回答
     * @return List<Answer> 回答集合
     */
    List<Answer> findAnswersByQuestId(String questId);
}
