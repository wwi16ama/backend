package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.Account.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @NotBlank
    private String password = "{bcrypt}$2y$12$E7xkkbPYS3lwOFs//ziVEuj.Z5ZLJ2XGrkYqtk2H7.s6G7CvdPqfi";

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @Pattern(regexp = "DE[0-9]{20}")
    private String bankingAccount;
    @NotNull
    private boolean admissioned;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"balance", "transactions"})
    private Account memberBankingAccount = new Account();

    @ManyToMany
    private List<Office> offices;

    @OneToMany(cascade = {CascadeType.ALL})
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

    public Member(Member member) {
        this.id = member.getId();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.dateOfBirth = member.getDateOfBirth();
        this.gender = member.getGender();
        this.status = member.getStatus();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.address = member.getAddress();
        this.bankingAccount = member.getBankingAccount();
        this.admissioned = member.isAdmissioned();
        this.offices = member.getOffices();
        this.memberBankingAccount = member.getMemberBankingAccount();
        this.flightAuthorization = member.getFlightAuthorization();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Account getMemberBankingAccount() {
        return memberBankingAccount;
    }

    public void setMemberBankingAccount(Account memberBankingAccount) {
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
