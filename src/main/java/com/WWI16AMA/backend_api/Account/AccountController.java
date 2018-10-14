package com.WWI16AMA.backend_api.Account;

import com.WWI16AMA.backend_api.Member.MemberView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping(path = "accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(path = "")
    public Iterable<Account> getAllAccounts(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String orderBy )
    {
        Sort sort = new Sort(Sort.Direction.fromString(direction), orderBy);
        return accountRepository.findAll(PageRequest.of(start, limit, sort)).stream()
                .collect(toList());
    }

    @GetMapping(path = "/transactions/{id}")
    public Iterable<Transaction> getAllTransactions(@PathVariable int id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with the id " + id + " does not exist"));
        return acc.getTransactions();
    }





    @PostMapping(path = "/transactions/{id}")
    public Transaction addTransaction(@RequestBody Transaction transaction, @PathVariable int id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with the id " + id + " does not exist"));
        acc.addTransaction(transaction);
        acc.add2Balance(transaction.getAmount());
        accountRepository.save(acc);
        return transaction;
    }
}
