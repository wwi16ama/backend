package com.WWI16AMA.backend_api.Account;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import com.WWI16AMA.backend_api.Account.ProtectedAccount.VereinsAccount;
import com.WWI16AMA.backend_api.Events.ExtTransactionEvent;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    @Autowired
    private ApplicationEventPublisher publisher;

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
        publisher.publishEvent(new ExtTransactionEvent(acc, transaction));
        return transaction;
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'KASSIERER', 'SYSTEMADMINISTRATOR')")
    @GetMapping(path = "/vereinskonto")
    public VereinsAccount getVereinsAccount() {
        // TODO funktioniert nicht: zeigt nicht die bereits vorhandenen Transaktionen an :-(
        return (VereinsAccount) accountRepository.findById(VereinsAccount.getInstance().getId())
                .orElseThrow(() -> new IllegalStateException("Das Vereinskonto  ist verlorgen gegangen"));
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
