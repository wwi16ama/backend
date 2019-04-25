package com.WWI16AMA.backend_api.Plane;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaneRepository extends PagingAndSortingRepository<Plane, Integer> {
    Optional<Plane> findByNumber(String number);

    @Query(value = "SELECT * FROM plane WHERE id = ?1 AND is_deleted = 0", nativeQuery = true)
    Optional<Plane> findById(Integer id);

    @Query(value = "SELECT * FROM plane WHERE is_deleted = 0", nativeQuery = true)
    Iterable<Plane> findAll();

    @Query(value = "SELECT COUNT(*) FROM plane WHERE is_deleted = 0", nativeQuery = true)
    long count();

    @Query(value = "SELECT * FROM plane WHERE id = ?1", nativeQuery = true)
    Optional<Plane> findByIdIncludingDeleted(Integer id);

    @Query(value = "SELECT * FROM plane", nativeQuery = true)
    Iterable<Plane> findAllIncludingDeleted();

    @Query(value = "SELECT COUNT(*) FROM plane", nativeQuery = true)
    long countIncludingDeleted();
}
