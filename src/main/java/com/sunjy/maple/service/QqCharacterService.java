package com.sunjy.maple.service;

import com.sunjy.maple.entity.QqCharacter;

import java.util.List;

/**
 * @author created by sunjy on 12/9/23
 */
public interface QqCharacterService {

    void save(QqCharacter qqCharacter);

    QqCharacter get(String id);

    QqCharacter getByQqNumber(String qqNumber);

    List<QqCharacter> list();

    void update(QqCharacter qqCharacter);

    void delete(String id);

    void delete(List<String> idList);

}
