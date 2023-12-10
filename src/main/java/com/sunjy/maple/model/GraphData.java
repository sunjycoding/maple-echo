package com.sunjy.maple.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author created by sunjy on 12/1/23
 */
@Data
public class GraphData {

    @JSONField(name = "AvatarURL")
    private String avatarUrl;

    @JSONField(name = "ClassID")
    private Long classId;

    @JSONField(name = "ClassRankGroupID")
    private Long classRankGroupId;

    @JSONField(name = "CurrentEXP")
    private BigDecimal currentExp;

    @JSONField(name = "DateLabel")
    private String dateLabel;

    @JSONField(name = "EXPDifference")
    private BigDecimal expDifference;

    @JSONField(name = "EXPToNextLevel")
    private BigDecimal expToNextLevel;

    @JSONField(name = "ImportTime")
    private Long importTime;

    @JSONField(name = "Level")
    private Integer level;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "ServerID")
    private Integer serverId;

    @JSONField(name = "ServerMergeID")
    private Integer serverMergeId;

    @JSONField(name = "TotalOverallEXP")
    private BigDecimal totalOverallExp;

}