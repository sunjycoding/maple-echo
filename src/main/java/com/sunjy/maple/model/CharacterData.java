package com.sunjy.maple.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author created by sunjy on 12/1/23
 */
@Data
public class CharacterData {

    @JSONField(name = "AchievementPoints")
    private String achievementPoints;

    @JSONField(name = "AchievementRank")
    private String achievementRank;

    @JSONField(name = "CharacterImageURL")
    private String characterImageUrl;

    @JSONField(name = "Class")
    private String className;

    @JSONField(name = "ClassRank")
    private Long classRank;

    @JSONField(name = "EXP")
    private BigDecimal exp;

    @JSONField(name = "EXPPercent")
    private BigDecimal expPercent;

    @JSONField(name = "GlobalRanking")
    private Long globalRanking;

    @JSONField(name = "GraphData")
    private List<GraphData> graphDataList;

    @JSONField(name = "Guild")
    private String guild;

    @JSONField(name = "LegionCoinsPerDay")
    private Integer legionCoinsPerDay;

    @JSONField(name = "LegionLevel")
    private Integer legionLevel;

    @JSONField(name = "LegionPower")
    private Long legionPower;

    @JSONField(name = "LegionRank")
    private Long legionRank;

    @JSONField(name = "Level")
    private Integer level;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "Server")
    private String server;

    @JSONField(name = "ServerClassRanking")
    private Long serverClassRanking;

    @JSONField(name = "ServerRank")
    private Long serverRank;

    @JSONField(name = "ServerSlug")
    private String serverSlug;

}
