package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id", "firstName", "lastName", "memberBankingAccountId", "offices"})
public class MemberView {

    private int Id;
    private String firstName;
    private String lastName;
    private Integer memberBankingAccountId;
    private List<Office> offices;
    private double sumCredits;

    public MemberView() {
    }

    public MemberView(Member mem) {
        Id = mem.getId();
        this.firstName = mem.getFirstName();
        this.lastName = mem.getLastName();
        this.memberBankingAccountId = mem.getMemberBankingAccount().getId();
        this.offices = mem.getOffices();
        this.sumCredits = mem.getSumCredits();
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

    public Integer getMemberBankingAccountId() {
        return memberBankingAccountId;
    }

    public List<Office> getOffices() {
        return offices;
    }

    public double getSumCredits() {
        return sumCredits;
    }
}
