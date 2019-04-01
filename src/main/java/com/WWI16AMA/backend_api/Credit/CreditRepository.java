package com.WWI16AMA.backend_api.Credit;

import com.WWI16AMA.backend_api.Service.ServiceName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Integer> {

    Optional<Credit> findCreditByServiceName(ServiceName serviceName);
}
