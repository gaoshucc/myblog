package com.funnycode.myblog.service.impl;

import com.funnycode.myblog.mapper.QuestionMapper;
import com.funnycode.myblog.mapper.UserMapper;
import com.funnycode.myblog.pojo.PO.Answer;
import com.funnycode.myblog.pojo.PO.QuestType;
import com.funnycode.myblog.pojo.PO.Question;
import com.funnycode.myblog.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gaoshucc
 * @create 2018-12-06 21:05
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<QuestType> findQuestCate() {
        return questionMapper.findQuestCate();
    }

    @Override
    public List<Question> findAllQuestions() {
        return questionMapper.findAllQuestions();
    }

    @Override
    public boolean saveQuestion(Question question, String userId) {
        Integer num = questionMapper.saveQuestion(question);
        Integer updateSuccess = userMapper.updateUserExperienceByUserId(userId);

        return (num>0&&updateSuccess>0)?true:false;
    }

    @Override
    public String findQuestionContentByQuestId(String questId) {
        return questionMapper.findQuestionContentByQuestId(questId);
    }

    @Override
    public Question findQuestionByQuestId(String questId) {
        return questionMapper.findQuestionByQuestId(questId);
    }

    @Override
    public boolean saveAnswer(Answer answer) {
        Integer saveAnswerSuccess = questionMapper.saveAnswer(answer);

        return saveAnswerSuccess>0?true:false;
    }

    @Override
    public List<Answer> findAnswers(String questId) {
        return questionMapper.findAnswersByQuestId(questId);
    }

    @Override
    public List<Question> findAllQuestionsLimit(int start, int end) {
        return questionMapper.findAllQuestionsLimit(start, end);
    }

}
