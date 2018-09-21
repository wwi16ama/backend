package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Address(){

    }

    private int postalCode;
    private String city;
    private String streetAddress;

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

    public Address(int postalCode, String city, String streetAddress) {
        this.postalCode = postalCode;
        this.city = city;
        this.streetAddress = streetAddress;
    }

    private Address() {

    }
}
