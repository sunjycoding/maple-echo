package com.sunjy.maple.service;

import com.sunjy.maple.entity.QuestionAnswer;

import java.util.List;

/**
 * @author created by sunjy on 12/9/23
 */
public interface QuestionAnswerService {

    void save(QuestionAnswer questionAnswer);

    QuestionAnswer get(String id);

    List<QuestionAnswer> listByQuestionLike(String question);

    List<QuestionAnswer> list();

    void update(QuestionAnswer questionAnswer);

    void delete(String id);

    void delete(List<String> idList);

}
