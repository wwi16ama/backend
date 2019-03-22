package com.WWI16AMA.backend_api.Fee;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends PagingAndSortingRepository<Fee, Integer> {
}
