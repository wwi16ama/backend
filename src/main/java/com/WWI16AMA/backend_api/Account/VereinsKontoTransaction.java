package com.WWI16AMA.backend_api.Account;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VereinsKontoTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "memberAcc_id")
    Account memberAccount;

    public VereinsKontoTransaction() {

    }

    public VereinsKontoTransaction(Transaction tr, Account memAcc) {

        // MAN BEACHTE DAS NEGATIVE VORZEICHEN!!
        super(-tr.getAmount(), tr.getType());
        if (memAcc == null) {
            throw new IllegalArgumentException("Member darf nicht null sein");
        }
        this.memberAccount = memAcc;
    }
}
