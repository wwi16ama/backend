package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;

public class IntTransactionEvent extends ExtTransactionEvent {

    public IntTransactionEvent(Member member, double amount, Transaction.FeeType type) {
        super(member, amount, type);
    }
}
