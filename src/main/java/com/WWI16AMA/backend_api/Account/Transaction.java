package com.WWI16AMA.backend_api.Account;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @NotNull
    private final LocalDateTime timestamp;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private FeeType type;
    @NotBlank
    @Pattern(regexp = ".{4,50}")
    private String text;
    // TODO sollte vllt currency oder BigDecimal werden
    @NotNull
    private double amount;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(double amount, String text, FeeType feeType) {
        this.timestamp = LocalDateTime.now();
        this.amount = BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        this.text = text;
        this.type = feeType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    /**
     * TODO:
     * es wäre zB im Eventlistener cool, leicht zu sehen, ob es sich um einen Typ
     * handelt, der auf dem Mitgliedskonto "eingezahlt" oder "abgebucht" wird,
     * zB indem alle "Einzahlungen" mit "GUTSCHRIFT_" beginnen, à la: GUTSCHRIFT_AMT,
     * genauso alle "Abbuchungen" zB mit "GEBÜHR_", also "GEBÜHR_MITGLIEDSBEITRAG".
     */
    public enum FeeType {
        MITGLIEDSBEITRAG,
        GEBÜHRFLUGZEUG,
        GUTSCHRIFTAMT,
        GUTSCHRIFTLEISTUNG,
        EINZAHLUNG,
        AUSZAHLUNG
    }

}
