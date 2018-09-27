package com.WWI16AMA.backend_api.Plane;

import com.WWI16AMA.backend_api.Member.FlightAuthorization;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Plane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String number;
    @NotBlank
    private String name;
    @NotBlank
    private String position;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FlightAuthorization.Authorization neededAuthorization;

    Plane() {}

    public Plane(String number, String name, FlightAuthorization.Authorization neededAuthorization, String position) {

        this.number = number;
        this.name = name;
        this.position = position;
        this.neededAuthorization = neededAuthorization;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public FlightAuthorization.Authorization getNeededAuthorization() {
        return neededAuthorization;
    }

    public void setNeededAuthorization(String neededAuthorization) throws IllegalArgumentException {
        this.neededAuthorization = FlightAuthorization.Authorization.valueOf(neededAuthorization.replace("-", "").toUpperCase());
    }
}
