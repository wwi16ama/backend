package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import com.WWI16AMA.backend_api.Account.Transaction;

public class IntTransactionEvent extends AbstractTransactionEvent {

    public IntTransactionEvent(Account acc, Transaction transaction) {
        super(acc, transaction);
    }
}
