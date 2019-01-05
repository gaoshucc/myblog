package com.funnycode.myblog.service;

import com.funnycode.myblog.pojo.PO.Answer;
import com.funnycode.myblog.pojo.PO.QuestType;
import com.funnycode.myblog.pojo.PO.Question;
import com.funnycode.myblog.pojo.PO.User;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-06 21:04
 */
public interface QuestionService {
    List<QuestType> findQuestCate();

    List<Question> findAllQuestions();

    boolean saveQuestion(Question question, String userId);

    String findQuestionContentByQuestId(String questId);

    Question findQuestionByQuestId(String questId);

    boolean saveAnswer(Answer answer);

    List<Answer> findAnswers(String questId);

    List<Question> findAllQuestionsLimit(int start, int end);

    Integer findAnswerCountByAuthorId(String authorId);

    List<Question> findMyQuestionsByUserId(String userId);

    boolean deleteQuestionByQuestionId(String questionId);

    List<Question> findQuestionRecycleBinByUserId(String userId);

    boolean recycleQuestionByQuestionId(String questionId);

    boolean completelyDelQuestionByQuestionId(String questionId);

    List<Question> findQuestionsByTypeId(Integer questTypeId);
}
