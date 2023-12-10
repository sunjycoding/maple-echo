package com.sunjy.maple.service.impl;

import com.sunjy.maple.config.BotConfig;
import com.sunjy.maple.constant.enums.AnswerTypeEnum;
import com.sunjy.maple.constant.AppConstant;
import com.sunjy.maple.constant.enums.CommandEnum;
import com.sunjy.maple.constant.enums.JobEnum;
import com.sunjy.maple.entity.QqCharacter;
import com.sunjy.maple.entity.QuestionAnswer;
import com.sunjy.maple.model.CharacterData;
import com.sunjy.maple.service.ExternalApiService;
import com.sunjy.maple.service.ImageService;
import com.sunjy.maple.service.MessageService;
import com.sunjy.maple.service.QqCharacterService;
import com.sunjy.maple.service.QuestionAnswerService;
import com.sunjy.maple.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * @author created by sunjy on 11/30/23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final String CHARACTER_NAME_REGEX = "^[a-zA-Z0-9]+$";

    private final BotConfig botConfig;

    private final QuestionAnswerService questionAnswerService;

    private final QqCharacterService qqCharacterService;

    private final ExternalApiService externalApiService;

    private final ImageService imageService;

    @Override
    public void interpret(MessageEvent messageEvent, String botNickName) {
        String content = messageEvent.getMessage().contentToString();
        // 主要可以划分为以下指令类型
        // [查询] + [] / [我] / [@qqNumber] / [老子]
        // [绑定] + 角色名
        // [取消绑定]
        // [机器人名] + [CommandEnum]
        // [机器人名] + [数据库的question字段]
        String helpCommand = CommandEnum.HELP.getCommand();
        String inquiryCommand = CommandEnum.INQUIRY.getCommand();
        String bindCommand = CommandEnum.BIND.getCommand();
        String cancelBindCommand = CommandEnum.CANCEL_BIND.getCommand();
        String addCommand = CommandEnum.ADD.getCommand();
        String deleteCommand = CommandEnum.DELETE.getCommand();
        log.info("接收到消息:{}", content);
        if (content.startsWith(inquiryCommand)) {
            // 截取查询内容
            content = content.replace(inquiryCommand, AppConstant.EMPTY_STRING);
            content = content.trim();
            replyInquiry(messageEvent, content);
        } else if (content.startsWith(bindCommand)) {
            // 截取绑定角色名
            String characterName = content.replace(bindCommand, AppConstant.EMPTY_STRING);
            characterName = characterName.trim();
            if (StringUtils.isNotEmpty(characterName)) {
                replyBind(messageEvent, characterName);
            }
        } else if (content.startsWith(cancelBindCommand)) {
            replyCancelBindCommand(messageEvent);
        } else if (content.startsWith(botNickName)) {
            // 以机器人名开头
            if (content.contains(helpCommand)) {
                replyHelp(messageEvent);
            } else if (content.contains(addCommand)) {
                replyAdd(messageEvent);
            } else if (content.contains(deleteCommand)) {
                replyDelete(messageEvent);
            } else {
                String contentWithOutName = content.replaceAll(botNickName, AppConstant.EMPTY_STRING);
                contentWithOutName = contentWithOutName.trim();
                if (AppConstant.EMPTY_STRING.equals(contentWithOutName)) {
                    messageEvent.getSubject().sendMessage("Hi，我是" + botNickName + "，干嘛？");
                } else {
                    reply(messageEvent, content);
                }
            }
        }

    }

    public void replyHelp(MessageEvent messageEvent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("目前有以下有效提问");
        CommandEnum[] commandEnums = CommandEnum.values();
        for (CommandEnum commandEnum : commandEnums) {
            stringBuilder
                    .append(System.lineSeparator())
                    .append("[")
                    .append(commandEnum.getCommand())
                    .append("]");
        }
        List<QuestionAnswer> questionAnswerList = questionAnswerService.list();
        questionAnswerList.forEach(questionAnswer -> stringBuilder
                .append(System.lineSeparator())
                .append("[")
                .append(questionAnswer.getQuestion())
                .append("]"));
        stringBuilder
                .append(System.lineSeparator())
                .append("PS: 除了[查询]、[绑定]、[取消绑定]以外，其他的提问请在前面加上我的名字哦");
        messageEvent.getSubject().sendMessage(stringBuilder.toString());
    }

    public void replyInquiry(MessageEvent messageEvent, String content) {
        String characterName = analyzeCharacterName(messageEvent, content);
        Contact contact = messageEvent.getSubject();
        // 默认空字符串不查询
        if (AppConstant.EMPTY_STRING.equals(characterName)) {
            contact.sendMessage("未绑定角色，请先绑定角色：绑定[角色名]");
            throw new RuntimeException("角色名为空");
        }
        try {
            contact.sendMessage(generateInquiryImage(contact, characterName));
        } catch (Exception e) {
            contact.sendMessage("角色[" + characterName + "]查询失败");
            throw new RuntimeException(e);
        }
    }

    public void replyBind(MessageEvent messageEvent, String characterName) {
        Contact contact = messageEvent.getSubject();
        // 获取发送者的QQ号
        User user = messageEvent.getSender();
        String qqNumber = String.valueOf(user.getId());
        String exceptionPrefix = "用户" + user.getNick() + "[" + qqNumber + "]";
        // 是否已经绑定了角色
        QqCharacter selectedQqCharacter = qqCharacterService.getByQqNumber(qqNumber);
        if (selectedQqCharacter != null) {
            contact.sendMessage("已经绑定角色" + selectedQqCharacter.getCharacterName() + "，请先取消绑定");
            throw new RuntimeException(exceptionPrefix + "已经绑定角色");
        }
        // 是否有效角色名
        if (characterName.matches(CHARACTER_NAME_REGEX)) {
            QqCharacter qqCharacter = new QqCharacter();
            qqCharacter.setQqNumber(qqNumber);
            qqCharacter.setCharacterName(characterName);
            qqCharacterService.save(qqCharacter);
            contact.sendMessage("绑定[" + characterName + "]成功");
        } else {
            contact.sendMessage("无效的角色名" + characterName);
            throw new RuntimeException(exceptionPrefix + "无效的角色名");
        }
    }

    public void replyCancelBindCommand(MessageEvent messageEvent) {
        Contact contact = messageEvent.getSubject();
        // 获取发送者的QQ号
        User user = messageEvent.getSender();
        String qqNumber = String.valueOf(user.getId());
        String exceptionPrefix = "用户" + user.getNick() + "[" + qqNumber + "]";
        // 是否已经绑定了角色
        QqCharacter selectedQqCharacter = qqCharacterService.getByQqNumber(qqNumber);
        if (selectedQqCharacter != null) {
            qqCharacterService.delete(selectedQqCharacter.getId());
            contact.sendMessage("取消绑定成功");
        } else {
            contact.sendMessage("未绑定角色");
            throw new RuntimeException(exceptionPrefix + "未绑定角色");
        }
    }

    public void replyAdd(MessageEvent messageEvent) {
        messageEvent.getSubject().sendMessage("该功能还没有上线");
    }

    public void replyDelete(MessageEvent messageEvent) {
        messageEvent.getSubject().sendMessage("该功能还没有上线");
    }

    public void reply(MessageEvent messageEvent, String content) {
        Contact contact = messageEvent.getSubject();
        // 获取所有Q&A
        QuestionAnswer certainQuestionAnswer = null;
        List<QuestionAnswer> questionAnswerList = questionAnswerService.list();
        for (QuestionAnswer questionAnswer : questionAnswerList) {
            String question = questionAnswer.getQuestion();
            // 找到可能的Q&A
            if (content.toUpperCase().contains(question.toUpperCase())) {
                List<QuestionAnswer> possibleQuestionList = questionAnswerService.listByQuestionLike(question);
                int size = possibleQuestionList.size();
                if (size == 1) {
                    certainQuestionAnswer = questionAnswer;
                    break;
                } else {
                    contact.sendMessage("找到" + size + "条符合条件的提问，请先明确你的提问");
                }
            }
        }
        if (certainQuestionAnswer != null) {
            AnswerTypeEnum answerTypeEnum = certainQuestionAnswer.getAnswerType();
            switch (answerTypeEnum) {
                case TEXT -> contact.sendMessage(certainQuestionAnswer.getAnswerText());
                case FILE -> {
                    File file = FileUtils.readFile(certainQuestionAnswer.getAnswerFileUrl());
                    try {
                        // 判断文件类型, 图片
                        BufferedImage bufferedImage = ImageIO.read(file);
                        contact.sendMessage(contact.uploadImage(generateExternalResource(bufferedImage)));
                        // 文件 TODO
                    } catch (Exception e) {
                        contact.sendMessage("处理错误");
                        throw new RuntimeException(e);
                    }
                }
                default -> contact.sendMessage("无法处理的回答类型");
            }
        } else {
            contact.sendMessage("不好意思，听不懂你在说什么");
        }
    }

    private String analyzeCharacterName(MessageEvent messageEvent, String content) {
        String rankKeyWord = "第";
        String characterName = AppConstant.EMPTY_STRING;
        String mine = "我";
        String daddy = "老子";
        if (content.matches(CHARACTER_NAME_REGEX)) {
            // 查询 角色名
            characterName = content;
        } else if (content.contains(rankKeyWord)) {
            // 查询 ...第... (查询R区第x，查询xx职业第x)
            int indexRankKeyWord = content.indexOf(rankKeyWord);
            String beforeRankKeyWord = content.substring(0, indexRankKeyWord);
            String rank = content.substring(indexRankKeyWord + 1);
            // 默认查找全部排名
            String type = "overall";
            String jobId = AppConstant.EMPTY_STRING;
            // 0代表所有区 1是R区 2是非R区
            String rebootIndex = "0";
            if (beforeRankKeyWord.contains("非R") || beforeRankKeyWord.contains("非r")) {
                rebootIndex = "2";
            } else if (beforeRankKeyWord.contains("R") || beforeRankKeyWord.contains("r")) {
                rebootIndex = "1";
            }
            JobEnum[] jobEnumArray = JobEnum.values();
            for (JobEnum jobEnum : jobEnumArray) {
                String englishName = jobEnum.getEnglishName();
                if (beforeRankKeyWord.contains(englishName)) {
                    jobId = jobEnum.getJobId().toString();
                    break;
                }
                List<String> chineseNameList = jobEnum.getChineseNameList();
                for (String chineseName : chineseNameList) {
                    if (beforeRankKeyWord.contains(chineseName)) {
                        jobId = jobEnum.getJobId().toString();
                        break;
                    }
                }
                if (StringUtils.isNotEmpty(jobId)) {
                    type = "job";
                    break;
                }
            }
            characterName = externalApiService.requestCharacterName(type, jobId, rebootIndex, rank);
        } else if (content.contains(mine) || content.contains(daddy)) {
            // 查询 我 / 老子
            // 判断该qq号是否已经绑定角色
            User user = messageEvent.getSender();
            String qqNumber = String.valueOf(user.getId());
            QqCharacter qqCharacter = qqCharacterService.getByQqNumber(qqNumber);
            if (qqCharacter != null) {
                characterName = qqCharacter.getCharacterName();
            } else {
                characterName = AppConstant.EMPTY_STRING;
            }
        } else if (messageEvent.getMessage().contains(At.Key)) {
            // 查询 @的QQ号
            for (SingleMessage message : messageEvent.getMessage()) {
                if (message instanceof At at) {
                    String qqNumber = String.valueOf(at.getTarget());
                    QqCharacter qqCharacter = qqCharacterService.getByQqNumber(qqNumber);
                    if (qqCharacter != null) {
                        characterName = qqCharacter.getCharacterName();
                    } else {
                        characterName = AppConstant.EMPTY_STRING;
                    }
                    break;
                }
            }
        }
        return characterName;
    }

    private Image generateInquiryImage(Contact contact, String characterName) {
        log.info("正在查询角色【{}】的数据", characterName);
        // 请求数据
        CharacterData characterData;
        characterData = externalApiService.requestCharacterData(characterName);
        if (characterData != null) {
            log.info("查询角色【{}】数据成功, 正在生成图片", characterName);
            // 生成并上传图片
            BufferedImage bufferedImage = imageService.generateCharacterImage(characterData);
            return contact.uploadImage(generateExternalResource(bufferedImage));
        }
        throw new RuntimeException("生成图片错误");
    }

    private ExternalResource generateExternalResource(BufferedImage bufferedImage) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "PNG", outputStream);
            byte[] imageData = outputStream.toByteArray();
            try (ExternalResource externalResource = ExternalResource.create(imageData)) {
                return externalResource;
            }
        } catch (Exception e) {
            throw new RuntimeException("获取图片失败");
        }
    }

}
