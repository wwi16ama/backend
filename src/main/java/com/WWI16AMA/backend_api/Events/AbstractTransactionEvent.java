package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import com.WWI16AMA.backend_api.Account.Transaction;

public abstract class AbstractTransactionEvent {
    private Account account;
    private Transaction transaction;

    public AbstractTransactionEvent(Account acc, Transaction tr) {
        this.account = acc;
        this.transaction = tr;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Account getAccount() {
        return account;
    }

}
