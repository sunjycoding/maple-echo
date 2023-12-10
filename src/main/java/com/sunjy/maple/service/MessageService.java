package com.sunjy.maple.service;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author created by sunjy on 11/30/23
 */
public interface MessageService {

    void interpret(MessageEvent messageEvent, String botNickName);

}
