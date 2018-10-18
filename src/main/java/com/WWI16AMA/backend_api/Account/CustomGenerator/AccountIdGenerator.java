package com.WWI16AMA.backend_api.Account.CustomGenerator;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class AccountIdGenerator implements IdentifierGenerator {

    final int START_NUMBER = 37459;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {

        int counter = 0;

        while(accountRepository.existsById(START_NUMBER+counter)){
            ++counter;
        }

        return START_NUMBER+counter;


    }
}
