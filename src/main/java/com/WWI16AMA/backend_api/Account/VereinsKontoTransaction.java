package com.WWI16AMA.backend_api.Account;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VereinsKontoTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "memberAcc_id")
    @JsonIgnoreProperties(value = {"balance", "transactions"})
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

    public Account getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(Account memberAccount) {
        this.memberAccount = memberAccount;
    }
}
