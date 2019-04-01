package com.WWI16AMA.backend_api.Events;


import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;

public class EmailNotificationEvent extends MemberEvent {

    public EmailNotificationEvent(Member member) {
        super(member);
    }


    public EmailNotificationEvent(Member member, Type type, Transaction transaction) {
        super(member);
        this.type = type;
        this.transaction = transaction;
    }

    private Type type;


    private Transaction transaction;

    public Type getType() {
        return type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public enum Type {
        AUFWANDSENTSCHÄDIGUNG,
        AUFWENDUNGEN,
        DELETE_MAIL_INTERNAL,
        DELETE_MAIL_EXTERNAL,
        MEMBER_DATA

    }

}
