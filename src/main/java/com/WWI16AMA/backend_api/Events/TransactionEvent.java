package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Account.Transaction;
import org.springframework.context.ApplicationEvent;

public class TransactionEvent extends ApplicationEvent {

    private int amount;
    private Transaction.FeeType type;


    public TransactionEvent(Object source) {
        super(source);
    }

    public TransactionEvent(Object source, int amount, Transaction.FeeType type) {
        super(source);
        this.amount = amount;
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public Transaction.FeeType getType() {
        return type;
    }
}
