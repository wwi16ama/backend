package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class FlightAuthorization {

    public enum Authorization {
        PPLA,
        PPLB,
        BZFI,
        BZFII,
        LEHRBEFUGNIS
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Authorization authorization;

    private LocalDate dateOfIssue;

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

    public void setAuthorization(String authorization) throws IllegalArgumentException {

        this.authorization = Authorization.valueOf(authorization.replace("-", "").toUpperCase());
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
}
