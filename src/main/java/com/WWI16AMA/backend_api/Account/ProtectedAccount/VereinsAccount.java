package com.WWI16AMA.backend_api.Account.ProtectedAccount;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Account.VereinsKontoTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class VereinsAccount extends Account {

    private static VereinsAccount instance;

    private VereinsAccount() {
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<VereinsKontoTransaction> transactions = new ArrayList<>();

    public static VereinsAccount getInstance() {
        if (instance == null) {
            instance = new VereinsAccount();
            instance.setBalance(25000.0);
        }
        return instance;
    }

    public void addTransaction(VereinsKontoTransaction vtr) {
        addToBalance(vtr.getAmount());
        this.transactions.add(vtr);
        System.out.println("addTransaction is called");
    }

    public List<VereinsKontoTransaction> getVereinskontoTransactions() {
        return this.transactions;
    }

    @JsonIgnore
    @Override
    public List<Transaction> getTransactions() {
        return super.getTransactions();
    }
}
