package com.sunjy.maple.service.impl;

import com.sunjy.maple.constant.AppConstant;
import com.sunjy.maple.constant.enums.LevelExpEnum;
import com.sunjy.maple.model.CharacterData;
import com.sunjy.maple.model.GraphData;
import com.sunjy.maple.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * @author created by sunjy on 12/1/23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    // CharacterData 图表的尺寸
    private static final int CHARACTER_IMAGE_WIDTH = 800;
    private static final int CHARACTER_IMAGE_DIVIDE_POINT = 350;
    private static final int CHARACTER_IMAGE_HEIGHT = 600;
    private static final int CHARACTER_IMAGE_MARGIN = 50;

    private static final String CHARACTER_IMAGE_LEFT_COLOR_HEX = "F4A261";
    private static final String CHARACTER_IMAGE_RIGHT_COLOR_HEX = "E9C46A";
    private static final String CHARACTER_IMAGE_BAR_COLOR_HEX = "2A9D8F";
    private static final String CHARACTER_IMAGE_VALUE_COLOR_HEX = "264653";

    private static final String INFINITY = "∞";

    @Override
    public BufferedImage generateCharacterImage(CharacterData characterData) {
        try {
            BufferedImage bufferedImage = new BufferedImage(CHARACTER_IMAGE_WIDTH, CHARACTER_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bufferedImage.createGraphics();

            // 设置字体
            Font font = new Font("Microsoft YaHei", Font.PLAIN, 14);
            graphics.setFont(font);

            // 绘制背景
            Color leftColor = getColorByHex(CHARACTER_IMAGE_LEFT_COLOR_HEX);
            Color rightColor = getColorByHex(CHARACTER_IMAGE_RIGHT_COLOR_HEX);
            Color barColor = getColorByHex(CHARACTER_IMAGE_BAR_COLOR_HEX);
            Color valueColor = getColorByHex(CHARACTER_IMAGE_VALUE_COLOR_HEX);

            graphics.setColor(leftColor);
            graphics.fillRect(0, 0, CHARACTER_IMAGE_DIVIDE_POINT, CHARACTER_IMAGE_HEIGHT);

            graphics.setColor(rightColor);
            graphics.fillRect(CHARACTER_IMAGE_DIVIDE_POINT, 0, CHARACTER_IMAGE_WIDTH, CHARACTER_IMAGE_HEIGHT);

            // 图片左侧
            // 绘制提示
            int hintStartX = 10;
            int hintStartY = 20;
            graphics.setColor(Color.BLACK);
            graphics.drawString("【经验值参考New Age版本】", hintStartX, hintStartY);
            // 绘制角色图像
            URI uri = URI.create(characterData.getCharacterImageUrl());
            URL characterImageUrl = uri.toURL();
            BufferedImage characterImage = ImageIO.read(characterImageUrl);
            int characterWidth = 120;
            int characterHeight = 120;
            int characterX = CHARACTER_IMAGE_DIVIDE_POINT / 2 - characterWidth / 2;
            int characterY = 30;
            graphics.drawImage(characterImage, characterX, characterY, characterWidth, characterHeight, null);

            List<GraphData> graphDataList = characterData.getGraphDataList();
            // 角色信息
            BigDecimal exp = characterData.getExp();
            int level = characterData.getLevel();
            graphics.setColor(Color.BLACK);
            int startX = characterX - 30;
            int startY = characterY + characterHeight + 30;
            int divideLineStartX = 30;
            int divideLineEndX = CHARACTER_IMAGE_DIVIDE_POINT - 30;
            int gap = 25;
            graphics.drawString("角色：" + characterData.getName(), startX, startY);
            startY = startY + gap;
            graphics.drawString("服务器：" + characterData.getServer(), startX, startY);
            startY = startY + gap;
            graphics.drawString("职业：" + characterData.getClassName(), startX, startY);
            startY = startY + gap;
            graphics.drawString("等级：" + characterData.getLevel(), startX, startY);
            startY = startY + gap;
            LevelExpEnum levelExpEnum = LevelExpEnum.findByLevel(level);
            BigDecimal expToNextLevel = new BigDecimal(levelExpEnum.getExpToNextLevel());
            String expValue = generateBarValue(exp);
            String expToNextLevelValue = generateBarValue(expToNextLevel);
            String expContent = "经验值：" + expValue + "/" + expToNextLevelValue +
                    " (" + characterData.getExpPercent() + "%)";
            graphics.drawString(expContent, startX, startY);
            startY = startY + 10;
            graphics.drawLine(divideLineStartX, startY, divideLineEndX, startY);
            startY = startY + gap;
            graphics.drawString("本服职业排名：" + characterData.getServerClassRanking(), startX, startY);
            startY = startY + gap;
            graphics.drawString("本服排名：" + characterData.getServerRank(), startX, startY);
            startY = startY + gap;
            graphics.drawString("全服职业排名：" + characterData.getClassRank(), startX, startY);
            startY = startY + gap;
            graphics.drawString("全服排名：" + characterData.getGlobalRanking(), startX, startY);
            startY = startY + 10;
            graphics.drawLine(divideLineStartX, startY, divideLineEndX, startY);
            startY = startY + gap;
            Integer legionCoinsPerDay = characterData.getLegionCoinsPerDay();
            if (legionCoinsPerDay == null) {
                legionCoinsPerDay = -1;
            }
            graphics.drawString("联盟等级：" + characterData.getLegionLevel(), startX, startY);
            startY = startY + gap;
            graphics.drawString("联盟排名：" + characterData.getLegionRank(), startX, startY);
            startY = startY + gap;
            graphics.drawString("联盟战斗力：" + characterData.getLegionPower(), startX, startY);
            startY = startY + gap;
            graphics.drawString("每日联盟币：" + legionCoinsPerDay, startX, startY);
            startY = startY + 10;
            graphics.drawLine(divideLineStartX, startY, divideLineEndX, startY);
            startY = startY + gap;
            int nextLevel = getInquiryNextLevel(level);
            String dayToNextLevelByDaily = INFINITY;
            String dayToNextLevelByWeek = INFINITY;
            String dayToNextLevelByHalfMonth = INFINITY;
            int contentStartX = startX - CHARACTER_IMAGE_MARGIN;
            if (level < AppConstant.MAX_LEVEL) {
                if (CollectionUtils.isNotEmpty(graphDataList)) {
                    // 最近1天平均获取的经验
                    BigDecimal lastDayAvgExpDifference = BigDecimal.ZERO;
                    // 最近7天平均获取的经验
                    BigDecimal lastWeekAvgExpDifference = BigDecimal.ZERO;
                    // 最近14天平均获取的经验
                    BigDecimal lastHalfMonthAvgExpDifference = BigDecimal.ZERO;
                    int size = graphDataList.size();
                    int dayCount;
                    lastDayAvgExpDifference = graphDataList.get(size - 1).getExpDifference();
                    if (size <= 7) {
                        BigDecimal average = calculateAverage(graphDataList);
                        lastWeekAvgExpDifference = average;
                        lastHalfMonthAvgExpDifference = average;
                    } else if (size <= 14) {
                        dayCount = 7;
                        lastWeekAvgExpDifference = calculateAverage(graphDataList.subList(size - dayCount, size));
                        lastHalfMonthAvgExpDifference = calculateAverage(graphDataList);

                    } else {
                        dayCount = 14;
                        lastHalfMonthAvgExpDifference = calculateAverage(graphDataList.subList(size - dayCount, size));
                    }
                    dayToNextLevelByDaily = calculateDaysToNextLevel(level, nextLevel, exp, lastDayAvgExpDifference);
                    dayToNextLevelByWeek = calculateDaysToNextLevel(level, nextLevel, exp, lastWeekAvgExpDifference);
                    dayToNextLevelByHalfMonth = calculateDaysToNextLevel(level, nextLevel, exp, lastHalfMonthAvgExpDifference);
                    String content = "按照最近%d天的进度，需要%s天到达%d级";
                    graphics.drawString(String.format(content, 1, dayToNextLevelByDaily, nextLevel), contentStartX, startY);
                    startY = startY + gap;
                    graphics.drawString(String.format(content, 7, dayToNextLevelByWeek, nextLevel), contentStartX, startY);
                    startY = startY + gap;
                    graphics.drawString(String.format(content, 14, dayToNextLevelByHalfMonth, nextLevel), contentStartX, startY);
                }
            } else {
                graphics.setColor(valueColor);
                String content = "恭喜，你已经到达满级!";
                graphics.drawString(String.format(content, 14, dayToNextLevelByHalfMonth, nextLevel), startX, startY);
            }

            // 图片右侧
            if (level < 210) {
                graphics.setColor(valueColor);
                int x = CHARACTER_IMAGE_DIVIDE_POINT + (CHARACTER_IMAGE_WIDTH - CHARACTER_IMAGE_DIVIDE_POINT) / 2 - 150;
                int y = CHARACTER_IMAGE_HEIGHT / 2;
                graphics.drawString("【仅限于210级以上的角色展示每日经验】", x, y);
            } else if (CollectionUtils.isNotEmpty(graphDataList)) {
                // 绘制条形图
                int barHeight = 20;
                // 最大条形宽度
                int maxBarWidth = CHARACTER_IMAGE_WIDTH - CHARACTER_IMAGE_DIVIDE_POINT - CHARACTER_IMAGE_MARGIN * 4;
                // 条形图开始的纵坐标
                int barStartY = 80;
                // 条形之间的间隔
                int barGap = 10;
                // label开始的x坐标
                int labelStartX = CHARACTER_IMAGE_DIVIDE_POINT + CHARACTER_IMAGE_MARGIN;
                // bar开始的x坐标
                int barStartX = labelStartX + CHARACTER_IMAGE_MARGIN;
                List<BigDecimal> expDifferenceList = graphDataList.stream().map(GraphData::getExpDifference).toList();
                BigDecimal maxExpDifference = expDifferenceList.stream().max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
                // 右侧图表
                for (GraphData graphData : graphDataList) {
                    // 文字的y坐标, 根据bar的位置来定
                    int labelStartY = barStartY + barHeight / 2 + 5;
                    // label
                    graphics.setColor(Color.BLACK);
                    String dateLabel = graphData.getDateLabel();
                    dateLabel = dateLabel.substring(5);
                    graphics.drawString(dateLabel + ":", labelStartX, labelStartY);

                    // bar
                    graphics.setColor(barColor);
                    BigDecimal expDifference = graphData.getExpDifference();
                    BigDecimal proportion = expDifference.divide(maxExpDifference, AppConstant.SCALE, RoundingMode.HALF_UP);
                    int barWidth = (int) (maxBarWidth * proportion.doubleValue());
                    graphics.fillRect(barStartX, barStartY, barWidth, barHeight);

                    // value
                    graphics.setColor(valueColor);
                    String expDifferenceStrValue = generateBarValue(expDifference);
                    graphics.drawString(expDifferenceStrValue, barStartX + barWidth + 3, labelStartY);

                    // 换一条bar
                    barStartY += barHeight + barGap;
                }
            }

            // 释放资源
            graphics.dispose();
            return bufferedImage;
        } catch (Exception e) {
            throw new RuntimeException("生成角色图片时出现错误");
        }
    }

    private Color getColorByHex(String colorHex) {
        int radix = 16;
        try {
            return new Color(
                    Integer.parseInt(colorHex.substring(0, 2), radix),
                    Integer.parseInt(colorHex.substring(2, 4), radix),
                    Integer.parseInt(colorHex.substring(4, 6), radix)
            );
        } catch (Exception e) {
            throw new RuntimeException("图片生成颜色出现错误");
        }
    }

    private BigDecimal calculateAverage(List<GraphData> graphDataList) {
        BigDecimal sum = graphDataList.stream()
                .map(GraphData::getExpDifference)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = BigDecimal.ZERO;
        if (!graphDataList.isEmpty()) {
            average = sum.divide(BigDecimal.valueOf(graphDataList.size()), AppConstant.SCALE, RoundingMode.HALF_UP);
        }
        return average;
    }

    private String generateBarValue(BigDecimal exp) {
        if (exp == null || exp.compareTo(BigDecimal.ZERO) <= 0) {
            return "0";
        }
        BigDecimal k = new BigDecimal("1000");
        BigDecimal m = new BigDecimal("1000000");
        BigDecimal b = new BigDecimal("1000000000");
        BigDecimal t = new BigDecimal("1000000000000");
        BigDecimal qt = new BigDecimal("1000000000000000");
        if (exp.compareTo(qt) >= 0) {
            // QT
            BigDecimal result = exp.divide(qt, AppConstant.SCALE, RoundingMode.CEILING);
            return result + "QT";
        } else if (exp.compareTo(t) >= 0) {
            // T
            BigDecimal result = exp.divide(t, AppConstant.SCALE, RoundingMode.CEILING);
            return result + "T";
        } else if (exp.compareTo(b) >= 0) {
            // B
            BigDecimal result = exp.divide(b, AppConstant.SCALE, RoundingMode.CEILING);
            return result + "B";
        } else if (exp.compareTo(m) >= 0) {
            // M
            BigDecimal result = exp.divide(m, AppConstant.SCALE, RoundingMode.CEILING);
            return result + "M";
        } else if (exp.compareTo(k) >= 0) {
            // K
            BigDecimal result = exp.divide(k, AppConstant.SCALE, RoundingMode.CEILING);
            return result + "K";
        } else {
            return exp.toString();
        }
    }

    private int getInquiryNextLevel(int level) {
        int nextLevel = 0;
        if (level > 0 && level < 275) {
            for (nextLevel = level + 1; nextLevel < AppConstant.MAX_LEVEL; nextLevel++) {
                if (nextLevel % 5 == 0) {
                    break;
                }
            }
        } else if (level >= 275 && level <= 299) {
            nextLevel = level + 1;
        } else {
            throw new RuntimeException("无效的等级");
        }
        return nextLevel;
    }

    private String calculateDaysToNextLevel(int currentLevel, int nextLevel,
                                            BigDecimal currentExp, BigDecimal averageExpDifference) {
        if (averageExpDifference.compareTo(BigDecimal.ZERO) == 0) {
            return INFINITY;
        }
        BigDecimal expToNextLevelSum = BigDecimal.ZERO;
        for (int i = currentLevel; i < nextLevel; i++) {
            LevelExpEnum levelExpEnum = LevelExpEnum.findByLevel(currentLevel);
            expToNextLevelSum = expToNextLevelSum.add(new BigDecimal(levelExpEnum.getExpToNextLevel()));
        }
        BigDecimal neededExp = expToNextLevelSum.subtract(currentExp);
        BigDecimal days = neededExp.divide(averageExpDifference, AppConstant.SCALE, RoundingMode.HALF_UP);
        return days.toString();
    }

}
