package com.WWI16AMA.backend_api.PlaneLog;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PlaneLogRepository extends PagingAndSortingRepository<PlaneLogEntry, Long> {
}
