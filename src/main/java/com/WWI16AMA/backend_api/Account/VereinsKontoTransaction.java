package com.WWI16AMA.backend_api.Account;

import com.WWI16AMA.backend_api.Member.Member;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VereinsKontoTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    public VereinsKontoTransaction(Transaction tr, Member member) {
        super(tr.getAmount(), tr.getType());
        this.member = member;
    }
}
