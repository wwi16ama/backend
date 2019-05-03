package com.WWI16AMA.backend_api.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MemberContactDetails {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotNull
    private Address address;

    public MemberContactDetails() {

    }

    public MemberContactDetails(String firstName, String lastName, String email, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
