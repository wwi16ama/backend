package com.WWI16AMA.backend_api.Events;

import com.WWI16AMA.backend_api.Member.Member;

public abstract class MemberEvent {

    private Member member;
    private long timestamp;

    public MemberEvent(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member in Event is null");
        }
        this.member = member;
        this.timestamp = System.currentTimeMillis();
    }

    public Member getMember() {
        return this.member;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }
}
