package com.WWI16AMA.backend_api.Account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private LocalDateTime timestamp;

    @NotNull
    private double amount;

    @NotNull
    Transaction.Fee type;

    public Transaction(){}

    public Transaction(@NotNull double amount, @NotNull Transaction.Fee type) {
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public enum Fee {
        GEBÜHR,
        AUFWANDSENTSCHÄDIGUNG,
        ZAHLUNG
    }
}
