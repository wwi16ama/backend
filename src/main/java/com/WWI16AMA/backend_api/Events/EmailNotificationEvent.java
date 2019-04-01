package com.WWI16AMA.backend_api.Events;


import com.WWI16AMA.backend_api.Member.Member;

public class EmailNotificationEvent extends MemberEvent {

    public EmailNotificationEvent(Member member) {
        super(member);
    }
}
