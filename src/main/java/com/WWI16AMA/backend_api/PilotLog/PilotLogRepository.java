package com.WWI16AMA.backend_api.PilotLog;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PilotLogRepository extends PagingAndSortingRepository<PilotLog, Integer> {
}
