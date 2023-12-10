package com.sunjy.maple.core;

import com.sunjy.maple.config.BotConfig;
import com.sunjy.maple.constant.AppConstant;
import com.sunjy.maple.service.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Service;
import top.mrxiaom.qsign.QSignService;

import java.io.File;

/**
 * @author created by sunjy on 12/9/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MapleBotEngine {

    private final BotConfig botConfig;

    private final MessageService messageService;

    public static Bot bot;

    @PostConstruct
    public void initBot() {
        // 签名服务
        String filepath = AppConstant.Q_SIGN_PATH;
        QSignService.Factory.init(new File(filepath));
        QSignService.Factory.loadProtocols(null);
        QSignService.Factory.register();
        // 使用密码登录
        bot = BotFactory.INSTANCE.newBot(botConfig.getQqNumber(), botConfig.getPassword(),
                botConfiguration -> {
                    botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
                    botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
                    botConfiguration.fileBasedDeviceInfo();
                }
        );
        bot.login();
        registerListeners();
    }

    private void registerListeners() {
        bot.getEventChannel().subscribeAlways(MessageEvent.class, messageEvent -> {
            messageService.interpret(messageEvent, bot.getNick());
        });
    }

}
