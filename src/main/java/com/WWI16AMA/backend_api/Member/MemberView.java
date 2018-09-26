package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "firstName", "lastName"})
public class MemberView {

    private int Id;
    private String firstName;
    private String lastName;

    public MemberView() {
    }

    public MemberView(Member mem) {
        Id = mem.getId();
        this.firstName = mem.getFirstName();
        this.lastName = mem.getLastName();
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

}
