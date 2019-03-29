package com.WWI16AMA.backend_api.Account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping(path = "accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'KASSIERER')")
    @GetMapping(path = "")
    public List<AccountView> getAllAccounts() {
        Iterable<Account> accs = accountRepository.findAll();
        return StreamSupport.stream(accs.spliterator(), false)
                .map(AccountView::new)
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'KASSIERER') or #id == principal.id")
    @GetMapping(path = "/{id}")
    public Account showAccountDetail(@PathVariable int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with the id " + id + " does not exist"));
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'KASSIERER') or #id == principal.getMemberBankingAccount().getId()")
    @PostMapping(path = "/{id}/transactions")
    public Transaction addTransaction(@RequestBody Transaction transaction, @PathVariable int id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with the id " + id + " does not exist"));
        acc.addTransaction(transaction);
        accountRepository.save(acc);
        return transaction;
    }

    @JsonPropertyOrder({"id", "balance"})
    private class AccountView {
        private int id;

        private double balance;

        public AccountView() {
        }

        public AccountView(Account acc) {
            this.id = acc.getId();
            this.balance = acc.getBalance();
        }

        public int getId() {
            return id;
        }

        public double getBalance() {
            return balance;
        }
    }

}
