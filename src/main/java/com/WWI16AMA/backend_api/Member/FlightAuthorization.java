package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
public class FlightAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Authorization authorization;
    @NotNull
    private LocalDate dateOfIssue;
    @NotNull
    private LocalDate expires;

    public FlightAuthorization() {

    }

    public FlightAuthorization(Authorization authorization, LocalDate dateOfIssue, LocalDate expires) {
        this.authorization = authorization;
        this.dateOfIssue = dateOfIssue;
        this.expires = expires;
    }

    public String getAuthorization() {
        return authorization.toString();
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public LocalDate getExpires() {
        return expires;
    }

    public void setExpires(LocalDate expires) {
        this.expires = expires;
    }

    public enum Authorization {
        PPLA,
        PPLB,
        BZFI,
        BZFII,
        LEHRBEFUGNIS
    }
}
