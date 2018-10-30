package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "firstName", "lastName", "memberBankingAccountId"})
public class MemberView {

    private int Id;
    private String firstName;
    private String lastName;
    private Integer memberBankingAccountId;

    public MemberView() {
    }

    public MemberView(Member mem) {
        Id = mem.getId();
        this.firstName = mem.getFirstName();
        this.lastName = mem.getLastName();
        this.memberBankingAccountId = mem.getMemberBankingAccount().getId();
    }

    public int getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getMemberBankingAccount() {
        return memberBankingAccountId;
    }
}
