package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public Address(){

    }

    private int postalCode;
    private String city;
    private String streetAddress;
    private int streetNumber;

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
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

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Address(int postalCode, String city, String streetAddress, int streetNumber) {
        this.postalCode = postalCode;
        this.city = city;
        this.streetAddress = streetAddress;
        this.streetNumber = streetNumber;
    }
}
