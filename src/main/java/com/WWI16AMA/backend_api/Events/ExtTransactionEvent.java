package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;

public class ExtTransactionEvent extends MemberEvent {

    private int amount;
    private Transaction.FeeType type;

    public ExtTransactionEvent(Member member, int amount, Transaction.FeeType type) {
        super(member);
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
