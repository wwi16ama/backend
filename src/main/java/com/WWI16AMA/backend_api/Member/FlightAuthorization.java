package com.WWI16AMA.backend_api.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


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

    private LocalDate dateOfIssue;

    private LocalDate expires;

    public String getAuthorization() {	//kp ob das dumm oder schlau is
        return authorization.title();
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

    public FlightAuthorization () {

    }
}
