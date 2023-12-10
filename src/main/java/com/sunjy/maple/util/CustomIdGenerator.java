package com.sunjy.maple.util;

import com.sunjy.maple.constant.AppConstant;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

/**
 * @author created by sunjy on 12/5/23
 */
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return UUID.randomUUID().toString().replace("-", AppConstant.EMPTY_STRING);
    }

}
