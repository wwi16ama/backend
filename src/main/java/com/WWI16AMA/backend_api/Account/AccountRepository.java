package com.WWI16AMA.backend_api.Account;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {
}
