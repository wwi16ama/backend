package com.WWI16AMA.backend_api.Events;


import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;

public class EmailNotificationEvent extends MemberEvent {

    private Type type;
    private Transaction transaction;

    public EmailNotificationEvent(Member member, Type type, Transaction transaction) {
        super(member);
        this.type = type;
        this.transaction = transaction;
    }

    public Type getType() {
        return type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public enum Type {
        AUFWANDSENTSCHÄDIGUNG,
        AUFWENDUNGEN,
        DELETE_MAIL_EXTERNAL,
        DELETE_MAIL_INTERNAL,
        MEMBER_DATA

    }

}
