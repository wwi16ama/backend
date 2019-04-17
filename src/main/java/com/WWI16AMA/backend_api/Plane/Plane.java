package com.WWI16AMA.backend_api.Plane;

import com.WWI16AMA.backend_api.Member.FlightAuthorization;
import com.WWI16AMA.backend_api.PlaneLog.PlaneLogEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Plane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "[A-Z]{1}-{1}[A-Z]{4}")
    private String number;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z_äÄöÖüÜß0-9/. \\-]+")
    private String name;
    @NotBlank
    private String position;
    @Column(length = 500)
    private URL pictureUrl;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FlightAuthorization.Authorization neededAuthorization;
    @PositiveOrZero
    @Max(999)
    private double pricePerBookedHour;
    @PositiveOrZero
    @Max(999)
    private double pricePerFlightMinute;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "plane_id")
    private List<PlaneLogEntry> planeLog;

    Plane() {
    }

    public Plane(String number, String name, FlightAuthorization.Authorization neededAuthorization, String position,
                 URL pictureUrl, double pricePerBookedHour, double pricePerFlightMinute) {

        this.number = number;
        this.name = name;
        this.position = position;
        this.pictureUrl = pictureUrl;
        this.neededAuthorization = neededAuthorization;
        this.pricePerBookedHour = pricePerBookedHour;
        this.pricePerFlightMinute = pricePerFlightMinute;
        this.planeLog = new ArrayList<>();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public URL getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(URL pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public FlightAuthorization.Authorization getNeededAuthorization() {
        return neededAuthorization;
    }

    public void setNeededAuthorization(String neededAuthorization) throws IllegalArgumentException {
        this.neededAuthorization = FlightAuthorization.Authorization.valueOf(
                neededAuthorization.replace("-", "").toUpperCase());
    }

    public double getPricePerBookedHour() {
        return pricePerBookedHour;
    }

    public void setPricePerBookedHour(double pricePerBookedHour) {
        this.pricePerBookedHour = pricePerBookedHour;
    }

    public double getPricePerFlightMinute() {
        return pricePerFlightMinute;
    }

    public void setPricePerFlightMinute(double pricePerFlightMinute) {
        this.pricePerFlightMinute = pricePerFlightMinute;
    }


    public PlaneLogEntry addPlaneLogEntry(PlaneLogEntry entry) {
        this.planeLog.add(entry);
        return entry;
    }

    public List<PlaneLogEntry> getPlaneLog() {
        return planeLog;
    }

    public void setPlaneLog(List<PlaneLogEntry> entries) {
        this.planeLog = entries;
    }


}
