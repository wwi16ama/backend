package com.WWI16AMA.backend_api.Account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VereinsKontoTransactionRepository extends CrudRepository<VereinsKontoTransaction, Long> {
}
