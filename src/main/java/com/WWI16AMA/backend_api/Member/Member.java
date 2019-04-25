package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import com.WWI16AMA.backend_api.PilotLog.PilotLog;
import com.WWI16AMA.backend_api.Service.Service;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(name = "5-digit-Id", strategy = "com.WWI16AMA.backend_api.Member.MemberIdGenerator")
    @GeneratedValue(generator = "5-digit-Id")
    private Integer id;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z_äÄöÖüÜß\\-]+")
    private String firstName;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z_äÄöÖüÜß\\-]+")
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
    private String password;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    @NotBlank
    @Pattern(regexp = "DE[0-9]{20}")
    private String bankingAccount;
    @NotNull
    private boolean admissioned;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"balance", "transactions"})
    @JoinColumn(name = "account_id")
    private Account memberBankingAccount;

    @ManyToMany
    private List<Office> offices;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "member_id")
    private List<Service> services = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    private List<FlightAuthorization> flightAuthorization = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private PilotLog pilotLog;
    @JsonIgnore
    private boolean isDeleted;

    public Member() {

    }

    /**
     * Constructor contains all Fields that always have to be set. ("Pflichtfelder")
     */
    public Member(String firstName, String lastName, LocalDate dateOfBirth, Gender gender, Status status,
                  String email, Address address, String bankingAccount, boolean admissioned, String hashedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
        this.email = email;
        this.address = address;
        this.bankingAccount = bankingAccount;
        this.admissioned = admissioned;
        this.password = hashedPassword;
        this.memberBankingAccount = new Account();
        this.pilotLog = new PilotLog();
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
        this.pilotLog = member.getPilotLog();
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

    // So wird das PW nicht bei GET / POST _gesendet_.. (1/2)
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    // ..kann aber bei POST / PUT  _entgegengenommen_ werden (2/2)
    @JsonProperty
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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<FlightAuthorization> getFlightAuthorization() {
        return flightAuthorization;
    }

    public void setFlightAuthorization(List<FlightAuthorization> flightAuthorization) {
        this.flightAuthorization = flightAuthorization;
    }

    public PilotLog getPilotLog() {
        return pilotLog;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void delete(MemberRepository mr) {
        if (isDeleted) throw new IllegalStateException("This Entity is already deleted");
        this.isDeleted = true;
        mr.save(this);
    }

    public enum Status {
        ACTIVE,
        PASSIVE,
        HONORARYMEMBER
    }
}
