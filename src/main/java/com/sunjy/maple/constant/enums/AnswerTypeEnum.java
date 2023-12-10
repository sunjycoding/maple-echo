package com.sunjy.maple.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author created by sunjy on 12/5/23
 */
@AllArgsConstructor
@Getter
public enum AnswerTypeEnum {

    TEXT("TEXT"),

    FILE("FILE"),
    ;

    private final String answerType;

}
