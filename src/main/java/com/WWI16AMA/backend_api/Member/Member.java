package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.Member.FlightAuthorization;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;

@Entity
public class Member {

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

    private List<Office> offices; //TODO: Using a list might be usefull since its possible to have more then one office

    private List<FlightAuthorization> flightAuthorization; //TODO: see above


    public Member() {

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
