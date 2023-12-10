package com.sunjy.maple.service.impl;

import com.sunjy.maple.entity.QuestionAnswer;
import com.sunjy.maple.repository.QuestionAnswerRepository;
import com.sunjy.maple.service.QuestionAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author created by sunjy on 12/9/23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

    private final QuestionAnswerRepository questionAnswerRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(QuestionAnswer questionAnswer) {
        QuestionAnswer questionAnswerCriteria = new QuestionAnswer();
        questionAnswerCriteria.setQuestion(questionAnswer.getQuestion());
        Example<QuestionAnswer> example = Example.of(questionAnswerCriteria);
        if (questionAnswerRepository.exists(example)) {
            throw new RuntimeException("已经存在的提问!");
        }
        questionAnswerRepository.save(questionAnswer);
    }

    @Override
    public QuestionAnswer get(String id) {
        return questionAnswerRepository.findById(id).orElse(null);
    }

    @Override
    public List<QuestionAnswer> listByQuestionLike(String question) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion(question);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("question",
                        ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()
                );
        Example<QuestionAnswer> example = Example.of(questionAnswer, exampleMatcher);
        return questionAnswerRepository.findAll(example);
    }

    @Override
    public List<QuestionAnswer> list() {
        return questionAnswerRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(QuestionAnswer questionAnswer) {
        questionAnswerRepository.save(questionAnswer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        questionAnswerRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> idList) {
        questionAnswerRepository.deleteAllById(idList);
    }

}
