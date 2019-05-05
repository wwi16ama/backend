package com.WWI16AMA.backend_api.Events;


import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Plane.Plane;

public class EmailNotificationEvent extends MemberEvent {

    private Type type;
    private Transaction transaction;
    private Plane plane;

    public EmailNotificationEvent(Member member, Type type, Transaction transaction) {
        super(member);
        this.type = type;
        this.transaction = transaction;
    }

    public EmailNotificationEvent(Member member, Type type, Transaction transaction, Plane plane) {
        super(member);
        this.type = type;
        this.transaction = transaction;
        this.plane = plane;
    }

    public Type getType() {
        return type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Plane getPlane() {
        return plane;
    }

    public enum Type {
        AUFWANDSENTSCHÄDIGUNG,
        AUFWENDUNGEN,
        TANKEN,
        LOW_BALANCE,
        DELETE_MAIL_EXTERNAL,
        DELETE_MAIL_INTERNAL,
        MEMBER_DATA

    }

}
