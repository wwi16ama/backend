package com.WWI16AMA.backend_api.Member;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends PagingAndSortingRepository<Member, Integer> {

    @Query(value = "SELECT * FROM member WHERE id = ?1 AND is_deleted = 0", nativeQuery = true)
    Optional<Member> findById(Integer id);

    @Query(value = "SELECT * FROM member WHERE is_deleted = 0", nativeQuery = true)
    Iterable<Member> findAll();

    @Query(value = "SELECT COUNT(*) FROM member WHERE is_deleted = 0", nativeQuery = true)
    long count();

    @Query(value = "SELECT * FROM member WHERE id = ?1", nativeQuery = true)
    Optional<Member> findByIdIncludingDeleted(Integer id);

    @Query(value = "SELECT * FROM member", nativeQuery = true)
    Iterable<Member> findAllIncludingDeleted();

    @Query(value = "SELECT COUNT(*) FROM member", nativeQuery = true)
    long countIncludingDeleted();
}
