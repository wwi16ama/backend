package com.WWI16AMA.backend_api.Airplane;

import com.WWI16AMA.backend_api.Member.FlightAuthorization;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String planeNumber;
    @NotNull
    private String planeName;
    @NotNull
    private String planePosition;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FlightAuthorization.Authorization planeAuthorization;

    Airplane() {}

    public Airplane(String planeNumber, String planeName, FlightAuthorization.Authorization planeAuthorization, String planePosition) {

        this.planeNumber = planeNumber;
        this.planeName = planeName;
        this.planePosition = planePosition;
        this.planeAuthorization = planeAuthorization;

    }

    public int getId() {
        return id;
    }

    public String getPlaneNumber() {
        return planeNumber;
    }

    public void setPlaneNumber(String planeNumber) {
        this.planeNumber = planeNumber;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public String getPlanePosition() {
        return planePosition;
    }

    public void setPlanePosition(String planePosition) {
        this.planePosition = planePosition;
    }

    public FlightAuthorization.Authorization getPlaneAuthorization() {
        return planeAuthorization;
    }

    public void setPlaneAuthorization(String planeAuthorization) throws IllegalArgumentException{
        this.planeAuthorization = FlightAuthorization.Authorization.valueOf(planeAuthorization.replace("-", "").toUpperCase());
    }
}
