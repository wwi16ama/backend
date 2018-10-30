package com.WWI16AMA.backend_api.Account;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private FeeType type;
    @DateTimeFormat(pattern = "yyyy-MM-dd:mm:ss")
    private LocalDate timestamp;
    @NotNull
    private double amount;
    public Transaction() {
        this.timestamp = LocalDate.now();
    }

    public Transaction(double amount, FeeType feeType) {
        this.timestamp = LocalDate.now();
        this.amount = amount;
        this.type = feeType;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public FeeType getType() {
        return type;
    }

    public void setType(FeeType type) {
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

    public enum FeeType {
        GEBÜHR,
        AUFWANDSENTSCHÄDIGUNG,
        ZAHLUNG
    }
}
