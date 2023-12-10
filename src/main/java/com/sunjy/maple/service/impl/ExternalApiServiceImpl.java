package com.sunjy.maple.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sunjy.maple.constant.AppConstant;
import com.sunjy.maple.constant.enums.LevelExpEnum;
import com.sunjy.maple.model.CharacterData;
import com.sunjy.maple.model.GraphData;
import com.sunjy.maple.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author created by sunjy on 12/1/23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    @Override
    public CharacterData requestCharacterData(String characterName) {
        try {
            CharacterData characterData = null;
            String url = "https://api.maplestory.gg/v2/public/character/gms/" + characterName;
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                String characterDataStr = jsonObject.getString("CharacterData");
                characterData = JSON.parseObject(characterDataStr, CharacterData.class);
                // 这里要重新处理一下数据, 因为返回来的数据里的dateLabel和expDifference其实差了一天
                List<GraphData> newGraphDataList = new ArrayList<>();
                List<GraphData> graphDataList = characterData.getGraphDataList();
                if (CollectionUtils.isNotEmpty(graphDataList)) {
                    int size = graphDataList.size();
                    for (int i = 0; i < size - 1; i++) {
                        GraphData current = graphDataList.get(i);
                        GraphData next = graphDataList.get(i + 1);
                        current.setDateLabel(next.getDateLabel());
                        newGraphDataList.add(current);
                    }
                    characterData.setGraphDataList(newGraphDataList);
                }
                // newAge版本开始 maplestory.gg 的 ExpPercent不准确, 得自己计算
                BigDecimal expPercent = calculateExpPercent(characterData.getLevel(), characterData.getExp());
                characterData.setExpPercent(expPercent);
            }
            return characterData;
        } catch (Exception e) {
            throw new RuntimeException("请求maplestory.gg接口出现错误");
        }
    }

    @Override
    public String requestCharacterName(String type, String jobId, String rebootIndex, String rank) {
        try {
            String urlPreset = "https://maplestory.nexon.net/api/ranking?id=%s&id2=%s&rebootIndex=%s&page_index=%s";
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format(urlPreset, type, jobId, rebootIndex, rank);
            log.info("准备请求地址：【{}】", url);
            JSONArray jsonArray = restTemplate.getForObject(url, JSONArray.class);
            assert jsonArray != null;
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getString("CharacterName");
        } catch (Exception e) {
            throw new RuntimeException("查找排名失败");
        }
    }

    public BigDecimal calculateExpPercent(int level, BigDecimal currentExp) {
        // 小数点后保留两位
        LevelExpEnum levelExpEnum = LevelExpEnum.findByLevel(level);
        BigDecimal expToNextLevel = new BigDecimal(levelExpEnum.getExpToNextLevel());
        return currentExp.divide(expToNextLevel, AppConstant.SCALE, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }

}
