package com.WWI16AMA.backend_api.Member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends CrudRepository<Office, Integer> {

    Optional<Office> findByTitle(Office.Title title);
}
