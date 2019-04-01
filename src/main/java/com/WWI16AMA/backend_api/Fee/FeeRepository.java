package com.WWI16AMA.backend_api.Fee;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeRepository extends PagingAndSortingRepository<Fee, Integer> {

    Optional<Fee> findByCategory(Fee.Status category);
}
