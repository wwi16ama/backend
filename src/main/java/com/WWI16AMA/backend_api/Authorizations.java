package com.WWI16AMA.backend_api;

import java.util.Date;

public class Authorizations {

    private String authorization;

    private Date dateOfIssue;

    private Date expires;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {

        if (authorization.equals("PPL-A")||authorization.equals("PPL-B")||authorization.equals("BZF-I")||authorization.equals("Lehrbefugnis")) {
            this.authorization = authorization;
        } else {
            //TODO: Fehler in den Daten Error
        }


    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Authorizations () {

    }
}
