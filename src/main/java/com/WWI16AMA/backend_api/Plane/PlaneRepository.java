package com.WWI16AMA.backend_api.Plane;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaneRepository extends PagingAndSortingRepository<Plane, Integer> {
    Optional<Plane> findByNumber(String number);
}
