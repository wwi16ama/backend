package com.WWI16AMA.backend_api.Member;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends PagingAndSortingRepository<Member, Integer> {

}
