package com.sunjy.maple.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基本提问
 *
 * @author created by sunjy on 12/3/23
 */
@AllArgsConstructor
@Getter
public enum CommandEnum {

    HELP("帮助"),

    INQUIRY("查询"),

    BIND("绑定"),

    CANCEL_BIND("取消绑定"),

    ADD("添加"),

    DELETE("删除"),

    ;

    private final String command;

}
