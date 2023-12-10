package com.sunjy.maple.service.impl;

import com.sunjy.maple.entity.QqCharacter;
import com.sunjy.maple.repository.QqCharacterRepository;
import com.sunjy.maple.service.QqCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author created by sunjy on 12/9/23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QqCharacterServiceImpl implements QqCharacterService {

    private final QqCharacterRepository qqCharacterRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(QqCharacter qqCharacter) {
        qqCharacterRepository.save(qqCharacter);
    }

    @Override
    public QqCharacter get(String id) {
        return qqCharacterRepository.findById(id).orElse(null);
    }

    @Override
    public QqCharacter getByQqNumber(String qqNumber) {
        QqCharacter qqCharacterCriteria = new QqCharacter();
        qqCharacterCriteria.setQqNumber(qqNumber);
        Example<QqCharacter> example = Example.of(qqCharacterCriteria);
        return qqCharacterRepository.findOne(example).orElse(null);
    }

    @Override
    public List<QqCharacter> list() {
        return qqCharacterRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(QqCharacter qqCharacter) {
        qqCharacterRepository.save(qqCharacter);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        qqCharacterRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> idList) {
        qqCharacterRepository.deleteAllById(idList);
    }

}
