package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    private String email;
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private Address address;
    @Column(nullable = false)
    private String bankingAccount;
    @Column(nullable = false)
    private boolean admissioned;
    private String memberBankingAccount;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "members_offices",
            joinColumns = {@JoinColumn(name = "member_Id")},
            inverseJoinColumns = {@JoinColumn(name = "office_Id")}
    )
    private List<Office> offices = new ArrayList<>(); //TODO: Using a list might be usefull since its possible to have more then one office

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "member_Id")
    @Enumerated(EnumType.STRING)

    private List<FlightAuthorization> flightAuthorization = new ArrayList<>();


    public Member() {

    }

    /**
     * Constructor contains all Fields that always have to be set. ("Pflichtfelder")
     *
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param gender
     * @param status
     * @param email
     * @param address
     * @param bankingAccount
     * @param admissioned
     */
    public Member(String firstName, String lastName, LocalDate dateOfBirth, Gender gender, Status status, String email, Address address, String bankingAccount, boolean admissioned) {
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

    public List<Office> getOffices() {
        return offices;
    }

    public void setOffices(List<Office> offices) {
        this.offices = offices;
    }

    public List<FlightAuthorization> getFlightAuthorization() {
        return flightAuthorization;
    }

    public void setFlightAuthorization(List<FlightAuthorization> flightAuthorization) {
        this.flightAuthorization = flightAuthorization;
    }
}
