package com.sunjy.maple.service;

import com.sunjy.maple.model.CharacterData;

/**
 * @author created by sunjy on 12/1/23
 */
public interface ExternalApiService {

    CharacterData requestCharacterData(String characterName);

    String requestCharacterName(String type, String jobId, String rebootIndex, String rank);
}
