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

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER') or hasAnyRole('KASSIERER')")
    @GetMapping(path = "")
    public List<AccountView> getAllAccounts(
//            @RequestParam(defaultValue = "20") int limit,
//            @RequestParam(defaultValue = "0") int start,
//            @RequestParam(defaultValue = "asc") String direction,
//            @RequestParam(defaultValue = "id") String orderBy
    ) {
//        Sort sort = new Sort(Sort.Direction.fromString(direction), orderBy);
//        return accountRepository.findAll(PageRequest.of(start, limit, sort))
//                .stream()
//                .map(AccountView::new)
//                .collect(toList());
        Iterable<Account> accs = accountRepository.findAll();
        return StreamSupport.stream(accs.spliterator(), false)
                .map(AccountView::new)
                .collect(toList());
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER') or hasAnyRole('KASSIERER') or #id == principal.id")
    @GetMapping(path = "/{id}")
    public Account showAccountDetail(@PathVariable int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with the id " + id + " does not exist"));
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER') or hasAnyRole('KASSIERER') or #id == principal.id")
    @PostMapping(path = "/{id}/transactions")
    public Transaction addTransaction(@RequestBody Transaction transaction, @PathVariable int id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with the id " + id + " does not exist"));
        acc.addTransaction(transaction);
        acc.add2Balance(transaction.getAmount());
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
