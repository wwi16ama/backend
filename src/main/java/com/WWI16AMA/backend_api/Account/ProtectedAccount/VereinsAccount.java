package com.WWI16AMA.backend_api.Account.ProtectedAccount;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.VereinsKontoTransaction;

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
    private List<VereinsKontoTransaction> vereinsTransactions = new ArrayList<>();

    public static VereinsAccount getInstance(AccountRepository accountRepository) {
        if (instance == null) {
            instance = new VereinsAccount();
            instance.setBalance(25000.0);
        }
        return accountRepository.save(instance);
        // return instance;
    }

//     public void addTransaction(VereinsKontoTransaction vtr) {
//         addToBalance(vtr.getAmount());
//         this.vereinsTransactions.add(vtr);
//     }
//
//     public List<VereinsKontoTransaction> getVereinsTransactions() {
//         return this.vereinsTransactions;
//     }
}
