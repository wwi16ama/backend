package com.WWI16AMA.backend_api.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double balance;

    @OneToMany
    private List<Transaction> transactions = new ArrayList<>();

    public Account() {
        balance = 0;
    }

}
