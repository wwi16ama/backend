package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @Pattern(regexp = "DE[0-9]{20}")
    private String bankingAccount;
    @NotNull
    private boolean admissioned;
    private String memberBankingAccount;

    @ManyToMany
    private Set<Office> offices = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<FlightAuthorization> flightAuthorization = new ArrayList<>();


    public Member() {

    }

    /**
     * Constructor contains all Fields that always have to be set. ("Pflichtfelder")
     */
    public Member(String firstName, String lastName, LocalDate dateOfBirth, Gender gender, Status status,
                  String email, Address address, String bankingAccount, boolean admissioned) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
        this.email = email;
        this.address = address;
        this.bankingAccount = bankingAccount;
        this.admissioned = admissioned;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getBankingAccount() {
        return bankingAccount;
    }

    public void setBankingAccount(String bankingAccount) {
        this.bankingAccount = bankingAccount;
    }

    public boolean isAdmissioned() {
        return admissioned;
    }

    public void setAdmissioned(boolean admissioned) {
        this.admissioned = admissioned;
    }

    public String getMemberBankingAccount() {
        return memberBankingAccount;
    }

    public void setMemberBankingAccount(String memberBankingAccount) {
        this.memberBankingAccount = memberBankingAccount;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public void setOffices(List<Office> offices) {
        this.offices = new HashSet<Office>(offices);
    }

    public List<FlightAuthorization> getFlightAuthorization() {
        return flightAuthorization;
    }

    public void setFlightAuthorization(List<FlightAuthorization> flightAuthorization) {
        this.flightAuthorization = flightAuthorization;
    }
}
