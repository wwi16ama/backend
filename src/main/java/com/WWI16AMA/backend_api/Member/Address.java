package com.WWI16AMA.backend_api.Member;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Pattern(regexp = "\\d{5}")
    private String postalCode;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z_äÄöÖüÜß \\.\\(\\)\\-]+")
    private String city;
    @NotBlank
    @Length(min = 3)
    private String streetAddress;

    public Address() {

    }

    public Address(String postalCode, String city, String streetAddress) {
        this.postalCode = postalCode;
        this.city = city;
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}
