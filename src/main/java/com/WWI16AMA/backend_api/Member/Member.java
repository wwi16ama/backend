package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private Status status;
    private String email;
    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;
    private String bankingAccount;
    private boolean admissioned;
    private String memberBankingAccount;
    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Office> offices; //TODO: Using a list might be usefull since its possible to have more then one office
    @OneToMany(cascade = {CascadeType.ALL})
    private List<FlightAuthorization> flightAuthorization = new ArrayList<FlightAuthorization>(10); //TODO: see above

    private Member() {

    }

    /**
     * Constructor contains all Fields that always have to be set. ("Pflichtfelder")
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param sex
     * @param status
     * @param email
     * @param address
     * @param bankingAccount
     * @param admissioned
     */
    public Member(String firstName, String lastName, LocalDate dateOfBirth, String sex, Status status, String email, Address address, String bankingAccount, boolean admissioned){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.status = status;
        this.email = email;
        this.address = address;
        this.bankingAccount = bankingAccount;
        this.admissioned = admissioned;
    }
    //Getter & Setter

    public Integer getId() {
        return id;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) { this.status = status; }

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

    public Office getOffices(int id) {
        return offices.get(id);
    }

    public void setOffices(Office offices) {
        this.offices.add(offices);
    }

    public FlightAuthorization getFlightAuthorization(int id) {
        return flightAuthorization.get(id);
    }

    public void setFlightAuthorization(FlightAuthorization flightAuthorization) {
        this.flightAuthorization.add(flightAuthorization);
    }
}
