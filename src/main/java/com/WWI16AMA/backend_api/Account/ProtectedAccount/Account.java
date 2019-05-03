package com.WWI16AMA.backend_api.Account.ProtectedAccount;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GenericGenerator(name = "5-digit-Id", strategy = "com.WWI16AMA.backend_api.Account.CustomGenerator.AccountIdGenerator")
    @GeneratedValue(generator = "5-digit-Id")
    private int id;

    @NotNull
    private double balance;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "memberBankingAccount")
    @JsonIgnore
    private Member member;

    public Account() {
        balance = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    void addToBalance(double amount) {
        this.balance += amount;
    }

    void addTransaction(Transaction transaction) {
        addToBalance(transaction.getAmount());
        this.transactions.add(transaction);
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
