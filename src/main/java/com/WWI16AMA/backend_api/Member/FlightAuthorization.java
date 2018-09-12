package com.WWI16AMA.backend_api.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class FlightAuthorization {

    @Entity
    public enum Authorization {
        PPLA("PPL-A"),
        PPLB("PPL-B"),
        BZFI("BZF-I"),
        BZFII("BZF-II"),
        LEHRBEFUGNIS("Lehrbefugnis");


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        private String title;

        Authorization(String title){
            this.title = title;
        }

        public String title(){
            return title;
        }
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Authorization authorization;

    private Date dateOfIssue;

    private Date expires;

    public String getAuthorization() {	//kp ob das dumm oder schlau is
        return authorization.title();
    }

    public void setAuthorization(String authorization) throws IllegalArgumentException {

        this.authorization = Authorization.valueOf(authorization.replace("-", "").toUpperCase());
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

    public FlightAuthorization () {

    }
}
