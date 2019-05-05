package com.WWI16AMA.backend_api.Account.ProtectedAccount;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.VereinsKontoTransaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class VereinsAccount extends Account {

    private static VereinsAccount instance;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<VereinsKontoTransaction> vereinsTransactions = new ArrayList<>();

    private VereinsAccount() {
    }

    public static VereinsAccount getInstance(AccountRepository accountRepository) {
        if (instance == null) {
            instance = new VereinsAccount();
            instance.setBalance(25000.0);
            accountRepository.save(instance);
        }
        return (VereinsAccount) accountRepository.findById(instance.getId())
                .orElseThrow(() -> new IllegalStateException("VereinsAccount nicht richtig gespeichert"));
    }

}
