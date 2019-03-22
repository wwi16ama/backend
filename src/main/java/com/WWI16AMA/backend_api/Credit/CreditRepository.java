package com.WWI16AMA.backend_api.Credit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Integer> {
}
