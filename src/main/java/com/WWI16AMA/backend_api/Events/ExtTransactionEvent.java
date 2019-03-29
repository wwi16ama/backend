package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;

public class ExtTransactionEvent extends MemberEvent {

    private double amount;
    private Transaction.FeeType type;

    public ExtTransactionEvent(Member member, double amount, Transaction.FeeType type) {
        super(member);
        this.amount = amount;
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction.FeeType getType() {
        return type;
    }
}
