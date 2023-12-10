package com.sunjy.maple.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author created by sunjy on 12/3/23
 */
@AllArgsConstructor
@Getter
public enum JobEnum {

    /**
     * Explorer
     */
    BEGINNER(null, "beginner", List.of(new String[]{"新手"})),

    WARRIOR(1, "warrior", List.of(new String[]{"战士", "英雄", "黑骑", "圣骑"})),

    MAGICIAN(2, "magician", List.of(new String[]{"法师", "火毒", "冰雷", "主教"})),

    BOWMAN(3, "bowman", List.of(new String[]{"弓箭手", "神射", "箭神"})),

    THIEF(4, "thief", List.of(new String[]{"飞侠", "刀飞", "标飞", "双刀"})),

    PIRATE(5, "pirate", List.of(new String[]{"海盗", "队长", "火炮"})),

    /**
     * Cygnus Knights
     */
    NOBLESSE(10, "noblesse", List.of(new String[]{"骑士团新手"})),

    DAWN_WARRIOR(11, "dawn warrior", List.of(new String[]{"魂骑"})),

    BLAZE_WIZARD(12, "blaze wizard", List.of(new String[]{"炎术"})),

    WIND_ARCHER(13, "wind archer", List.of(new String[]{"风灵", "风铃"})),

    NIGHT_WALKER(14, "night walker", List.of(new String[]{"夜行"})),

    THUNDER_BREAKER(15, "thunder breaker", List.of(new String[]{"奇袭"})),

    MIHILE(202, "mihile", List.of(new String[]{"米哈"})),

    /**
     * Resistance
     */
    CITIZEN(30, "citizen", List.of(new String[]{"反抗者新手"})),

    BLASTER(215, "blaster", List.of(new String[]{"爆破"})),

    BATTLE_MAGE(32, "battle mage", List.of(new String[]{"战法", "战斗法师", "脚气"})),

    WILD_HUNTER(33, "wild hunter", List.of(new String[]{"豹弩"})),

    XENON(208, "xenon", List.of(new String[]{"尖兵", "煎饼"})),

    MECHANIC(35, "mechanic", List.of(new String[]{"机械"})),

    DEMON_AVENGER(209, "demon avenger", List.of(new String[]{"恶魔复仇者", "白毛"})),

    DEMON_SLAYER(31, "demon slayer", List.of(new String[]{"恶魔猎手", "红毛"})),

    /**
     * Hero
     */
    ARAN(21, "aran", List.of(new String[]{"战神"})),

    EVAN(22, "evan", List.of(new String[]{"龙神"})),

    LUMINOUS(203, "luminous", List.of(new String[]{"夜光"})),

    LEGEND(20, "legend", List.of(new String[]{"英雄新手"})),

    MERCEDES(23, "mercedes", List.of(new String[]{"双弩"})),

    PHANTOM(24, "phantom", List.of(new String[]{"幻影"})),

    SHADE(212, "shade", List.of(new String[]{"隐月"})),

    /**
     * Nova
     */
    KAISER(204, "kaiser", List.of(new String[]{"凯撒", "狂龙", "狂蛇"})),

    KAIN(222, "kain", List.of(new String[]{"该隐", "凯因"})),

    CADENA(216, "cadena", List.of(new String[]{"卡德娜", "卡姐"})),

    ANGELIC_BUSTER(205, "angelic buster", List.of(new String[]{"天使"})),

    /**
     * Flora
     */
    ADELE(221, "adele", List.of(new String[]{"阿呆", "御剑骑士"})),

    ILLIUM(217, "illium", List.of(new String[]{"圣晶", "圣经", "黑皮"})),

    ARK(218, "ark", List.of(new String[]{"亚克"})),

    KHALI(224, "khali", List.of(new String[]{"凯莉"})),

    /**
     * Sengoku
     */
    HAYATO(206, "hayato", List.of(new String[]{"剑豪"})),

    KANNA(207, "kanna", List.of(new String[]{"阴阳师"})),

    /**
     * Anima
     */
    LARA(223, "lara", List.of(new String[]{"拉拉"})),

    HOYOUNG(220, "hoyoung", List.of(new String[]{"虎影"})),

    /**
     * Others
     */
    ZERO(210, "zero", List.of(new String[]{"神子", "神之子"})),

    KINESES(214, "kineses", List.of(new String[]{"超能", "凯内"})),

    BEAST_TAMER(211, "beast tamer", List.of(new String[]{"林之灵", "lzl"})),
    ;

    private final Integer jobId;

    private final String englishName;

    private final List<String> chineseNameList;
}
