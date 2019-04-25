package com.WWI16AMA.backend_api.PlaneLog;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneLogRepository extends PagingAndSortingRepository<PlaneLogEntry, Long> {
}
