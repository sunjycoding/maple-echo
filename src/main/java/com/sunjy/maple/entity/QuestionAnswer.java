package com.sunjy.maple.entity;

import com.sunjy.maple.constant.enums.AnswerTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author created by sunjy on 12/5/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_question_answer")
public class QuestionAnswer extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnswerTypeEnum answerType;

    @Column
    private String answerText;

    @Column
    private String answerFileUrl;

}
