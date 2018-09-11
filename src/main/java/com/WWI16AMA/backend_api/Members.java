package com.WWI16AMA.backend_api;

import java.util.Date;
import java.util.List;

import com.WWI16AMA.backend_api.Address;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String sex;

    private String status;

    private String email;

    private Address address;

    private String bankingAccount;

    private boolean admissioned;

    private String memberBankingAccount;

    private List<Offices> offices; //TODO: Using a list might be usefull since its possible to have more then one office

    private List<Authorizations> flightAuthorization; //TODO: see above


    public Members () {

    }


    //Getter & Setter

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Offices getOffices(int id) {
        return offices.get(id);
    }

    public void setOffices(Offices offices) {
        this.offices.add(offices);
    }

    public Authorizations getFlightAuthorization(int id) {
        return flightAuthorization.get(id);
    }

    public void setFlightAuthorization(Authorizations flightAuthorization) {
        this.flightAuthorization.add(flightAuthorization);
    }
}
